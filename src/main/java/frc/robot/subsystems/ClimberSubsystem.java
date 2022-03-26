package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;

public class ClimberSubsystem extends SubsystemBase{
    private final TalonSRX m_leftClimb = new TalonSRX(ClimberConstants.kLeftClimberID);
    private final TalonSRX m_rightClimb = new TalonSRX(ClimberConstants.kRightClimberID);
    // private final TalonSRX m_leftLock = new TalonSRX(ClimberConstants.kLeftClimbLockID);
    // private final TalonSRX m_rightLock =  new TalonSRX(ClimberConstants.kRightClimbLockID);
    // private final double m_lockPower = ClimberConstants.kLockPower;

    public ClimberSubsystem () {
        // m_rightLock.setInverted(true);
        // m_leftLock.setInverted(true);
    }

    public void setClimberPower(double climbingPower) {
        m_leftClimb.set(ControlMode.PercentOutput, climbingPower);
        m_rightClimb.set(ControlMode.PercentOutput, climbingPower);
    }

    public void setLeftClimberPower(double climbingPower) {
        m_leftClimb.set(ControlMode.PercentOutput, climbingPower);
    }

    public void setRightClimberPower(double climbingPower) {
        m_rightClimb.set(ControlMode.PercentOutput, climbingPower);
    }

    public void stopClimber() {
        m_leftClimb.set(ControlMode.PercentOutput, 0);
        m_rightClimb.set(ControlMode.PercentOutput, 0);
    }

    // public void lockClimber() {
    //     m_leftLock.set(ControlMode.PercentOutput, m_lockPower);
    //     m_rightLock.set(ControlMode.PercentOutput, m_lockPower);
    // }

    // public void unwindLock() {
    //     m_leftLock.set(ControlMode.PercentOutput, -m_lockPower);
    //     m_rightLock.set(ControlMode.PercentOutput, -m_lockPower);
    // }

    // public void stopLock() {
    //     m_leftLock.set(ControlMode.PercentOutput, 0);
    //     m_rightLock.set(ControlMode.PercentOutput, 0);
    // }

}
