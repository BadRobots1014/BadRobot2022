package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.commands.drive.AnchorCommand;
import frc.robot.commands.drive.PivotCommand;
import frc.robot.commands.shoot.ShootCommand;

public class AutoShootCommand extends SequentialCommandGroup {
    public AutoShootCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyroSubsystem, 
        ShooterSubsystem shooterSubsystem, 
        IndexerSubsystem indexerSubsystem
        ) {
        super(
            // Robot is turned on facing the hub so this should work
            // Will likely eventually be limelight code
            new PivotCommand(driveSubsystem, () -> 0.0),
            new ParallelDeadlineGroup
            (
                new ShootCommand(shooterSubsystem, indexerSubsystem, 0.8).withTimeout(2),
                new AnchorCommand(driveSubsystem, gyroSubsystem)
            )
        );
    }
}