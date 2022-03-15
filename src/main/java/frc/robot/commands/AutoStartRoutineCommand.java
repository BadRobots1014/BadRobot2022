package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.commands.drive.DriveDistanceCommand;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class AutoStartRoutineCommand extends SequentialCommandGroup {
    public AutoStartRoutineCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyro, 
        ShooterSubsystem shooterSubsystem,
        IndexerSubsystem indexerSubsystem
        ) {
        addCommands(
            new AutoShootCommand(driveSubsystem, gyro, shooterSubsystem, indexerSubsystem),
            // TODO: set -5 to actual distance needed to get out of tarmac.
            new DriveDistanceCommand(driveSubsystem, gyro, -5.0)
        );
    }
}