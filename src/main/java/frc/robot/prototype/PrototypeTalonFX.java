package frc.robot.prototype;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

/**
 * Wrapper class for TalonFX speed controllers used for prototyping.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class PrototypeTalonFX implements PrototypeSpeedController {

    private TalonFX controller;

    /**
     * Creates a {@code PrototypeTalonFX} to wrap a TalonFX speed controller for
     * prototyping use
     * 
     * @param controller The TalonFX speed controller object
     */
    public PrototypeTalonFX(TalonFX controller) {
        this.controller = controller;
    }

    @Override
    public void set(double speed) {
        controller.set(TalonFXControlMode.PercentOutput, speed);
    }

}
