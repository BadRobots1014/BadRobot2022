package frc.robot.commands;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Optional;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;

import frc.robot.commands.drive.ArcadeDriveStrategy;
import frc.robot.commands.drive.DriveStraightStrategy;
import frc.robot.commands.drive.AnchorStrategy;
import frc.robot.commands.drive.DriveStrategy;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.drive.PivotTurnStrategy;

public class TeleopDriveCommand extends CommandBase {
    private final DriveTrainSubsystem m_drive;
    private final Supplier<Double> m_xSource;
    private final Supplier<Double> m_ySource;
    private final Supplier<Double> m_throttleSource;

    public final DriveStrategy m_arcadeStrategy;
    public final DriveStrategy m_driveStraightStrategy;
    public final DriveStrategy m_pivotTurnStrategy;
    public final DriveStrategy m_anchorStrategy;

    private Optional<DriveStrategy> m_strategy;

    private final ShuffleboardTab m_tab;

    public TeleopDriveCommand(DriveTrainSubsystem drive, GyroSubsystem gyro, Supplier<Double> xSource,
            Supplier<Double> ySource, Supplier<Double> throttleSource) {

        m_drive = drive;
        m_xSource = xSource;
        m_ySource = ySource;
        m_throttleSource = throttleSource;

        m_arcadeStrategy = new ArcadeDriveStrategy();
        m_driveStraightStrategy = new DriveStraightStrategy(gyro);
        m_pivotTurnStrategy = new PivotTurnStrategy();
        m_anchorStrategy = new AnchorStrategy(gyro);

        // Initialize teleop. driving without a current strategy.
        //
        // This will display in the "Teleop. Drive: Strategy" Shuffleboard widget as "None" until a
        // strategy is selected.
        m_strategy = Optional.empty();

        m_tab = Shuffleboard.getTab("Teleop. Drive");
        m_tab.addString("Strategy", () -> m_strategy.map(DriveStrategy::getName).orElse("None"));
        m_tab.addNumber("X", m_xSource::get);
        m_tab.addNumber("Y", m_ySource::get);
        m_tab.addNumber("Throttle (%)", m_throttleSource::get);
        m_tab.addNumber("Yaw (deg.)", gyro::getYaw);
        m_tab.addNumber("Rotational PID", gyro::getRotationalPid);

        // Although {@link TeleopDriveCommand} doesn't use the gyroscope directly, some of its
        // strategies do, and they cannot add requirements themselves.
        addRequirements(drive, gyro);
    }

    /*
     * Command methods --------------------------------------------------------
     */

    @Override
    public void initialize() {
        // HACK:
        //
        // TL;DR: Without this line of code, the robot may go berserk once it is enabled *in a
        // different position or angle* than when it was previously disabled.
        //
        // When {@link TeleopDriveCommand} is interrupted (i.e., when another command that requires
        // {@link DriveTrainSubsystem} is queued, or when the robot is disabled from the driver
        // station)---and, by extension, when the current drive strategy is interrupted---the only
        // clean-up code executed is {@link #end}. Therefore, if this drive strategy depends on some
        // external data that is modified while {@link TeleopDriveCommand} is paused, then it must
        // be notified or normalized somehow upon resumption.
        //
        // There are two drive strategies like this---{@link AnchorStrategy} and
        // {@link DriveStraightStrategy}---both of which rely on yaw and/or disposition measurements
        // from an external {@link GyroscopeSubsystem}. Now, imagine the following hypothetical:
        //
        //   1. The driver moves the joystick into the deadzone.
        //   2. The anchor strategy is selected, which sets the gyroscope's rotational and
        //      positional PID setpoints to the current angle and displacement.
        //   3. The driver disables the robot from the driver station.
        //   4. The robot is rotated or displaced while disabled.
        //   5. The driver enables the robot from the driver station with the joystick still in the
        //      deadzone.
        //   6. The anchor strategy is re-selected. Because the last strategy was also to anchor,
        //      the anchor strategy is *not* reset.
        //   7. The anchor strategy resumes using the setpoints from *before the robot was
        //      disabled*.
        //   8. Without any driver interaction since enabling, the robot immediately drives the
        //      motors in an attempt to return to its original rotation and displacement, possibly
        //      endangering bystanders or the driver in the process.
        //
        // Now, imagine that this hypothetical isn't a hypothetical... and you can see why this line
        // of code is important.
        m_strategy.ifPresent(DriveStrategy::reset);
    }

    @Override
    public void execute() {
        // Poll the current joystick position.
        final double x = m_xSource.get();
        final double y = m_ySource.get();

        assert this.coordinateIsValid(x) : "X-coordinate is out-of-range";
        assert this.coordinateIsValid(y) : "Y-coordinate is out-of-range";

        // Note: Strategy selection must use the *original* position of the joystick for region
        // detection. Do not scale 'x' and 'y' by the throttle power yet!
        final DriveStrategy nextStrategy = this.getNextDriveStrategy(x, y);

        // Determine if the current stategy is nonexistent or differs from the new strategy.
        if (m_strategy.filter(nextStrategy::equals).isEmpty()) {
            // Update the current strategy.
            m_strategy = Optional.of(nextStrategy);

            // The new strategy wasn't used last, so its state is likely stale. We will reset it to
            // a normalized state.
            nextStrategy.reset();

            if (nextStrategy.shouldLockPosition()) {
                System.out.println("Locking position by stopping drivetrain");
                m_drive.stop();
            }
        }

        final double throttle = m_throttleSource.get();
        assert this.throttleIsValid(throttle) : "Throttle is out-of-range";

        // Execute the next strategy.
        final WheelSpeeds wheelSpeeds = nextStrategy.execute(
            this.scaleCoordinate(x, throttle),
            this.scaleCoordinate(y, throttle)
        );

        m_drive.tankDrive(wheelSpeeds.left, wheelSpeeds.right);
    }

    private boolean coordinateIsValid(final double coord) {
        return (coord <= 1.0) && (coord >= -1.0);
    }

    private DriveStrategy getNextDriveStrategy(final double x, final double y) {
        // 'x' and 'y' represent a Cartesian point in the plane of possible joystick positions.
        // There are certain regions in this plane that correspond to certain drive strategies.
        //
        // The simplest of these, and which also serves as a general joystick deadzone, is the
        // anchor region. This region is a circle of radius
        // {@link ControllerConstants#kDeadzoneRadius} centered at the origin. If the joystick
        // position is within the anchor region, then the robot *must* anchor---this region takes
        // precedence over all others.
        //
        // The regions with the second highest precedence are those for driving straight and
        // pivot-turning. These regions are narrow isosceles triangles symmetric along the X and Y
        // axes and which are reflected across, and have an apex at, the origin. The base lengths of
        // these triangles are defined by {@link DriveTrainConstants#kDriveStraightRegionBaseLength}
        // and {@link DriveTrainConstants#kPivotTurnRegionBaseLength}.
        //
        // Finally, if the joystick position is not within any of the previous regions, then the
        // robot defaults to arcade-driving.

        // Anchor region:
        //
        // This region is simply a circle, so if the distance of the joystick position from the
        // origin is less than or equal to this region's radius, then the robot anchors.
        final double originDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        if (originDistance <= ControllerConstants.kDeadzoneRadius) {
            return m_anchorStrategy;
        }

        final double absX = Math.abs(x);
        final double absY = Math.abs(y);

        // Pivot-turn region:
        //
        // This region is an isosceles triangle symmetric about the X-axis, with an apex at the
        // origin, and reflected across the origin as well. However, for the purpose of detection,
        // we only need to consider quadrant 1.
        //
        // In quadrant 1, this region is simply a line with a Y-intercept at the origin and a slope
        // of half the base length (divided by the maximum X value, 1). If we imagine this line as a
        // linear function, then each output represents the maximum possible Y-coordinate of the
        // joystick to be within the pivot-turn region for a given X-coordinate. Therefore, pivot-
        // turn detection is fairly straightforward: find this maximum Y-coordinate by applying the
        // joystick X-coordinate to this linear function (i.e., multiplying the half of the base
        // length by X), and if the actual joystick Y-coordinate is equal to or less than this
        // maximum, then the robot pivot-turns.

        final Function<Double, Double> pivotTurnLinearFunction = (input) -> {
            final double maximumOutput = ControllerConstants.kPivotTurnRegionHalfBaseLength;
            return maximumOutput * input;
        };

        if (absY <= pivotTurnLinearFunction.apply(absX)) {
            return m_pivotTurnStrategy;
        }

        // Drive straight:
        //
        // This follows the same principle as that of the pivot-turn region *except* that the drive-
        // straight linear function is *the inverse of* the pivot-turn linear function, so X and Y
        // must be swapped.

        final Function<Double, Double> driveStraightLinearFunction = (input) -> {
            final double maximumOutput = ControllerConstants.kDriveStraightRegionHalfBaseLength;
            return maximumOutput * input;
        };

        if (absX <= driveStraightLinearFunction.apply(absY)) {
            return m_driveStraightStrategy;
        }

        // Arcade-drive: Default to arcade-driving.
        return m_arcadeStrategy;
    }

    private boolean throttleIsValid(final double throttle) {
        // Throttle is a percentage.
        return (throttle <= 1.0) && (throttle >= 0.0);
    }

    private double scaleCoordinate(double coord, final double throttle) {
        // Redefine the coordinate such that it smoothly approaches 0 at the edge of the deadzone.
        //
        // A naive deadzone implementation simply filters-out coordinates below a threshold. A major
        // flaw in this approach is that the coordinates *outside* the deadzone are not scaled
        // accordingly; in other words, if 0.29 is filtered-out but 0.30 isn't, then the driver will
        // notice that the robot 'jerks' when crossing the deadzone boundary as it toggles between 0
        // and 0.30 without any smooth transition.
        //
        // Instead, we want the coordinates outside the deadzone to be defined relative to the edge
        // of the deadzone rather than the origin.
        //
        // Note: This process *must* occur before throttle scaling.
        coord = MathUtil.applyDeadband(coord, ControllerConstants.kDeadzoneRadius);

        // Scale the coordinate by the throttle power.
        coord *= throttle;

        return coord;
    }

    @Override
    public void end(boolean interrupted) {
        // The strategies themselves can't access the drivetrain directly, so we are responsible for
        // killing the motors when this command is paused.
        m_drive.stop();

        // TODO: Try to remove this. It should be unnecessary as this statement is also present in
        // {@link #initialize}.
        m_strategy.ifPresent(DriveStrategy::reset);
    }
}