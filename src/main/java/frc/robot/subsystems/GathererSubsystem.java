package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.GathererConstants;
import frc.robot.subsystems.gatherer.GathererState;
import frc.robot.subsystems.gatherer.GathererStateExtended;
import frc.robot.subsystems.gatherer.GathererStateExtending;
import frc.robot.subsystems.gatherer.GathererStateRetracted;
import frc.robot.subsystems.gatherer.GathererStateRetracting;

/**
 * Represents the gatherer subsystem and exposes methods to control it.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Benjamin Gluck
 */
public class GathererSubsystem extends SubsystemBase {

    public static enum State {
        RETRACTED,
        EXTENDING,
        EXTENDED,
        RETRACTING
    }

    private final GathererState retractedState;
    private final GathererState extendingState;
    private final GathererState extendedState;
    private final GathererState retractingState;

    private GathererState state;

    private final TalonSRX armMotor;
    private final TalonSRX collectorMotor;

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
        this.collectorMotor = new TalonSRX(GathererConstants.kCollectorSpeedController);
    }

    /*
     * Helper methods ---------------------------------------------------------
     */

    /**
     * Handles state changes by calling prev.end() and next.initialize().
     * 
     * @param prev the previous state object
     * @param next the next state object
     */
    private void onStateChange(GathererState prev, GathererState next) {
        prev.end();
        next.initialize();
    }

    /*
     * Direct hardware control methods ----------------------------------------
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
        return this.armMotor.getSensorCollection().isRevLimitSwitchClosed();
    }

    public boolean isArmAtExtendLimit() {
        return this.armMotor.getSensorCollection().isFwdLimitSwitchClosed();
    }

    /*
     * State setter and getter ------------------------------------------------
     */

    public GathererState getCurrentState() {
        return this.state;
    }

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

    public void setState(GathererState state) {
        this.onStateChange(this.state, state);
        this.state = state;
    }

    /*
     * Request methods --------------------------------------------------------
     */

    public void requestEngage() {
        this.state.onRequestEngage();
    }

    public void requestDisengage() {
        this.state.onRequestDisengage();
    }

    /*
     * SubsystemBase methods --------------------------------------------------
     */

    @Override
    public void periodic() {
        this.state.execute();
    }

}
