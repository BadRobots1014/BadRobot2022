package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveTrainConstants;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class DriveTrainSubsystem extends SubsystemBase {

    /*
     * TODO: With our current hardware of SPARK MAX speed controllers, this code
     * does not work! Modify the code to use SPARK MAX speed controllers instead of
     * PWMTalonFX speed controllers. To do this, you may need to install a vendor
     * library for REV Robotics.
     */

    // private final PWMTalonFX m_rightMotor = new PWMTalonFX(DriveTrainConstants.kDriveTrainRightPort);
    // private final PWMTalonFX m_leftMotor = new PWMTalonFX(DriveTrainConstants.kDriveTrainLeftPort);
    

    private final CANSparkMax m_frontRight = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_frontLeft = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_backRight = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax m_backLeft = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final MotorControllerGroup m_rightMotors = new MotorControllerGroup(m_frontRight, m_backRight);
    private final MotorControllerGroup m_leftMotors = new MotorControllerGroup(m_frontLeft, m_backLeft);

    private final DifferentialDrive m_driveTrain = new DifferentialDrive(m_leftMotors, m_rightMotors);

    public DriveTrainSubsystem() {
        m_leftMotors.setInverted(true);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        m_driveTrain.tankDrive(leftSpeed, rightSpeed);
    }

    public void stop() {
        m_driveTrain.stopMotor();
    }
}
