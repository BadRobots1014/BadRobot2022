package frc.robot.commands;

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
        addCommands(
            // todo: make a command that orients towards hub instead of pivot turn
            DriveStrategyCommand.pivotTurn(driveSubsystem, () -> 56.0),
            DriveStrategyCommand.anchor(driveSubsystem, gyro),
            new ShootCommand(shooterSubsystem)
        );
    }
}
