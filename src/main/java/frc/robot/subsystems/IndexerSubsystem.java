package frc.robot.subsystems;
import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
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

    private final ShuffleboardTab tab;

    public IndexerSubsystem() {
        m_state = "empty";
        checkState();
        m_shotRequested = false;

        this.tab = Shuffleboard.getTab("Indexer");
        this.tab.addBoolean("Lower Sensor", m_lowerSensorSupplier);
        this.tab.addBoolean("Upper Sensor", m_upperSensorSupplier);
        this.tab.addString("State", () -> m_state);
    }

    public void periodic() {
        checkState();
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

    public boolean lowerSensorOn() {
        return m_lowerSensor.get();
    }

    public boolean upperSensorOn() {
        return m_upperSensor.get();
    }

    public void checkState() {
        if (m_state.equals("empty")) {
            if (lowerSensorOn()) {
                m_state = "emptyIntaking";
            }
        }
        else if (m_state.equals("emptyIntaking")) {
            if (upperSensorOn()) {
                m_state = "topLoaded";
            }
            //TODO Stop if the ball rolls out?
        }
        else if (m_state.equals("topLoaded")) {
            if (m_shotRequested) {
                m_state = "topLoadedShotReq";
            }
            else if (lowerSensorOn()) {
                m_state = "topLoadedIntaking";
            }
        }
        else if (m_state.equals("topLoadedIntaking")) {
            if (!lowerSensorOn()) {
                m_state = "topLoaded";
            }
            //Time passes: m_state = "fullLoaded";
        }
        else if (m_state.equals("fullLoaded")) {
        }
    }
}
