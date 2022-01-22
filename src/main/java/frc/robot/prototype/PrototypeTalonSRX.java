package frc.robot.prototype;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Wrapper class for TalonSRX speed controllers used for prototyping.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class PrototypeTalonSRX implements PrototypeSpeedController {

    private TalonSRX controller;

    /**
     * Creates a {@code PrototypeTalonSRX} to wrap a TalonSRX speed controller for
     * prototyping use
     * 
     * @param controller The TalonSRX speed controller object
     */
    public PrototypeTalonSRX(TalonSRX controller) {
        this.controller = controller;
    }

    @Override
    public void set(double speed) {
        controller.set(TalonSRXControlMode.PercentOutput, speed);
    }

}
