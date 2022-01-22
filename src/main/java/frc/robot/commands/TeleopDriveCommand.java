package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrainSubsystem;

public class TeleopDriveCommand extends CommandBase {
    
    private final DriveTrainSubsystem m_drive;
    private final Supplier<Double> m_leftPower;
    private final Supplier<Double> m_rightPower;

    public TeleopDriveCommand(
        DriveTrainSubsystem drive,
        Supplier<Double> leftPower,
        Supplier<Double> rightPower
    ) {
        m_drive = drive;
        m_leftPower = leftPower;
        m_rightPower = rightPower;

        addRequirements(m_drive);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_drive.tankDrive(m_leftPower.get(), m_rightPower.get());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_drive.stop();
    }
}
