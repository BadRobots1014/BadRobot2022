// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.PrototypeControlCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.PrototypeSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

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
      new PrototypeControlCommand(m_prototypeSubsystem, 1, this::getPrototypePowerOutput),
      new PrototypeControlCommand(m_prototypeSubsystem, 2, this::getPrototypePowerOutput),
      new PrototypeControlCommand(m_prototypeSubsystem, 3, this::getPrototypePowerOutput),
      new PrototypeControlCommand(m_prototypeSubsystem, 4, this::getPrototypePowerOutput),
  };

  private final SendableChooser<Command> m_prototypeCommandChooser = new SendableChooser<>();
  private final SendableChooser<Boolean> m_prototypeInputChooser = new SendableChooser<>();

  private final NetworkTableEntry m_prototypePower;
  private final NetworkTableEntry m_prototypeOutputIsInverted;

  private final ShuffleboardTab m_prototypeTab = Shuffleboard.getTab("Prototype");

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    m_prototypeCommandChooser.addOption("SPARK MAX", m_prototypeControlCommands[0]);
    m_prototypeCommandChooser.addOption("TalonFX (Port 11)", m_prototypeControlCommands[1]);
    m_prototypeCommandChooser.addOption("TalonFX (Port 13)", m_prototypeControlCommands[2]);
    m_prototypeCommandChooser.addOption("TalonFX (Port 14)", m_prototypeControlCommands[3]);

    m_prototypeInputChooser.setDefaultOption("Joystick", true);
    m_prototypeInputChooser.addOption("Discrete Value", false);

    m_prototypeTab.add("Output", m_prototypeCommandChooser).withWidget(BuiltInWidgets.kComboBoxChooser);
    m_prototypeTab.add("Input Source", m_prototypeInputChooser).withWidget(BuiltInWidgets.kComboBoxChooser);
    m_prototypePower = m_prototypeTab.add("Power Output (Discrete)", 0).withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 1)).getEntry();
    m_prototypeOutputIsInverted = m_prototypeTab.add("Invert Output", false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

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
    JoystickButton prototypeButton = new JoystickButton(m_prototypeJoystick, 1);

    prototypeButton.whenHeld(new RunCommand(this::schedulePrototypeCommand, m_prototypeSubsystem))
    .whenReleased(new RunCommand(this::deschedulePrototypeCommmand));
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
    return m_prototypeCommandChooser.getSelected();
  }

  private void schedulePrototypeCommand() {
    CommandScheduler.getInstance().schedule(this.getPrototypeCommand());
  }

  private void deschedulePrototypeCommmand() {
    CommandScheduler.getInstance().cancelAll();
  }

  private double getPrototypePowerOutput() {
    int inversionConstant = m_prototypeOutputIsInverted.getBoolean(false) ? 1 : -1;

    if (m_prototypeInputChooser.getSelected()) {
      return inversionConstant * m_prototypeJoystick.getY();
    } else {
      return inversionConstant * m_prototypePower.getDouble(0);
    }
  }
}
