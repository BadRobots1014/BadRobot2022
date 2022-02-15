package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.GathererConstants;

public class GathererSubsystem extends SubsystemBase {
    private final TalonSRX m_deployMotor = new TalonSRX(GathererConstants.kGathererSpeedController);
    private final TalonSRX m_collectorMotor = new TalonSRX(GathererConstants.kCollectorSpeedController);

    public GathererSubsystem() {}

    public void runGatherer(double speed) {
        m_deployMotor.set(ControlMode.PercentOutput, 1.0);
    }

    public void stopGatherer() {
        m_deployMotor.set(ControlMode.PercentOutput, 0);
    }

    public void startCollector() {
        m_collectorMotor.set(ControlMode.PercentOutput, 1);
    }
    public void stopCollector() {
        m_collectorMotor.set(ControlMode.PercentOutput, 0);
    }
}
