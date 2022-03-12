// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.DeployGathererCommand;
import frc.robot.commands.BeginGatheringCommand;
import frc.robot.commands.ShootCommand;
import frc.robot.subsystems.GathererSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.commands.RetractGathererCommand;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.commands.IndexerCommand;
import frc.robot.commands.UpperIndexerCommand;
import frc.robot.commands.drive.FollowTargetCommand;
import frc.robot.commands.drive.TeleopDriveCommand;

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
    private final XboxController m_shooterController = new XboxController(1);

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

    private final ShootCommand m_shootCommand = new ShootCommand(m_shooterSubsystem, m_indexerSubsystem);
    private final TeleopDriveCommand m_teleopDriveCommand = new TeleopDriveCommand(
            m_driveTrainSubsystem,
            m_gyroSubsystem,
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
                    return 1.0;
                }
            });

    private final FollowTargetCommand m_followTargetCommand = new FollowTargetCommand(m_driveTrainSubsystem,
            m_visionSubsystem, () -> {
                // Invert the X-axis.
                return -1.0 * m_driverStick.getX();
            }, m_driverStick::getY);

    /**
     * Chooser for the automonous routine
     */
    private final SendableChooser<Command> m_automonousRoutineChooser = new SendableChooser<>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        System.out.println("RobotContainer initializing");
        // We use an arcade-drive system with joystick regions that select specialized
        // drive strategies.
        m_driveTrainSubsystem.setDefaultCommand(m_teleopDriveCommand);

        // Configure the button bindings
        configureButtonBindings();
        System.out.println("RobotContainer Init done");
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
      BooleanSupplier leftTriggerSupplier = () -> m_shooterController.getLeftTriggerAxis() > 0.3;
        final Trigger shootTrigger = new Trigger(leftTriggerSupplier);
        // final JoystickButton shootButton = new JoystickButton(m_shooterController, XboxController.Button.kLeftBumper.value);
        shootTrigger.whileActiveContinuous(m_shootCommand);

        final JoystickButton gatherButton = new JoystickButton(m_driverStick, ControllerConstants.kCollectorButton);
        final JoystickButton lowerGathererButton = new JoystickButton(m_driverStick, ControllerConstants.kLowerButton);
        final JoystickButton raiseGathererButton = new JoystickButton(m_driverStick, ControllerConstants.kRaiseButton);

        // Temporary manual indexer controls
        final JoystickButton runIndexerButton = new JoystickButton(m_driverStick,
                ControllerConstants.kLowerIndexerButton);
        final JoystickButton runUpperIndexerButton = new JoystickButton(m_shooterController,
                XboxController.Button.kX.value);
        runIndexerButton.whileHeld(m_runIndexerCommand);
        runUpperIndexerButton.whileHeld(m_runUpperIndexerCommand);

        final JoystickButton followTargetButton = new JoystickButton(m_driverStick,
                ControllerConstants.kFollowTargetButton);
        followTargetButton.whileHeld(m_followTargetCommand);

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
}
