package frc.robot.commands;

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

        m_strategy = Optional.empty();

        m_tab = Shuffleboard.getTab("Teleop. Drive");
        m_tab.addString("Current Strategy", () -> m_strategy.map((strategy) -> strategy.getName()).orElse("None"));

        addRequirements(drive, gyro);
    }

    /*
     * Command methods --------------------------------------------------------
     */

    @Override
    public void execute() {
        final double throttle = m_throttleSource.get();
        final double x = m_xSource.get() * throttle;
        final double y = m_ySource.get() * throttle;

        final DriveStrategy nextStrategy = this.getNextDriveStrategy(x, y, throttle);

        m_strategy.ifPresent((current) -> {
            if (nextStrategy != current) {
                current = nextStrategy;
                current.reset();
            }
        });
        m_strategy = Optional.of(nextStrategy);

        /*
        * Execute the strategy set in m_strategy.
        */
        nextStrategy.execute(x, y);
    }

    public DriveStrategy getNextDriveStrategy(double x, double y, double throttle) {
        final double anchorDeadzoneRadius = throttle * ControllerConstants.kAnchorDeadzoneRadius;
        final double driveStraightDeadzoneBaseLength = throttle * ControllerConstants.kDriveStraightDeadzoneBaseLength;
        final double pivotTurnDeadzoneBaseLength = throttle * ControllerConstants.kPivotTurnDeadzoneBaseLength;

        // 'x' and 'y' represent a Cartesian point. If that point is within a small circular deadzone
        // centered at the origin, then we anchor.
        final double originDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        if (originDistance <= anchorDeadzoneRadius) {
            return m_anchorStrategy;
        }

        // There are also deadzones for strictly driving straight and strictly pivot-turning. These
        // deadzones are, geometrically, isosceles triangles that are symmetric along the X and Y axes
        // with an apex at the origin. Thus, the legs of each triangle can be modelled with a linear
        // function. If the output of that function is ever less than the length of the associated
        // triangle's base, then the input to that function is within a deadzone.

        // TODO: Explain this better; make better variable names.

        final double driveStraightAxisDistance = Math.abs(x);
        final double pivotTurnAxisDistance = Math.abs(y);

        final double driveStraightDeadzone = driveStraightDeadzoneBaseLength * pivotTurnAxisDistance;
        if (driveStraightAxisDistance <= driveStraightDeadzone) {
            return m_driveStraightStrategy;
        }

        final double pivotTurnDeadzone = pivotTurnDeadzoneBaseLength * driveStraightAxisDistance;
        if (pivotTurnAxisDistance <= pivotTurnDeadzone) {
            return m_pivotTurnStrategy;
        }

        return m_arcadeStrategy;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
