package frc.robot.subsystems.gatherer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Constants.GathererConstants;
import frc.robot.state.StatefulSubsystem;

/**
 * Represents the gatherer subsystem and exposes methods to control it.
 * 
 * A {@code GathererSubsystem} uses an internal state machine to control what
 * the gatherer is doing at all times.
 * 
 * {@code this.state} stores a reference to the current state. State-specific
 * logic is implemented in {@code GathererState} classes. The
 * {@code requestEngage} and {@code requestDisengage} methods are used to
 * retract and extend the gatherer automagically.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Benjamin Gluck
 */
public class GathererSubsystem extends StatefulSubsystem<GathererState> {

    /**
     * {@code GathererState}s that can be requested from a {@code GathererSubsystem}
     * using the {@code getState} method.
     */
    public static enum State {
        RETRACTED,
        EXTENDING,
        EXTENDED,
        RETRACTING
    }

    /*
     * Private members --------------------------------------------------------
     */

    private final TalonSRX armMotor;
    private final TalonSRX collectorMotor;

    private final GathererState retractedState;
    private final GathererState extendingState;
    private final GathererState extendedState;
    private final GathererState retractingState;

    /*
     * Shuffleboard -----------------------------------------------------------
     */

    private final ShuffleboardTab shuffleTab;

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public GathererSubsystem() {

        this.retractedState = new GathererStateRetracted(this);
        this.extendingState = new GathererStateExtending(this);
        this.extendedState = new GathererStateExtended(this);
        this.retractingState = new GathererStateRetracting(this);

        this.state = this.retractedState;

        this.armMotor = new TalonSRX(GathererConstants.kGathererSpeedController);
        this.armMotor.setNeutralMode(NeutralMode.Brake);
        this.collectorMotor = new TalonSRX(GathererConstants.kCollectorSpeedController);

        this.shuffleTab = Shuffleboard.getTab("Gatherer");
        this.shuffleTab.addString("State", () -> this.state.getClass().toString());
    }

    /*
     * Exposed control methods ------------------------------------------------
     */

    public void extendArm() {
        this.armMotor.set(ControlMode.PercentOutput, -0.20);
    }

    public void retractArm() {
        this.armMotor.set(ControlMode.PercentOutput, 0.20);
    }

    public void stopArmMotor() {
        this.armMotor.set(ControlMode.PercentOutput, 0);
    }

    public void runCollector() {
        this.collectorMotor.set(ControlMode.PercentOutput, 1);
    }

    public void stopCollector() {
        this.collectorMotor.set(ControlMode.PercentOutput, 0);
    }

    public boolean isArmAtRetractLimit() {
        /*
         * This is supposed to read from the forward limit switch. The limit switches
         * are hooked up backwards on the robot.
         */
        return this.armMotor.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public boolean isArmAtExtendLimit() {
        /*
         * This is supposed to read from the reverse limit switch. The limit switches
         * are hooked up backwards on the robot.
         */
        return this.armMotor.getSensorCollection().isRevLimitSwitchClosed();
    }

    /**
     * Request the gatherer to engage. The internal state machine of {@code this}
     * will handle all further logic.
     */
    public void requestEngage() {
        this.state.onRequestEngage();
    }

    /**
     * Request the gatherer to disengage. The internal state machine of {@code this}
     * will handle all further logic.
     */
    public void requestDisengage() {
        this.state.onRequestDisengage();
    }

    /*
     * State methods ----------------------------------------------------------
     */

    /**
     * Gets the corresponding {@code GathererState} object used with {@code this}.
     * 
     * @param state the type of state object to retrieve
     * @return the corresponding {@code GathererState} object
     */
    public GathererState getState(State state) {
        switch (state) {
            case RETRACTED:
                return this.retractedState;
            case EXTENDING:
                return this.extendingState;
            case EXTENDED:
                return this.extendedState;
            case RETRACTING:
                return this.retractingState;
        }

        // This line to make the Java compiler happy.
        return null;
    }

}
