package frc.robot.subsystems.indexer;

public class IndexerManualState implements IndexerState {

    private final IndexerSubsystem subsystem;

    public IndexerManualState(IndexerSubsystem subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public void initialize() {
        this.subsystem.stop();
    }

    @Override
    public void execute() {
        // Do nothing.
    }

    @Override
    public void end() {
        // Nothing here.
    }
    
}
