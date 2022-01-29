package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;


public class DriveTrainSubsystem extends SubsystemBase {
    private final CANSparkMax m_frontRight = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_frontLeft = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_backRight = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_backLeft = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final MotorControllerGroup m_rightMotors = new MotorControllerGroup(m_frontRight, m_backRight);
    private final MotorControllerGroup m_leftMotors = new MotorControllerGroup(m_frontLeft, m_backLeft);

    private final DifferentialDrive m_driveTrain = new DifferentialDrive(m_leftMotors, m_rightMotors);

    private final ShuffleboardTab m_tab = Shuffleboard.getTab("Drivetrain");

    public DriveTrainSubsystem() {
        m_leftMotors.setInverted(true);

        m_tab.addNumber("Left Power", m_leftMotors::get);
        m_tab.addNumber("Right Power", m_rightMotors::get);
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
