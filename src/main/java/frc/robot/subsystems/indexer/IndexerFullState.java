package frc.robot.subsystems.indexer;

public class IndexerFullState implements IndexerState {

    private final IndexerSubsystem subsystem;

    public IndexerFullState(IndexerSubsystem subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public void initialize() {
        this.subsystem.stopUpper();
        this.subsystem.stopLower();
    }

    @Override
    public void execute() {
        // Nothing here.
    }

    @Override
    public void end() {
        // Nothing here.
    }
    
}
