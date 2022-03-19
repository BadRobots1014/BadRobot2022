package frc.robot.subsystems.gatherer;

import frc.robot.subsystems.gatherer.GathererSubsystem.State;

public class GathererStateRetracted implements GathererState {

    private GathererSubsystem subsystem;

    public GathererStateRetracted(GathererSubsystem subsystem) {
        this.subsystem = subsystem;        
    }

    private void stopMotors() {
        if (this.subsystem.isArmAtRetractLimit()) {
            this.subsystem.stopArmMotor();
            this.subsystem.stopCollector();
        } else {
            this.subsystem.retractArm();
        }
    }

    @Override
    public void initialize() {
        stopMotors();
    }

    @Override
    public void execute() {
        // Do nothing.
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
