// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.shoot.ShootCommand;
import frc.robot.commands.BackShootBackAutoCommand;
import frc.robot.commands.BasicAutoCommand;
import frc.robot.commands.DriveOnlyAutoCommand;
import frc.robot.commands.ClimbCommand;
import frc.robot.commands.ClimbDownCommand;
import frc.robot.subsystems.gatherer.GathererSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.commands.IndexerCommand;
import frc.robot.commands.IndexerReverseCommand;
import frc.robot.commands.LeftClimbingCommand;
// import frc.robot.commands.LockClimberCommand;
import frc.robot.commands.RightClimbingCommand;
// import frc.robot.commands.UnwindLockCommand;
import frc.robot.commands.UpperIndexerCommand;
import frc.robot.commands.UpperIndexerReverseCommand;
import frc.robot.commands.drive.FollowTargetCommand;
import frc.robot.commands.drive.TeleopDriveCommand;
import frc.robot.commands.gather.RequestDisengageGathererCommand;
import frc.robot.commands.gather.RequestEngageGathererCommand;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /*
     * Private constants ------------------------------------------------------
     */

    /*
     * The port number for the primary controller.
     */
    private static final int PRIMARY_CONTROLLER_PORT = 0;

    /*
     * The port number for the secondary controller.
     */
    private static final int SECONDARY_CONTROLLER_PORT = 1;

    /**
     * The throttle value to use with {@link #teleopDriveCmd} normally.
     */
    private static final double NORMAL_THROTTLE = 1.0;

    /**
     * The throttle value to use with {@link #teleopDriveCmd} in slow mode.
     */
    private static final double SLOW_THROTTLE = 0.5;

    /**
     * The threshold reading value used to deterimne if a trigger is depressed.
     */
    private static final double TRIGGER_THRESHOLD = 0.3;

    /*
     * Controllers ------------------------------------------------------------
     */

    private final Joystick primaryController;
    private final XboxController secondaryController;

    /*
     * Subsystems -------------------------------------------------------------
     */

    private final GyroSubsystem gyroSubsystem;
    private final VisionSubsystem visionSubsystem;

    private final DriveTrainSubsystem driveTrainSubsystem;

    private final GathererSubsystem gathererSubsystem;
    private final IndexerSubsystem indexerSubsystem;
    private final ShooterSubsystem shooterSubsystem;
    private final ClimberSubsystem climberSubsystem;

    /*
     * Commands ---------------------------------------------------------------
     */

    private final TeleopDriveCommand teleopDriveCmd;

    private final FollowTargetCommand followTargetCmd;

    private final RequestEngageGathererCommand reqEngageGathererCmd;
    private final RequestDisengageGathererCommand reqDisengageGathererCmd;

    private final IndexerCommand runIndexerCmd;
    private final IndexerReverseCommand runIndexerBackCmd;
    private final UpperIndexerCommand runUpperIndexerCmd;
    private final UpperIndexerReverseCommand runUpperIndexerBackCmd;

    private final ShootCommand shootCmd;
    // private final ShootCommand shootBackCmd;
    private final ShootCommand closeShootCmd;
    // private final ShootCommand closeShootBackCmd;

    private final ClimbCommand climbUpCommand;
    private final ClimbDownCommand climbDownCommand;
    // private final LockClimberCommand lockClimberCommand;
    // private final UnwindLockCommand unwindLockCommand;
    private final LeftClimbingCommand leftClimbCommand;
    private final RightClimbingCommand rightClimbCommand;

    private final BasicAutoCommand basicAutoCommand;
    private final DriveOnlyAutoCommand driveOnlyAutoCommand;
    private final BackShootBackAutoCommand backShootBackAutoCommand;

    /*
     * Shuffleboard -----------------------------------------------------------
     */

    /**
     * {@link SendableChooser} for selecting the autonomous routine.
     */
    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    private void createAutoChooser() {
        autoChooser.setDefaultOption("Shoot and backup", basicAutoCommand);
        autoChooser.addOption("Backup, Shoot, Backup", backShootBackAutoCommand);
        autoChooser.addOption("Backup only", driveOnlyAutoCommand);
        //autoChooser.addOption("Complex Auto", );
        SmartDashboard.putData(autoChooser);
    }

    /*
     * Supplier functions -----------------------------------------------------
     */

    /**
     * Get the x-axis reading from the primary controller joystick.
     * 
     * @return the x-axis reading from the primary controller joystick
     */
    private double getPrimaryX() {
        /*
         * The x-axis on the joysticks we use is inverted.
         */
        return -this.primaryController.getX();
    }

    /**
     * Get the y-axis reading from the primary controller joystick.
     * 
     * @return the y-axis reading from the primary controller joystick
     */
    private double getPrimaryY() {
        return this.primaryController.getY();
    }

    private double getSecondaryLeftY() {
        return this.secondaryController.getLeftY();
    }

    private double getSecondaryRightY() {
        return this.secondaryController.getRightY();
    }

    /**
     * Get the desired throttle value for use with {@link #teleopDriveCmd}.
     * 
     * @return the desired throttle value.
     */
    private double getThrottle() {
        return this.primaryController.getRawButton(ControllerConstants.kThrottleButton) ? SLOW_THROTTLE
                : NORMAL_THROTTLE;
    }

    /**
     * Get whether the left trigger on the secondary controller is depressed past a
     * certain threshold.
     * 
     * @return [the reading from the left trigger] > TRIGGER_THRESHOLD
     */
    private boolean getLeftTrigger() {
        return this.secondaryController.getLeftTriggerAxis() > TRIGGER_THRESHOLD;
    }

    /**
     * Get whether the right trigger on the secondary controller is depressed past a
     * certain threshold.
     * 
     * @return [the reading from the right trigger] > TRIGGER_THRESHOLD
     */
    private boolean getRightTrigger() {
        return this.secondaryController.getRightTriggerAxis() > TRIGGER_THRESHOLD;
    }

    private boolean getManualLeftLifterActive() {
        return Math.abs(this.secondaryController.getLeftY()) > ControllerConstants.kDeadzoneRadius;
    }

    private boolean getManualRightLifterActive() {
        return Math.abs(this.secondaryController.getRightY()) > ControllerConstants.kDeadzoneRadius;
    }

    /*
     * Private helper methods -------------------------------------------------
     */

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by instantiating a {@link GenericHID} or one of its subclasses
     * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
     * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /*
         * Driving bindings
         */
        JoystickButton followTargetButton = new JoystickButton(this.primaryController,
                ControllerConstants.kFollowTargetButton);
        followTargetButton.whileHeld(this.followTargetCmd);

        /*
         * Gatherer bindings
         */

        JoystickButton gatherButton = new JoystickButton(this.primaryController, ControllerConstants.kGatherButton);
        gatherButton.whileHeld(this.reqEngageGathererCmd).whenReleased(this.reqDisengageGathererCmd);
        gatherButton.whileHeld(this.runIndexerCmd);

        /*
         * Indexer bindings
         */

        JoystickButton runIndexerButton = new JoystickButton(this.secondaryController, XboxController.Button.kA.value);
        runIndexerButton.whileHeld(this.runIndexerCmd);

        JoystickButton runIndexerBackButton = new JoystickButton(this.secondaryController, XboxController.Button.kB.value);
        runIndexerBackButton.whileHeld(this.runIndexerBackCmd);

        JoystickButton runUpperIndexerButton = new JoystickButton(this.secondaryController, XboxController.Button.kX.value);
        runUpperIndexerButton.whileHeld(this.runUpperIndexerCmd);

        JoystickButton runUpperIndexerBackButton = new JoystickButton(this.secondaryController, XboxController.Button.kY.value);
        runUpperIndexerBackButton.whileHeld(this.runUpperIndexerBackCmd);

        /*
         * Shooter bindings
         */

        Trigger shootTrigger = new Trigger(this::getLeftTrigger);
        shootTrigger.whileActiveContinuous(this.shootCmd);

        // Trigger shootBackTrigger = new Trigger(this::getRightTrigger);
        // shootBackTrigger.whileActiveContinuous(this.shootBackCmd);

        JoystickButton closeShootBumper = new JoystickButton(this.secondaryController,
                XboxController.Button.kLeftBumper.value);
        closeShootBumper.whileHeld(this.closeShootCmd);

        // JoystickButton closeShootBackBumper = new JoystickButton(this.secondaryController,
        //         XboxController.Button.kRightBumper.value);
        // closeShootBackBumper.whileHeld(this.closeShootBackCmd);

        /*
         * Climber bindings
         */
        final JoystickButton runClimberButton = new JoystickButton(this.secondaryController, XboxController.Button.kStart.value);
        runClimberButton.whileHeld(this.climbUpCommand);
        //runClimberButton.whileHeld(this.unwindLockCommand);
        final JoystickButton runClimberDownButton = new JoystickButton(this.secondaryController, XboxController.Button.kBack.value);
        runClimberDownButton.whileHeld(this.climbDownCommand);
        //runClimberDownButton.whileHeld(this.lockClimberCommand);

        Trigger manualLeftClimberControl = new Trigger(this::getManualLeftLifterActive);
        manualLeftClimberControl.whileActiveContinuous(this.leftClimbCommand);
        Trigger manualRightClimberControl = new Trigger(this::getManualRightLifterActive);
        manualRightClimberControl.whileActiveContinuous(this.rightClimbCommand);

        // final JoystickButton lockClimberButton = new JoystickButton(this.primaryController, ControllerConstants.kLockClimberButton);
        // lockClimberButton.whileHeld(this.lockClimberCommand);
        // final JoystickButton unwindLockButton = new JoystickButton(this.primaryController, ControllerConstants.kUnwindClimberButton);
        // unwindLockButton.whileHeld(this.unwindLockCommand);

    }

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructs the {@link RobotContainer} object.
     */
    public RobotContainer() {
        System.out.println("RobotContainer: starting initialization");

        /*
         * Initialize controllers
         */

        this.primaryController = new Joystick(PRIMARY_CONTROLLER_PORT);
        this.secondaryController = new XboxController(SECONDARY_CONTROLLER_PORT);

        /*
         * Initialize subsystems
         */

        this.gyroSubsystem = new GyroSubsystem();
        this.visionSubsystem = new VisionSubsystem();

        this.driveTrainSubsystem = new DriveTrainSubsystem();

        this.gathererSubsystem = new GathererSubsystem();
        this.indexerSubsystem = new IndexerSubsystem();
        this.shooterSubsystem = new ShooterSubsystem();
        this.climberSubsystem = new ClimberSubsystem();

        /*
         * Initialize commands
         */

        this.teleopDriveCmd = new TeleopDriveCommand(this.driveTrainSubsystem,
                this.gyroSubsystem, this::getPrimaryX, this::getPrimaryY,
                this::getThrottle);

        this.followTargetCmd = new FollowTargetCommand(this.driveTrainSubsystem,
                visionSubsystem, this::getPrimaryX, this::getPrimaryY);

        this.reqEngageGathererCmd = new RequestEngageGathererCommand(this.gathererSubsystem);
        this.reqDisengageGathererCmd = new RequestDisengageGathererCommand(this.gathererSubsystem);

        this.runIndexerCmd = new IndexerCommand(this.indexerSubsystem);
        this.runIndexerBackCmd = new IndexerReverseCommand(this.indexerSubsystem);
        this.runUpperIndexerCmd = new UpperIndexerCommand(this.indexerSubsystem);
        this.runUpperIndexerBackCmd = new UpperIndexerReverseCommand(this.indexerSubsystem);

        this.shootCmd = new ShootCommand(this.shooterSubsystem, this.indexerSubsystem, ShooterConstants.farShotPower, "Far Forward");
        // this.shootBackCmd = new ShootCommand(this.shooterSubsystem, this.indexerSubsystem, ShooterConstants.farBackShotPower, "Far Backward");
        this.closeShootCmd = new ShootCommand(this.shooterSubsystem, this.indexerSubsystem, ShooterConstants.closeShotPower, "Close Forward");
        // this.closeShootBackCmd = new ShootCommand(this.shooterSubsystem, this.indexerSubsystem, ShooterConstants.closeBackShotPower, "Close Backward");

        this.driveTrainSubsystem.setDefaultCommand(this.teleopDriveCmd);

        this.climbDownCommand = new ClimbDownCommand(this.climberSubsystem);
        this.climbUpCommand = new ClimbCommand(this.climberSubsystem);
        this.leftClimbCommand = new LeftClimbingCommand(this.climberSubsystem, this::getSecondaryLeftY);
        this.rightClimbCommand = new RightClimbingCommand(this.climberSubsystem, this::getSecondaryRightY);
        // this.lockClimberCommand = new LockClimberCommand(this.climberSubsystem);
        // this.unwindLockCommand = new UnwindLockCommand(this.climberSubsystem);

        this.basicAutoCommand = new BasicAutoCommand(this.driveTrainSubsystem, this.gyroSubsystem, this.shooterSubsystem, this.indexerSubsystem);
        this.driveOnlyAutoCommand = new DriveOnlyAutoCommand(this.driveTrainSubsystem, this.gyroSubsystem);
        this.backShootBackAutoCommand = new BackShootBackAutoCommand(this.driveTrainSubsystem, this.gyroSubsystem, this.shooterSubsystem, this.indexerSubsystem);

        createAutoChooser();

        configureButtonBindings();

        System.out.println("RobotContainer: initialization done");
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
