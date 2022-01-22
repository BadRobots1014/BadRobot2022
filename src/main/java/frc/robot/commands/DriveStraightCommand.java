package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

import frc.robot.Constants.DriveTrainConstants;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.util.GyroProvider;

/**
 * A command that drives tank with a supplied power while maintaining the initial yaw angle.
 */
public class DriveStraightCommand extends PIDCommand {
    private final DriveTrainSubsystem drive;

    public DriveStraightCommand(
        DriveTrainSubsystem drive,
        GyroProvider gyro,
        Supplier<Double> power
    ) {
        super(
            new PIDController(
                DriveTrainConstants.kP,
                DriveTrainConstants.kI,
                DriveTrainConstants.kD
            ),
            gyro::getAngle,
            // Maintain the initial yaw angle while driving (hence "drive straight").
            gyro.getAngle(),
            correction -> {
                final double basePower = power.get();
                drive.tankDrive(basePower + correction, basePower - correction);
            },
            drive
        );

        this.drive = drive;
        super.addRequirements(this.drive);

        super.getController().enableContinuousInput(-180, 180);
    }

    @Override
    public void end(boolean interrupted)
    {
        this.drive.stop();
    }
}
