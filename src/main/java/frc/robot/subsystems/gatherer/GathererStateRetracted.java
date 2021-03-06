package frc.robot.subsystems.gatherer;

import frc.robot.subsystems.gatherer.GathererSubsystem.State;

public class GathererStateRetracted implements GathererState {

    private GathererSubsystem subsystem;

    public GathererStateRetracted(GathererSubsystem subsystem) {
        this.subsystem = subsystem;
    }

    private void stopMotors() {
        this.subsystem.stopArmMotor();
        this.subsystem.stopCollector();
    }

    @Override
    public void initialize() {
        stopMotors();
    }

    @Override
    public void execute() {
        if (this.subsystem.isArmAtRetractLimit()) {
            this.stopMotors();
        } else {
            this.subsystem.retractArm();
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
