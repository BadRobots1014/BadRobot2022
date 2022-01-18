package frc.robot.prototype;

import com.revrobotics.CANSparkMax;

/**
 * Wrapper class for CANSparkMax speed controllers used for prototyping.
 * 
 * @author Victor Chen <victorc.1@outlook.com
 */
public class PrototypeCANSparkMax implements PrototypeSpeedController {

    private CANSparkMax controller;

    /**
     * Creates a {@code PrototypeCANSparkMax} to wrap a CANSparkMax speed controller
     * for prototyping use.
     * 
     * @param controller The CANSparkMax speed controller object
     */
    public PrototypeCANSparkMax(CANSparkMax controller) {
        this.controller = controller;
    }

    @Override
    public void set(double speed) {
        controller.set(speed);
    }

}
