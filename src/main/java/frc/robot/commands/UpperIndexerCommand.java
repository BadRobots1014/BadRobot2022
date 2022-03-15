package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerSubsystem;

public class UpperIndexerCommand extends CommandBase {

    private final IndexerSubsystem m_subsystem;
    
    public UpperIndexerCommand(IndexerSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_subsystem.runUpperMotor(1);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.stopUpperMotor();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
