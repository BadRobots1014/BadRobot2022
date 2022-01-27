package frc.robot.commands.drive;

/**
 * Interface that defines strategies for driving control.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public interface DriveStrategy {

    public String getName();

    /**
     * Resets the strategy to an initial state.
     */
    public void reset();

    /**
     * Does the thing.
     * 
     * @param x The x-axis reading from the joystick
     * @param y The y-axis reading from the joystick
     * @author Victor Chen <victorc.1@outlook.com>
     * @author Will Blankemeyer
     */
    public void execute(double x, double y);

}
