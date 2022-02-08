package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoStartRoutineCommand extends SequentialCommandGroup {
    public AutoStartRoutineCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyro, 
        ShooterSubsystem shooterSubsystem
        ) {
        addCommands(
            new AutoShootCommand(driveSubsystem, gyro, shooterSubsystem),
            // TODO: set -5 to actual distance needed to get out of tarmac.
            new DriveDistanceCommand(driveSubsystem, gyro, -5.0)
        );
    }
}