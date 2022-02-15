// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.commands.drive.DriveStraightStrategy;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.DriveStrategyCommand;

/** An example command that uses an example subsystem. */
public class LockOnBallCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final GyroSubsystem m_gyroSubsystem;
  private final VisionSubsystem m_visionSubSystem;
  private final DriveTrainSubsystem m_driveSubsystem;
  //private final DriveStrategyCommand m_driveStrategy;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public LockOnBallCommand(GyroSubsystem subsystem, VisionSubsystem vision, DriveTrainSubsystem drive) {
    this.m_gyroSubsystem = subsystem;
    this.m_visionSubSystem = vision;
    this.m_driveSubsystem = drive;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      double totalAngle = 15.0;
      while(!isFinished()) {
        DriveStrategyCommand.pivotTurn(m_driveSubsystem, () -> (totalAngle += 15));
      }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_visionSubSystem.targetIsVisible();
  }
}
