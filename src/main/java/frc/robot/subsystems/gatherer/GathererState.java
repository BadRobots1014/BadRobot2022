package frc.robot.subsystems.gatherer;

/**
 * Specification of a state class to be used with {@link GathererSubsystem}.
 * @author Victor Chen <victorc.1@outlook.com>
 */
public interface GathererState {
    
    /**
     * This method is executed when the state is first entered.
     */
    public void initialize();

    /**
     * This method is executed periodically while in this state.
     */
    public void execute();

    /**
     * This method is executed before exiting the state.
     */
    public void end();

    /**
     * This method is executed when the driver requests the gatherer to be engaged.  
     */
    public void onRequestEngage();

    /**
     * This method is executed when the driver requests the gatherer to be disengaged.  
     */
    public void onRequestDisengage();

}
