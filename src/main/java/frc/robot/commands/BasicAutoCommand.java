package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.commands.drive.DriveTimeCommand;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class BasicAutoCommand extends SequentialCommandGroup {
    public BasicAutoCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyro, 
        ShooterSubsystem shooterSubsystem,
        IndexerSubsystem indexerSubsystem
        ) {
        addCommands(
            new AutoShootCommand(driveSubsystem, gyro, shooterSubsystem, indexerSubsystem, "Basic Auto"),
            // TODO: set -5 to actual distance needed to get out of tarmac.
            new DriveTimeCommand(driveSubsystem, gyro, -2.0)
        );
    }
}