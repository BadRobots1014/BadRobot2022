package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.commands.drive.AnchorCommand;
import frc.robot.commands.shoot.ShootCommand;

public class AutoShootCommand extends ParallelRaceGroup {
    public AutoShootCommand (
        DriveTrainSubsystem driveSubsystem, 
        GyroSubsystem gyroSubsystem, 
        ShooterSubsystem shooterSubsystem, 
        IndexerSubsystem indexerSubsystem
        ) {
        super(
                new ShootCommand(shooterSubsystem, indexerSubsystem, 0.5).withTimeout(2),
                new AnchorCommand(driveSubsystem, gyroSubsystem)
        );
    }
}