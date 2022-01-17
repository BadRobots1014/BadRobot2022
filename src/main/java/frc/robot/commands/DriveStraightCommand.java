package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveTrainConstants;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.util.GyroProvider;

/** A command that will turn the robot to the specified angle. */
public class DriveStraightCommand extends CommandBase {
  /**
   * Turns to robot to the specified angle.
   *
   * @param targetAngleDegrees The angle to turn to
   * @param drive The drive subsystem to use
   */
    private final DriveTrainSubsystem m_driveTrain;
    private final GyroProvider m_gyro = new GyroProvider();
    private final PIDController m_controller;
    XboxController m_xController;

    public DriveStraightCommand(DriveTrainSubsystem drive, XboxController controller) {
        m_driveTrain = drive;
        m_xController = controller;
        m_controller = new PIDController(DriveTrainConstants.kP, DriveTrainConstants.kI, DriveTrainConstants.kD);
    }

  @Override
  public void initialize()
  {
    m_controller.setSetpoint(m_gyro.getAngle());
  }

  @Override
  public void execute()
  {
    double correction = m_controller.calculate(m_gyro.getAngle());
    m_driveTrain.tankDrive(m_xController.getLeftY() + correction, m_xController.getLeftY() - correction);
  }

  @Override
  public boolean isFinished()
  {
      return false;
  }

  @Override
  public void end(boolean interrupted)
  {
      m_controller.reset();
      m_driveTrain.stop();
  }
}