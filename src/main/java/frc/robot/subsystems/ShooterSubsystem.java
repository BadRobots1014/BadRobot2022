package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {

    PWMTalonFX m_speedController = new PWMTalonFX(ShooterConstants.kShooterPort);

    public ShooterSubsystem() {

    }

    public void run() {
        m_speedController.set(0.5);
    }

    public void stop()
    {
        m_speedController.stopMotor();
    }

}
