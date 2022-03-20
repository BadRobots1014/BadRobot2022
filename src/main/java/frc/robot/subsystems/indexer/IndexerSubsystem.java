package frc.robot.subsystems.indexer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.state.StatefulSubsystem;

/**
 * Represents the indexer subsystem and exposes methods to control it.
 * 
 * @author Leo Canales
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class IndexerSubsystem extends StatefulSubsystem<IndexerState> {

    /**
     * {@link IndexerState}s that can be requested from a {@link IndexerSubsystem}
     * using the {@link #getState} method.
     */
    public static enum State {
        EMPTY,
        INTAKING,
        FULL,
        OUTPUTTING,
        MANUAL
    }

    /*
     * Private constants ------------------------------------------------------
     */

    private static final int LOWER_MOTOR_PORT = 29;
    private static final int UPPER_MOTOR_PORT = 15;

    private static final int LOWER_SENSOR_PORT = 0;
    private static final int UPPER_SENSOR_PORT = 1;

    /**
     * The maximum percent output of the lower motor.
     */
    private static final double LOWER_MAX_SPEED = 1;

    /**
     * The maximum percent output of the upper motor.
     */
    private static final double UPPER_MAX_SPEED = 1;

    /*
     * Private members --------------------------------------------------------
     */

    private final TalonSRX lowerMotor;
    private final TalonSRX upperMotor;

    private final DigitalInput lowerSensor;
    private final DigitalInput upperSensor;

    private final IndexerState emptyState;
    private final IndexerState intakingState;
    private final IndexerState fullState;
    private final IndexerState outputtingState;
    private final IndexerState manualState;

    /*
     * Shuffleboard -----------------------------------------------------------
     */

    private final ShuffleboardTab shuffleTab;

    /*
     * Constructor ------------------------------------------------------------
     */

    public IndexerSubsystem() {
        this.lowerMotor = new TalonSRX(LOWER_MOTOR_PORT);
        this.upperMotor = new TalonSRX(UPPER_MOTOR_PORT);

        this.lowerSensor = new DigitalInput(LOWER_SENSOR_PORT);
        this.upperSensor = new DigitalInput(UPPER_SENSOR_PORT);

        this.emptyState = new IndexerEmptyState(this);
        this.intakingState = new IndexerIntakingState(this);
        this.fullState = new IndexerFullState(this);
        this.outputtingState = new IndexerOutputtingState(this);
        this.manualState = new IndexerManualState(this);

        this.state = this.emptyState;
        this.rectifyState();

        this.shuffleTab = Shuffleboard.getTab("Indexer");
        this.shuffleTab.addString("State", () -> this.state.getClass().toString());
        this.shuffleTab.addBoolean("Lower Sensor", this::getLower);
        this.shuffleTab.addBoolean("Upper Sensor", this::getUpper);
    }

    /*
     * Exposed control methods ------------------------------------------------
     */

    /**
     * Runs the lower motor at the default power.
     */
    public void runLower() {
        this.lowerMotor.set(ControlMode.PercentOutput, LOWER_MAX_SPEED);
        System.out.println("Running lower motor");
    }

    /**
     * Runs the lower motor.
     * 
     * @param power the percentage power to run at, positive is inwards
     * @requires -1 <= power <= 1
     */
    public void runLower(double power) {
        this.lowerMotor.set(ControlMode.PercentOutput, LOWER_MAX_SPEED * power);
        System.out.println("Running lower motor");
    }

    /**
     * Stops the lower motor.
     */
    public void stopLower() {
        this.lowerMotor.set(ControlMode.PercentOutput, 0);
        System.out.println("Stopped lower motor");
    }

    /**
     * Runs the upper motor at the default power.
     */
    public void runUpper() {
        this.upperMotor.set(ControlMode.PercentOutput, -UPPER_MAX_SPEED);
        System.out.println("Running upper motor");
    }

    /**
     * Runs the upper motor.
     * 
     * @param power the percentage power to run at, positive is outwards toward the
     *              shooter.
     * @requires -1 <= power <= 1
     */
    public void runUpper(double power) {
        this.upperMotor.set(ControlMode.PercentOutput, -UPPER_MAX_SPEED * power);
        System.out.println("Running upper motor");
    }

    /**
     * Stops the upper motor.
     */
    public void stopUpper() {
        this.upperMotor.set(ControlMode.PercentOutput, 0);
        System.out.println("Stopped upper motor");
    }

    /**
     * Returns the state of the lower sensor.
     * 
     * @return [the sensor beam is obstructed]
     */
    public boolean getLower() {
        return this.lowerSensor.get();
    }

    /**
     * Returns the state of the upper sensor.
     * 
     * @return [the sensor beam is obstructed]
     */
    public boolean getUpper() {
        return this.upperSensor.get();
    }

    /*
     * State methods ----------------------------------------------------------
     */

    /**
     * Gets the corresponding {@link IndexerState} object used with {@code this}.
     * 
     * @param state the type of state object to retrieve
     * @return the corresponding {@link IndexerState} object
     */
    public IndexerState getState(State state) {
        switch (state) {
            case EMPTY:
                return this.emptyState;
            case INTAKING:
                return this.intakingState;
            case FULL:
                return this.fullState;
            case OUTPUTTING:
                return this.outputtingState;
            case MANUAL:
                return this.manualState;
            default:
                // This line to make the Java compiler happy.
                return null;
        }
    }

    /**
     * Resets {@code this.state} based on sensor readings.
     */
    public void rectifyState() {
        if (this.getUpper()) {
            this.setState(this.fullState);
        } else {
            this.setState(this.emptyState);
        }
    }

}
