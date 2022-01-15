// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.PrototypeControlCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.PrototypeSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  private final Joystick m_prototypeJoystick = new Joystick(0);

  private final PrototypeSubsystem m_prototypeSubsystem = new PrototypeSubsystem();

  private final PrototypeControlCommand[] m_prototypeControlCommands = {
      new PrototypeControlCommand(m_prototypeSubsystem,
          m_prototypeJoystick, 1),
      new PrototypeControlCommand(m_prototypeSubsystem,
          m_prototypeJoystick, 2),
      new PrototypeControlCommand(m_prototypeSubsystem,
          m_prototypeJoystick, 3),
      new PrototypeControlCommand(m_prototypeSubsystem,
          m_prototypeJoystick, 4) };

  SendableChooser<Command> m_prototypeChooser = new SendableChooser<Command>();

  private final ShuffleboardTab m_prototypeTab = Shuffleboard.getTab("Prototype");

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    m_prototypeChooser.addOption("1 (SPARK MAX)", m_prototypeControlCommands[1]);
    m_prototypeChooser.addOption("2 (TalonFX)", m_prototypeControlCommands[2]);
    m_prototypeChooser.addOption("3 (TalonFX)", m_prototypeControlCommands[3]);
    m_prototypeChooser.addOption("4 (TalonFX)", m_prototypeControlCommands[4]);

    m_prototypeTab.add(m_prototypeChooser);

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }

  /**
   * Returns a {@code PrototypeControlCommand} for the motor selected in
   * Shuffleboard.
   * 
   * @return The appropriate {@code PrototypeControlCommand}.
   */
  public Command getPrototypeCommand() {
    return m_prototypeChooser.getSelected();
  }
}
