package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

public class AutoGatherBallCommand extends SequentialCommandGroup {
    public AutoGatherBallCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyro
        ) {
        addCommands(
            //angle should be adjusted for each possible starting point, current is random
            DriveStrategyCommand.pivotTurn(driveSubsystem, () -> 56.0)
        );
    }
}
