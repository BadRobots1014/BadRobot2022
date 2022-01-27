package frc.robot.commands;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Optional;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;

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
    private final GyroSubsystem m_gyro;
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
        m_gyro = gyro;
        m_xSource = xSource;
        m_ySource = ySource;
        m_throttleSource = throttleSource;

        m_arcadeStrategy = new ArcadeDriveStrategy(m_drive);
        m_driveStraightStrategy = new DriveStraightStrategy(m_drive, m_gyro);
        m_pivotTurnStrategy = new PivotTurnStrategy(m_drive);
        m_anchorStrategy = new AnchorStrategy(m_drive, m_gyro);

        /**
         * Initialize teleop. driving without a current strategy.
         *
         * This will display in the "Teleop. Drive: Current Strategy" Shuffleboard widget as "None"
         * until a strategy is selected.
         */
        m_strategy = Optional.empty();

        m_tab = Shuffleboard.getTab("Teleop. Drive");
        m_tab.addString(
            "Current Strategy",
            () -> m_strategy.map(DriveStrategy::getName).orElse("None")
        );

        addRequirements(drive, gyro);
    }

    /*
     * Command methods --------------------------------------------------------
     */

    @Override
    public void execute() {
        final double x = m_xSource.get();
        final double y = m_ySource.get();

        /**
         * Note: Strategy selection must use the *original* position of the joystick for deadzone
         * detection. Do not scale 'x' and 'y' by the throttle power yet!
         */
        final DriveStrategy nextStrategy = this.getNextDriveStrategy(x, y);

        m_strategy.ifPresentOrElse(
            (current) -> {
                if (nextStrategy != current) {
                    /**
                     * If a current strategy exists, and the current strategy is *not* the new
                     * strategy, then we must update it.
                     */
                    current = nextStrategy;

                    /**
                     * The new strategy was not used last, so its state is likely stale. We will
                     * reset it to a normalized state.
                     */
                    nextStrategy.reset();
                }
            },
            () -> {
                /**
                 * No current strategy exists, so we will set it now.
                 */
                m_strategy = Optional.of(nextStrategy);
            }
        );

        final double throttle = m_throttleSource.get();

        /*
         * Execute the strategy set in {@link this::m_strategy}.
         *
         * This scales 'x' and 'y' by the throttle power.
         */
        nextStrategy.execute(throttle * x, throttle * y);
    }

    public DriveStrategy getNextDriveStrategy(final double x, final double y) {
        /**
         * 'x' and 'y' represent a Cartesian point in the plane of possible joystick positions.
         * There are certain regions in this plane that correspond to certain drive strategies.
         *
         * The simplest of these, and which also serves as a general joystick deadzone, is the
         * anchor region. This region is a circle of radius
         * {@link ControllerConstants.kDeadzoneRadius} centered at the origin. If the joystick
         * position is within the anchor region, then the robot *must* anchor---this region takes
         * precedence over all others.
         *
         * The regions with the second highest precedence are those for driving straight and
         * pivot-turning. These regions are narrow isosceles triangles symmetric along the X and Y
         * axes and which are reflected across, and have an apex at, the origin. The base lengths of
         * these triangles are defined by {@link DriveTrainConstants.kDriveStraightRegionBaseLength}
         * and {@link DriveTrainConstants.kPivotTurnRegionBaseLength}.
         *
         * Finally, if the joystick position is not within any of the previous regions, then the
         * robot defaults to arcade-driving.
         */

        /**
         * Anchor region:
         *
         * This region is simply a circle, so if the distance of the joystick position from the
         * origin is less than or equal to this region's radius, then the robot anchors.
         */
        final double originDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        if (originDistance <= ControllerConstants.kDeadzoneRadius) {
            return m_anchorStrategy;
        }

        final double absX = Math.abs(x);
        final double absY = Math.abs(y);

        /**
         * Pivot-turn region:
         *
         * This region is an isosceles triangle symmetric about the X-axis, with an apex at the
         * origin, and reflected across the origin as well. However, for the purpose of detection,
         * we only need to consider quadrant 1.
         *
         * In quadrant 1, this region is simply a line with a Y-intercept at the origin and a slope
         * of half the base length (divided by the maximum X value, 1). If we imagine this line as a
         * linear function, then each output represents the maximum possible Y-coordinate of the
         * joystick to be within the pivot-turn region for a given X-coordinate. Therefore, pivot-
         * turn detection is fairly straightforward: find this maximum Y-coordinate by applying the
         * joystick X-coordinate to this linear function (i.e., multiplying the half of the base
         * length by X), and if the actual joystick Y-coordinate is equal to or less than this
         * maximum, then the robot pivot-turns.
         */

        final Function<Double, Double> pivotTurnLinearFunction = (input) -> {
            final double maximumOutput = ControllerConstants.kPivotTurnRegionBaseLength * 0.5;
            return maximumOutput * input;
        };

        if (absY <= pivotTurnLinearFunction.apply(absX)) {
            return m_pivotTurnStrategy;
        }

        /**
         * Drive straight:
         *
         * This follows the same principle as that of the pivot-turn region *except* that the drive-
         * straight linear function is *the inverse of* the pivot-turn linear function, so X and Y
         * must be swapped.
         */

        final Function<Double, Double> driveStraightLinearFunction = (input) -> {
            final double maximumOutput = ControllerConstants.kDriveStraightRegionBaseLength * 0.5;
            return maximumOutput * input;
        };

        if (absX <= driveStraightLinearFunction.apply(absY)) {
            return m_driveStraightStrategy;
        }

        /**
         * Arcade-drive:
         *
         * Default to arcade-driving.
         */
        return m_arcadeStrategy;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
