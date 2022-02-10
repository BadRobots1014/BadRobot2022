package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class GathererSubsystem extends SubsystemBase {
    private final CANSparkMax m_deployMotor = new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_collectorMotor = new CANSparkMax(8, CANSparkMaxLowLevel.MotorType.kBrushless);

    public GathererSubsystem() {}

    public void runGatherer(double speed) {
        m_deployMotor.set(speed);
    }

    public void stopGatherer() {
        m_deployMotor.stopMotor();
    }

    public void startCollector() {
        m_collectorMotor.set(.3);
    }
    public void stopCollector() {
        m_collectorMotor.stopMotor();
    }
}
