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

    private final DriveStrategy m_arcadeStrategy;
    private final DriveStrategy m_driveStraightStrategy;

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

    @Override
    public void execute() {
        m_arcadeStrategy.execute(m_xSource.get(), m_ySource.get());
    }

    @Override
    public void end(boolean interrupted) {

    }
}
