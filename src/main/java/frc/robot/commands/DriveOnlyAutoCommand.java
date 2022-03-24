package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.Constants.DriveTrainConstants;
import frc.robot.commands.drive.DriveTimeCommand;
import frc.robot.subsystems.GyroSubsystem;

public class DriveOnlyAutoCommand extends SequentialCommandGroup {
    public DriveOnlyAutoCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyro
        ) {
        addCommands(
            // TODO: set -5 to actual distance needed to get out of tarmac.
            new DriveTimeCommand(driveSubsystem, gyro, 2.0)
        );
    }
}