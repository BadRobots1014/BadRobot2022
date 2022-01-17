package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrainSubsystem;

public class TeleopDriveCommand extends CommandBase {
    
    private final DriveTrainSubsystem m_subsystem;
    private final XboxController m_xboxController;

    public TeleopDriveCommand(DriveTrainSubsystem subsystem, XboxController controller){
        
        m_subsystem = subsystem;
        m_xboxController = controller;

        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        
        m_subsystem.tankDrive(m_xboxController.getLeftY(), m_xboxController.getRightY());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
    
}