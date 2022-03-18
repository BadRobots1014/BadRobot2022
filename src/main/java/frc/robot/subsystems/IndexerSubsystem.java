package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Represents the indexer subsystem and exposes methods to control it.
 * 
 * @author Leo Canales
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class IndexerSubsystem extends SubsystemBase {
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

    /*
     * Constructor ------------------------------------------------------------
     */

    public IndexerSubsystem() {
        this.lowerMotor = new TalonSRX(LOWER_MOTOR_PORT);
        this.upperMotor = new TalonSRX(UPPER_MOTOR_PORT);

        this.lowerSensor = new DigitalInput(LOWER_SENSOR_PORT);
        this.upperSensor = new DigitalInput(UPPER_SENSOR_PORT);
    }

    /*
     * Exposed control methods ------------------------------------------------
     */

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

}
