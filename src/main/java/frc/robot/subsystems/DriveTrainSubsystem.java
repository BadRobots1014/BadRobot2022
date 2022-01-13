package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveTrainConstants;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;

public class DriveTrainSubsystem extends SubsystemBase {

    /*
     * TODO: With our current hardware of SPARK MAX speed controllers, this code
     * does not work! Modify the code to use SPARK MAX speed controllers instead of
     * PWMTalonFX speed controllers. To do this, you may need to install a vendor
     * library for REV Robotics.
     */

    private final PWMTalonFX m_rightMotor = new PWMTalonFX(DriveTrainConstants.kDriveTrainRightPort);
    private final PWMTalonFX m_leftMotor = new PWMTalonFX(DriveTrainConstants.kDriveTrainLeftPort);

    private final DifferentialDrive m_driveTrain = new DifferentialDrive(m_leftMotor, m_rightMotor);

    public DriveTrainSubsystem() {

    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        m_driveTrain.tankDrive(leftSpeed, rightSpeed);
    }

    public void stop() {
        m_driveTrain.stopMotor();
    }
}
