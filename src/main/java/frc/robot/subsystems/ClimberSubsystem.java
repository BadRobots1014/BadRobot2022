package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;

public class ClimberSubsystem extends SubsystemBase{
    private final TalonSRX m_leftMotor = new TalonSRX(ClimberConstants.kLeftClimberID);
    private final TalonSRX m_rightMotor = new TalonSRX(ClimberConstants.kRightClimberID);
    private final TalonSRX m_leftLock = new TalonSRX(ClimberConstants.kLeftClimbLockID);
    private final TalonSRX m_rightLock =  new TalonSRX(ClimberConstants.kRightClimbLockID);
    private final double m_lockPower = ClimberConstants.kLockPower;

    public ClimberSubsystem () {
    }

    public void setClimberPower(double climbingPower) {
        m_leftMotor.set(ControlMode.PercentOutput, climbingPower);
        m_rightMotor.set(ControlMode.PercentOutput, climbingPower);
    }

    public void setLeftClimberPower(double climbingPower) {
        m_leftMotor.set(ControlMode.PercentOutput, climbingPower);
    }

    public void setRightClimberPower(double climbingPower) {
        m_rightMotor.set(ControlMode.PercentOutput, climbingPower);
    }

    public void stopClimber() {
        m_leftMotor.set(ControlMode.PercentOutput, 0);
        m_rightMotor.set(ControlMode.PercentOutput, 0);
    }

    public void lockClimber() {
        m_leftLock.set(ControlMode.PercentOutput, m_lockPower);
        m_rightLock.set(ControlMode.PercentOutput, m_lockPower);
    }

    public void unlockClimber() {
        m_leftLock.set(ControlMode.PercentOutput, 0);
        m_rightLock.set(ControlMode.PercentOutput, 0);
    }

}
