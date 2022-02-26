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
        System.out.println("Run arm "+speed+"...");
        m_deployMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopGatherer() {
        System.out.println("Stop arm...");        
        m_deployMotor.set(ControlMode.PercentOutput, 0);
    }

    public void startCollector() {
        System.out.println("Start collector...");
        m_collectorMotor.set(ControlMode.PercentOutput, 1);
    }
    public void stopCollector() {
        System.out.println("Stop collector...");
        m_collectorMotor.set(ControlMode.PercentOutput, 0);
    }
}
