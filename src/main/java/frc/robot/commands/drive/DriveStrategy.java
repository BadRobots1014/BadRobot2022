package frc.robot.commands.drive;

/**
 * Interface that defines strategies for driving control.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public interface DriveStrategy {

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

    /**
     * Sets the {@link DriveStrategyContext} use with the {@link DriveStrategy}.
     * This can be useful for changing the state of the {@link DriveStrategyContext}
     * from within a {@link DriveStrategy}.
     * 
     * @param context The {@link DriveStrategyContext} associated with this
     *                {@link DriveStrategy}.
     * @author Victor Chen <victorc.1@outlook.com>
     */
    public void setContext(DriveStrategyContext context);

}
