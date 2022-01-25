package frc.robot.commands.drive;

/**
 * A {@code DriveStrategyContext} is a stateful object that uses one or more
 * {@link DriveStrategy} objects to determine its behavior.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public interface DriveStrategyContext {

    /**
     * Sets the {@link DriveStrategy} that the {@link DriveStrategyContext} will use
     * to perform state-specific behavior.
     * 
     * @param strategy The strategy to use
     * @author Victor Chen <victorc.1@outlook.com>
     */
    public void setStrategy(DriveStrategy strategy);

    /**
     * Returns the next {@link DriveStrategy} to use.
     * 
     * @return The next {@link DriveStrategy} to use.
     * @author Victor Chen <victorc.1@outlook.com>
     */
    public DriveStrategy getNextDriveStrategy();

}
