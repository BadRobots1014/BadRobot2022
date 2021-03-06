package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IndexerConstants;

public class IndexerSubsystem extends SubsystemBase {
    private final TalonSRX m_lowerMotor = new TalonSRX(IndexerConstants.kLowerIndexerSpeedController);
    private final TalonSRX m_upperMotor = new TalonSRX(IndexerConstants.kUpperIndexerSpeedController);

    public IndexerSubsystem() {
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
}
