package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
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

    public DriveStraightCommand(DriveTrainSubsystem drive, Supplier<Double> basePowerSupplier) {
        super(
            new PIDController(
                DriveTrainConstants.kP,
                DriveTrainConstants.kI,
                DriveTrainConstants.kD
            ),
            GyroProvider::getAngle,
            // Maintain the initial yaw angle while driving (hence "drive straight").
            GyroProvider.getAngle(),
            correction -> {
                final double basePower = basePowerSupplier.get();
                drive.tankDrive(
                    constrainPower(basePower + correction),
                    constrainPower(basePower - correction)
                );
            },
            drive
        );

        getController().enableContinuousInput(-180, 180);

        this.drive = drive;
    }

    private static double constrainPower(double power) {
        return MathUtil.clamp(power, -1.0, 1.0);
    }

    @Override
    public void end(boolean interrupted)
    {
        this.drive.stop();
    }
}
