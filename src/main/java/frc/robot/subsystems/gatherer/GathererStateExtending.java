package frc.robot.subsystems.gatherer;

import frc.robot.subsystems.GathererSubsystem;
import frc.robot.subsystems.GathererSubsystem.State;

public class GathererStateExtending implements GathererState {

    private GathererSubsystem subsystem;

    public GathererStateExtending(GathererSubsystem subsystem) {
        this.subsystem = subsystem;        
    }

    @Override
    public void initialize() {
        // Do nothing.
    }

    @Override
    public void execute() {
        if (this.subsystem.isArmAtExtendLimit()) {
            this.subsystem.setState(this.subsystem.getState(State.EXTENDED));
        } else {
            this.subsystem.extendArm();
            this.subsystem.runCollector();
        }
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
