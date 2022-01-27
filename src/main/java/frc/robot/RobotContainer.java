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
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.ShootCommand;
import frc.robot.commands.TeleopDriveCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.commands.PrototypeControlCommand;
import frc.robot.subsystems.PrototypeSubsystem;


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
  private final Joystick m_driverStick = new Joystick(ControllerConstants.kControllerPort);

  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();
  private final DriveTrainSubsystem m_driveTrainSubsystem = new DriveTrainSubsystem();
  private final GyroSubsystem m_gyroSubsystem = new GyroSubsystem();

  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);
  private final ShootCommand m_shootCommand = new ShootCommand(m_shooterSubsystem);
  private final TeleopDriveCommand m_teleopDriveCommand = new TeleopDriveCommand(
    m_driveTrainSubsystem,
    m_gyroSubsystem,
    m_driverStick::getX,
    m_driverStick::getY,
    this::throttleOutput
  );

  /**
   * The joystick used for prototyping controls.
   */
  private final Joystick m_prototypeJoystick = new Joystick(ControllerConstants.kPrototypePort);

  /**
   * The {@code PrototypeSubsystem} used for prototyping.
   */
  private final PrototypeSubsystem m_prototypeSubsystem = new PrototypeSubsystem();

  /**
   * The {@code PrototypeControlCommand} used to control the
   * {@code PrototypeSubsystem}.
   */
  private final PrototypeControlCommand m_prototypeControlCommand = new PrototypeControlCommand(m_prototypeSubsystem,
      this::getPrototypePowerOutput);

  /**
   * The chooser for the input to drive the prototyping speed controllers.
   * Configured to accept input from the joystick or from a discrete value entered
   * into Shuffleboard.
   */
  private final SendableChooser<Boolean> m_prototypeInputChooser = new SendableChooser<>();

  /**
   * The speed [0, 1] to drive the prototyping speed controllers at.
   */
  private final NetworkTableEntry m_prototypePower;

  /**
   * Whether or not the output to the speed controllers should be inverted.
   */
  private final NetworkTableEntry m_prototypeOutputIsInverted;

  /**
   * A reference to the "Prototype" Shuffleboard tab object.
   */
  private final ShuffleboardTab m_prototypeTab = Shuffleboard.getTab("Prototype");

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // We use an arcade-drive system with controller deadzones that select specialized commands.
    m_driveTrainSubsystem.setDefaultCommand(m_teleopDriveCommand);

    // Configure the prototype input chooser
    m_prototypeInputChooser.setDefaultOption("Joystick", true);
    m_prototypeInputChooser.addOption("Discrete Value", false);

    m_prototypeTab.add("Input Source", m_prototypeInputChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

    // Configure the discrete power output chooser
    m_prototypePower = m_prototypeTab.add("Power Output (Discrete)", 0).withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 1)).getEntry();

    // Configure the invert output chooser
    m_prototypeOutputIsInverted = m_prototypeTab.add("Invert Output", false).withWidget(BuiltInWidgets.kToggleSwitch)
        .getEntry();

    // Configure the button bindings
    configureButtonBindings();
  }

  private double throttleOutput() {
    return 0.5 * (1.0 + (-1.0 * m_driverStick.getZ()));
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
    final JoystickButton buttonY = new JoystickButton(m_driverStick, XboxController.Button.kY.value);
    buttonY.whileHeld(m_shootCommand);
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
   * Calculates the speed value to drive the prototype speed controller at.
   *
   * @return the speed [-1, 1] to drive the prototype speed controller at.
   */
  private double getPrototypePowerOutput() {
    double inversionConstant = m_prototypeOutputIsInverted.getBoolean(false) ? 1 : -1;

    if (m_prototypeInputChooser.getSelected()) {
      return inversionConstant * m_prototypeJoystick.getY();
    } else {
      return inversionConstant * m_prototypePower.getDouble(0);
    }
  }
}
