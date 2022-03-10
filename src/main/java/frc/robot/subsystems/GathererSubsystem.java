package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.GathererConstants;

public class GathererSubsystem extends SubsystemBase {
    private final TalonSRX m_deployMotor = new TalonSRX(GathererConstants.kGathererSpeedController);
    private final TalonSRX m_collectorMotor = new TalonSRX(GathererConstants.kCollectorSpeedController);

    public GathererSubsystem() {}

    /**
     * Runs the gatherer with a custom speed, mainly for testing.
     * @param speed
     */
    public void runGatherer(double speed) {
        System.out.println("Run arm "+speed+"...");
        m_deployMotor.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Deploys the gatherer from stored position.
     * TODO: Make the gatherer stop when it reaches the limit switch.
     * (Check to make sure limit switch is on the robot)
     */
    public void deployGatherer() {
        m_deployMotor.set(ControlMode.PercentOutput, -0.20);
    }

    /**
     * Retracts the gatherer from deployed position.
     * TODO: Make the gatherer stop when it reaches the limit switch.
     * (Check to make sure limit switch is on the robot)
     */
    public void retractGatherer() {
        m_deployMotor.set(ControlMode.PercentOutput, 0.20);
    }

    public void stopGatherer() {
        System.out.println("Stop arm...");        
        m_deployMotor.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Starts the collector, the part of the gatherer that uses
     * wheels to direct balls into the robot.
     */
    public void startCollector() {
        System.out.println("Start collector...");
        m_collectorMotor.set(ControlMode.PercentOutput, 1);
    }

    public void stopCollector() {
        System.out.println("Stop collector...");
        m_collectorMotor.set(ControlMode.PercentOutput, 0);
    }
}
