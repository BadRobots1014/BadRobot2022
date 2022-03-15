package frc.robot.subsystems.gatherer;

import frc.robot.subsystems.gatherer.GathererSubsystem.State;

public class GathererStateRetracting implements GathererState {

    private GathererSubsystem subsystem;

    public GathererStateRetracting(GathererSubsystem subsystem) {
        this.subsystem = subsystem;        
    }

    @Override
    public void initialize() {
        // Do nothing.
    }

    @Override
    public void execute() {
        if (this.subsystem.isArmAtRetractLimit()) {
            this.subsystem.setState(this.subsystem.getState(State.RETRACTED));
        } else {
            this.subsystem.retractArm();
            this.subsystem.runCollector();
        }
    }

    @Override
    public void end() {
        // Do nothing.
    }

    @Override
    public void onRequestEngage() {
        this.subsystem.setState(this.subsystem.getState(State.EXTENDING));
    }

    @Override
    public void onRequestDisengage() {
        // Do nothing.
    }
    
}
