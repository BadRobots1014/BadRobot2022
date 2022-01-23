package frc.robot.commands;

import java.util.function.Supplier;

import frc.robot.subsystems.DriveTrainSubsystem;

public class PivotTurnCommand extends ArcadeDriveCommand {
    public PivotTurnCommand(DriveTrainSubsystem drive, Supplier<Double> rotationSource) {
        super(drive, () -> 0.0, rotationSource);
    }
}
