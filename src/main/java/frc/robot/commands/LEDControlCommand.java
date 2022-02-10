/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.LEDSubsystem;
import frc.robot.subsystems.LEDSubsystem.LEDState;

public class LEDControlCommand extends CommandBase {
  private final LEDSubsystem m_ledSubsystem;
  private final Joystick m_driveController;
  /**
   * Creates a new LEDControlCommand.
   */
  public LEDControlCommand(LEDSubsystem ledSubsystem, Joystick driveController) {
    m_ledSubsystem = ledSubsystem;
    m_driveController = driveController;
    addRequirements(ledSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
    m_ledSubsystem.setLightsPattern(LEDState.k1014COLOR);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_ledSubsystem.setLightsPattern(LEDState.k1014COLOR);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (RobotController.getBatteryVoltage() <= 10) {
      m_ledSubsystem.setLightsPattern(LEDState.kLOW_BATTERY);
    }
    else if (m_driveController.getRawButton(5)) { // Not a specific button, just fon an example
      m_ledSubsystem.setLightsPattern(LEDState.kAMERICA);
    }
    else {
      m_ledSubsystem.setLightsRainbow();

    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_ledSubsystem.setLightsPattern(LEDState.k1014COLOR);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
