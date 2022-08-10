package frc.robot.subsystems;
import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IndexerConstants;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class IndexerSubsystem extends SubsystemBase {
    private final TalonSRX m_lowerMotor = new TalonSRX(IndexerConstants.kLowerIndexerSpeedController);
    private final TalonSRX m_upperMotor = new TalonSRX(IndexerConstants.kUpperIndexerSpeedController);

    private final DigitalInput m_lowerSensor = new DigitalInput(IndexerConstants.kLowerIndexerSensor);
    private final DigitalInput m_upperSensor = new DigitalInput(IndexerConstants.KUpperIndexerSensor);
    private final BooleanSupplier m_lowerSensorSupplier = () -> m_lowerSensor.get();
    private final BooleanSupplier m_upperSensorSupplier = () -> m_upperSensor.get();

    private String m_state;
    public boolean m_shotRequested;
    private Timer m_timer;

    private final ShuffleboardTab tab;

    public IndexerSubsystem() {
        m_state = "empty";
        checkState();
        m_shotRequested = false;
        m_timer = new Timer();

        this.tab = Shuffleboard.getTab("Indexer");
        this.tab.addBoolean("Lower Sensor", m_lowerSensorSupplier);
        this.tab.addBoolean("Upper Sensor", m_upperSensorSupplier);
        this.tab.addString("State", () -> m_state);
    }

    public void periodic() {
        checkState();
        actOnState();
    }

    public void runLowerMotor(double power) {
        m_lowerMotor.set(ControlMode.PercentOutput, IndexerConstants.kIndexerMaxSpeed * power);
        System.out.println("Running lower motor");
    }

    public void stopLowerMotor() {
        m_lowerMotor.set(ControlMode.PercentOutput, 0);
        System.out.println("Stopped lower motor");
    }

    public void runUpperMotor(double power) {
        m_upperMotor.set(ControlMode.PercentOutput, -IndexerConstants.kIndexerMaxSpeed * power);
        System.out.println("Running upper motor");
    }

    public void stopUpperMotor() {
        m_upperMotor.set(ControlMode.PercentOutput, 0);
        System.out.println("Stopped upper motor");
    }

    public void stopAll() {
        m_upperMotor.set(ControlMode.PercentOutput, 0);
        System.out.println("Stopped upper motor");
        m_lowerMotor.set(ControlMode.PercentOutput, 0);
        System.out.println("Stopped lower motor");
    }

    public boolean lowerSensorOn() {
        return m_lowerSensor.get();
    }

    public boolean upperSensorOn() {
        return m_upperSensor.get();
    }

    public void checkState() { //Moves through the natural state flow
        switch(m_state) {
            case "empty":
                if (lowerSensorOn()) {
                    m_state = "emptyIntaking";
                }
            break;
            case "emptyIntaking":
                if (upperSensorOn()) {
                    m_state = "topLoaded";
                }
                //TODO Stop if the ball rolls out? (Not for now)
            break;
            case "toploaded":
                if (!upperSensorOn()) {
                    m_state = "empty";
                }
                else if (lowerSensorOn()) {
                    m_state = "topLoadedIntaking";
                    m_timer.stop();
                    m_timer.reset();
                    m_timer.start();
                }
            break;
            case "topLoadedIntaking":
                if (!lowerSensorOn()) {
                    m_timer.stop();
                    m_state = "topLoaded";
                }
                else if (m_timer.hasElapsed(2.0)) {
                    m_timer.stop();
                    m_state = "fullLoaded";
                }
            break;
            case "fullLoaded":
                if (!upperSensorOn()) {
                    m_state = "emptyIntaking";
                }
                else if (!lowerSensorOn()) {
                    m_state = "topLoaded";
                }
            break;
        }
    }

    public void jumpState() { //Gets the state by raw sensor data, not state flow
        if (upperSensorOn()) {
            if (lowerSensorOn()) m_state = "fullLoaded";
            else m_state = "topLoaded";
        }
        else {
            if (lowerSensorOn()) m_state = "emptyIntaking";
            else m_state = "empty";
        }
        //Does not include "topLoadedIntaking" state.
    }

    public void overideState(String newState) { //Sets the state
        m_state = newState;
    }

    public void actOnState() {
        switch(m_state) {
            case "empty":
            case "topLoaded":
            case "fullLoaded":
                stopLowerMotor();
            break;
            case "emptyIntaking":
            case "topLoadedIntaking":
                runLowerMotor(1);
            break;
        }
    }
}
