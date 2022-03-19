package frc.robot.subsystems.gatherer;

import frc.robot.state.State;

/**
 * Specification of a state class to be used with {@link GathererSubsystem}.
 * @author Victor Chen <victorc.1@outlook.com>
 */
public interface GathererState extends State {
    /**
     * This method is executed when the driver requests the gatherer to be engaged.  
     */
    public void onRequestEngage();

    /**
     * This method is executed when the driver requests the gatherer to be disengaged.  
     */
    public void onRequestDisengage();
}
