package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveTrainConstants;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMax.IdleMode;

public class DriveTrainSubsystem extends SubsystemBase {
    private final CANSparkMax m_leftA = new CANSparkMax(DriveTrainConstants.kDriveTrainLeftAPort, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_leftB = new CANSparkMax(DriveTrainConstants.kDriveTrainLeftBPort, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_rightA = new CANSparkMax(DriveTrainConstants.kDriveTrainRightAPort, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_rightB = new CANSparkMax(DriveTrainConstants.kDriveTrainRightBPort, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final DifferentialDrive m_driveTrain = new DifferentialDrive(m_leftA, m_rightA);

    private final ShuffleboardTab m_tab = Shuffleboard.getTab("Drivetrain");

    public DriveTrainSubsystem() {
        m_leftA.setInverted(true);
        m_rightA.setInverted(false);

        m_leftB.setInverted(false);
        m_rightB.setInverted(true);

        m_leftA.setIdleMode(IdleMode.kBrake);
        m_rightA.setIdleMode(IdleMode.kBrake);
        m_leftB.setIdleMode(IdleMode.kBrake);
        m_rightB.setIdleMode(IdleMode.kBrake);

        m_leftB.follow(m_leftA, false);
        m_rightB.follow(m_rightA, false);

        m_tab.addNumber("Left Power", m_leftA::get);
        m_tab.addNumber("Right Power", m_rightA::get);
    }

    public void arcadeDrive(double speed, double rotation) {
        m_driveTrain.arcadeDrive(speed, rotation, true);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        m_driveTrain.tankDrive(clampPower(leftSpeed), clampPower(rightSpeed), true);
    }

    private static double clampPower(double power) {
        return MathUtil.clamp(power, -1.0, 1.0);
    }

    public void stop() {
        m_driveTrain.stopMotor();
    }
}
