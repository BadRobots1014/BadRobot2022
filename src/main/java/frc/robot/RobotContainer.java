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
import frc.robot.commands.DeployGathererCommand;
import frc.robot.commands.BeginGatheringCommand;
import frc.robot.commands.ShootCommand;
import frc.robot.commands.TeleopDriveCommand;
import frc.robot.subsystems.GathererSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.commands.PrototypeControlCommand;
import frc.robot.commands.RetractGathererCommand;
import frc.robot.subsystems.PrototypeSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.commands.IndexerCommand;
import frc.robot.commands.UpperIndexerCommand;

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
  // TODO: Fix this constant
  private final Joystick m_driverStick = new Joystick(0);

  private final VisionSubsystem m_visionSubsystem = new VisionSubsystem();
  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();
  private final DriveTrainSubsystem m_driveTrainSubsystem = new DriveTrainSubsystem();
  private final GyroSubsystem m_gyroSubsystem = new GyroSubsystem();
  private final GathererSubsystem m_gathererSubsystem = new GathererSubsystem();
  private final IndexerSubsystem m_indexerSubsystem = new IndexerSubsystem();

  private final DeployGathererCommand m_deployGathererCommand = new DeployGathererCommand(m_gathererSubsystem);
  private final RetractGathererCommand m_retractGathererCommand = new RetractGathererCommand(m_gathererSubsystem);
  private final BeginGatheringCommand m_startGathererCommand = new BeginGatheringCommand(m_gathererSubsystem);

  private final IndexerCommand m_runIndexerCommand = new IndexerCommand(m_indexerSubsystem);
  private final UpperIndexerCommand m_runUpperIndexerCommand = new UpperIndexerCommand(m_indexerSubsystem);

  private final ShootCommand m_shootCommand = new ShootCommand(m_shooterSubsystem);
  private final TeleopDriveCommand m_teleopDriveCommand = new TeleopDriveCommand(
      m_driveTrainSubsystem,
      m_gyroSubsystem,
      m_visionSubsystem,
      () -> {
        // Invert the X-axis.
        return -1.0 * m_driverStick.getX();
      },
      m_driverStick::getY,
      () -> {
        if (m_driverStick.getRawButton(ControllerConstants.kThrottleButton)) {
          return 0.50;
        } else {
          // The default throttle is 75%. In practice, however, the maximum motor power is
          // 56%, as the
          // {@link DriveTrainSubsystem#tankDrive} currently squares inputs.
          return 0.75;
        }
      },
      () -> {
        // TODO
        return m_driverStick.getRawButton(ControllerConstants.kFollowTargetButton);
      });

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
   * Chooser for the automonous routine
   */
  private final SendableChooser<Command> m_automonousRoutineChooser = new SendableChooser<>();

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
    // We use an arcade-drive system with joystick regions that select specialized
    // drive strategies.
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

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    final JoystickButton shootButton = new JoystickButton(m_driverStick, ControllerConstants.kShootButton);
    shootButton.whileHeld(m_shootCommand);

    final JoystickButton gatherButton = new JoystickButton(m_driverStick, ControllerConstants.kCollectorButton);
    final JoystickButton lowerGathererButton = new JoystickButton(m_driverStick, ControllerConstants.kLowerButton);
    final JoystickButton raiseGathererButton = new JoystickButton(m_driverStick, ControllerConstants.kRaiseButton);

    //Temporary manual indexer controls
    final JoystickButton runIndexerButton = new JoystickButton(m_driverStick, ControllerConstants.kLowerIndexerButton);
    final JoystickButton runUpperIndexerButton = new JoystickButton(m_driverStick, ControllerConstants.kUpperIndexerButton);
    runIndexerButton.whileHeld(m_runIndexerCommand);
    runUpperIndexerButton.whileHeld(m_runUpperIndexerCommand);

    /*
     * Hold down the gather button to deploy the gatherer and run the collector. Let
     * go to retract the gatherer and stop the collector.
     */
    gatherButton.whileHeld(m_startGathererCommand).whenReleased(m_retractGathererCommand.withTimeout(5));

    // Temporary manual gather arm control
    lowerGathererButton.whileHeld(m_deployGathererCommand);
    raiseGathererButton.whenPressed(m_retractGathererCommand);

    // When limit switches are added to robot, add that to the subsystem and delete
    // withtimeout
    // final int lowerTime = GathererConstants.kDownRuntime;
    // final int raiseTime = GathererConstants.kUpRuntime;
    // gatherButton.whenPressed(m_startGathererCommand.withTimeout(lowerTime));
    // gatherButton.whenReleased(m_retractGathererCommand.withTimeout(raiseTime));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_automonousRoutineChooser.getSelected();
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
