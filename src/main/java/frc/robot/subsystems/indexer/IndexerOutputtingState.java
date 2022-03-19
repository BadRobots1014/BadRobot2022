package frc.robot.subsystems.indexer;

import frc.robot.subsystems.indexer.IndexerSubsystem.State;

public class IndexerOutputtingState implements IndexerState {

    private final IndexerSubsystem subsystem;

    public IndexerOutputtingState(IndexerSubsystem subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public void initialize() {
        this.subsystem.stopUpper();
        this.subsystem.stopLower();
    }

    @Override
    public void execute() {
        if (this.subsystem.getUpper()) {
            this.subsystem.runUpper();
        } else {
            this.subsystem.setState(this.subsystem.getState(State.EMPTY));
        }
    }

    @Override
    public void end() {
        this.subsystem.stopUpper();
    }
    
}
