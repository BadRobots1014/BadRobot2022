package frc.robot.state;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * A {@link StatefulSubsystem} is a subsystem which uses a state machine to
 * control its hardware.
 * 
 * @param <SubsystemState> the {@link State} specific to the subsystem
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public abstract class StatefulSubsystem<SubsystemState extends State> extends SubsystemBase {
    
    /*
     * Protected members ------------------------------------------------------
     */

    protected SubsystemState state;

    /*
     * Protected helper methods -----------------------------------------------
     */

    /**
     * Handles state changes by calling {@code prev.end()} and
     * {@code next.initialize()}.
     * 
     * @param prev the previous state object
     * @param next the next state object
     */
    protected void onStateChange(SubsystemState prev, SubsystemState next) {
        prev.end();
        next.initialize();
    }

    /*
     * State setter and getter ------------------------------------------------
     */

    /**
     * Sets {@code this.state} to {@code state}.
     * 
     * @param state the new state
     */
    public void setState(SubsystemState state) {
        this.onStateChange(this.state, state);
        this.state = state;
    }

    /*
     * SubsystemBase methods --------------------------------------------------
     */

    /**
     * The default implementation for {@link #periodic} calls
     * {@code this.state::execute}.
     */
    @Override
    public void periodic() {
        this.state.execute();
    }
    
}
