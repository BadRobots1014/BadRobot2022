package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.commands.drive.DriveTimeCommand;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class BackShootBackAutoCommand extends SequentialCommandGroup {
    public BackShootBackAutoCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyro, 
        ShooterSubsystem shooterSubsystem,
        IndexerSubsystem indexerSubsystem
        ) {
        addCommands(
            new DriveTimeCommand(driveSubsystem, gyro, 1.2),
            new AutoShootCommand(driveSubsystem, gyro, shooterSubsystem, indexerSubsystem, "BSB Auto"),
            new DriveTimeCommand(driveSubsystem, gyro, .8)
        );
    }
}