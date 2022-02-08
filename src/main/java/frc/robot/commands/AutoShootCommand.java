package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoShootCommand extends ParallelDeadlineGroup {
    public AutoShootCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyro, 
        ShooterSubsystem shooterSubsystem
        ) {
        super(
            // Robot is turned on facing the hub so this should work
            new ShootCommand(shooterSubsystem),
            new ParallelCommandGroup
            (
            DriveStrategyCommand.pivotTurn(driveSubsystem, () -> 0.0),
            DriveStrategyCommand.anchor(driveSubsystem, gyro)
            )
        );
    }
}