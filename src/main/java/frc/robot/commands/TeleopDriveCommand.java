package frc.robot.commands;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.drive.ArcadeDriveStrategy;
import frc.robot.commands.drive.DriveStraightStrategy;
import frc.robot.commands.drive.DriveStrategy;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

public class TeleopDriveCommand extends CommandBase {
    private final DriveTrainSubsystem m_drive;
    private final GyroSubsystem m_gyro;
    private final Supplier<Double> m_xSource;
    private final Supplier<Double> m_ySource;

    public final DriveStrategy m_arcadeStrategy;
    public final DriveStrategy m_driveStraightStrategy;

    private DriveStrategy m_strategy;

    public TeleopDriveCommand(DriveTrainSubsystem drive, GyroSubsystem gyro, Supplier<Double> xSource,
            Supplier<Double> ySource) {

        m_drive = drive;
        m_gyro = gyro;
        m_xSource = xSource;
        m_ySource = ySource;

        m_arcadeStrategy = new ArcadeDriveStrategy(m_drive);
        m_driveStraightStrategy = new DriveStraightStrategy(m_drive, m_gyro);

        addRequirements(drive);
        addRequirements(gyro);
    }

    /**
     * Sets the strategy that will be used to interpret controls to the given
     * {@code strategy}.
     * 
     * @param strategy The new strategy to use
     * @author Victor Chen <victorc.1@outlook.com
     */
    public void setStrategy(DriveStrategy strategy) {
        /*
         * When we change strategies, we want to reset the new strategy object to an
         * initial state.
         */
        strategy.reset();
        this.m_strategy = strategy;
    }

    @Override
    public void execute() {
        /*
         * Execute the strategy set in m_strategy.
         */
        m_strategy.execute(m_xSource.get(), m_ySource.get());
    }

    @Override
    public void end(boolean interrupted) {

    }
}
