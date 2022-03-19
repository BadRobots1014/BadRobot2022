package frc.robot.state;

/**
 * Specification of a state interface to be used with subsystems.
 * @author Victor Chen <victorc.1@outlook.com>
 */
public interface State {
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
}
