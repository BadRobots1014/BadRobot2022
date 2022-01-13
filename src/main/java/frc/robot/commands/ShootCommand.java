package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;


public class ShootCommand extends CommandBase {
    
    private final ShooterSubsystem m_subsystem;
    
    public ShootCommand(ShooterSubsystem subsystem) {
        m_subsystem = subsystem;

        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_subsystem.run();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_subsystem.run();
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
