package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveTrainConstants;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class DriveTrainSubsystem extends SubsystemBase {
    private final WPI_TalonSRX m_leftA = new WPI_TalonSRX(DriveTrainConstants.kDriveTrainLeftAPort);
    private final WPI_TalonSRX m_leftB = new WPI_TalonSRX(DriveTrainConstants.kDriveTrainLeftBPort);
    private final WPI_TalonSRX m_rightA = new WPI_TalonSRX(DriveTrainConstants.kDriveTrainRightAPort);
    private final WPI_TalonSRX m_rightB = new WPI_TalonSRX(DriveTrainConstants.kDriveTrainRightBPort);

    private final DifferentialDrive m_driveTrain;

    private final ShuffleboardTab m_tab = Shuffleboard.getTab("Drivetrain");

    public DriveTrainSubsystem() {
        m_leftA.setInverted(true);
        m_rightA.setInverted(false);

        m_leftB.setInverted(false);
        m_rightB.setInverted(true);

        m_leftA.setNeutralMode(NeutralMode.Brake);
        m_rightA.setNeutralMode(NeutralMode.Brake);
        m_leftB.setNeutralMode(NeutralMode.Brake);
        m_rightB.setNeutralMode(NeutralMode.Brake);

        m_leftB.follow(m_leftA);
        m_rightB.follow(m_rightA);

        // Initialize m_driveTrain here instead of when it's declared above because of this from the docs:
        //   "If a motor needs to be inverted, do so before passing it in."
        m_driveTrain = new DifferentialDrive(m_leftA, m_rightA);

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
