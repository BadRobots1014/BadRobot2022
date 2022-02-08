package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoShootCommand extends SequentialCommandGroup {
    public AutoShootCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyro, 
        ShooterSubsystem shooterSubsystem
        ) {
        super(
            // Robot is turned on facing the hub so this should work
            DriveStrategyCommand.pivotTurn(driveSubsystem, () -> 0.0),
            new ParallelDeadlineGroup
            (
                new ShootCommand(shooterSubsystem),
                DriveStrategyCommand.anchor(driveSubsystem, gyro)
            )
        );
    }
}