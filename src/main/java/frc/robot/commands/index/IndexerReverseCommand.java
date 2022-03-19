package frc.robot.commands.index;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.indexer.IndexerSubsystem.State;

public class IndexerReverseCommand extends CommandBase {

    private final IndexerSubsystem m_subsystem;
    
    public IndexerReverseCommand(IndexerSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_subsystem.setState(m_subsystem.getState(State.MANUAL));
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_subsystem.runLower(-1);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.stopLower();
        m_subsystem.rectifyState();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
