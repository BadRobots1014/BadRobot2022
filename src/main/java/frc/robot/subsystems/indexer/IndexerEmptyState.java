package frc.robot.subsystems.indexer;

import frc.robot.subsystems.indexer.IndexerSubsystem.State;

public class IndexerEmptyState implements IndexerState {

    private final IndexerSubsystem subsystem;

    public IndexerEmptyState(IndexerSubsystem subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public void initialize() {
        this.subsystem.stop();
    }

    @Override
    public void execute() {
        if (this.subsystem.getLower()) {
            this.subsystem.setState(this.subsystem.getState(State.INTAKING));
        }
    }

    @Override
    public void end() {
        // Nothing here.
    }
    
}
