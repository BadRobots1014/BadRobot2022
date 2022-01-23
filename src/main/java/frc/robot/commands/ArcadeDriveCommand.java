package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.DriveTrainSubsystem;

public class ArcadeDriveCommand extends CommandBase {
    private final DriveTrainSubsystem drive;
    private final Supplier<Double> speedSource;
    private final Supplier<Double> rotationSource;

    public ArcadeDriveCommand(
        DriveTrainSubsystem drive,
        Supplier<Double> speedSource,
        Supplier<Double> rotationSource
    ) {
        this.drive = drive;
        this.speedSource = speedSource;
        this.rotationSource = rotationSource;

        super.addRequirements(drive);
    }

    @Override
    public void execute() {
        this.drive.arcadeDrive(this.speedSource.get(), this.rotationSource.get());
    }

    @Override
    public void end(boolean interrupted) {
        this.drive.stop();
    }
}
