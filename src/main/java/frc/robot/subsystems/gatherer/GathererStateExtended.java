package frc.robot.subsystems.gatherer;

import frc.robot.subsystems.gatherer.GathererSubsystem.State;

public class GathererStateExtended implements GathererState {

    private GathererSubsystem subsystem;

    public GathererStateExtended(GathererSubsystem subsystem) {
        this.subsystem = subsystem;        
    }

    @Override
    public void initialize() {
        // Do nothing.
    }

    @Override
    public void execute() {
        this.subsystem.extendArm();
        this.subsystem.runCollector();
    }

    @Override
    public void end() {
        // Do nothing.
    }

    @Override
    public void onRequestEngage() {
        // Do nothing.
    }

    @Override
    public void onRequestDisengage() {
        this.subsystem.setState(this.subsystem.getState(State.RETRACTING));
    }
    
}
