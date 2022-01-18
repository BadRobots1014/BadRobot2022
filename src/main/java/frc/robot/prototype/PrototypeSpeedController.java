package frc.robot.prototype;

/**
 * {@code PrototypeSpeedController} defines an interface for wrapper classes for
 * speed controllers used for prototyping.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public interface PrototypeSpeedController {

    /**
     * Set the speed of a speed controller used for prototyping.
     * 
     * @param speed The speed to drive the speed controller at, a value [-1, 1]
     */
    void set(double speed);

}
