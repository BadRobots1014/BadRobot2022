package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants.DriveTrainConstants;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.util.GyroProvider;

/**
 * A command that drives tank with a supplied power while maintaining the initial yaw angle.
 */
public class DriveStraightCommand extends CommandBase {
    private final DriveTrainSubsystem drive;
    private final GyroProvider gyro;
    private final Supplier<Double> power;
    private final PIDController pid = new PIDController(
        DriveTrainConstants.kP,
        DriveTrainConstants.kI,
        DriveTrainConstants.kD
    );

    public DriveStraightCommand(
        DriveTrainSubsystem drive,
        GyroProvider gyro,
        Supplier<Double> power
    ) {
        this.drive = drive;
        this.gyro = gyro;
        this.power = power;

        // Use addRequirements() here to declare subsystem dependencies.
        super.addRequirements(this.drive);
        this.pid.enableContinuousInput(-180, 180);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        this.pid.reset();

        // Maintain the initial yaw angle while driving (hence "drive straight").
        this.pid.setSetpoint(this.gyro.getAngle());
    }

    @Override
    public void execute() {
        final double correction = this.pid.calculate(this.gyro.getAngle());
        final double basePower = power.get();
        drive.tankDrive(basePower - correction, basePower + correction);
    }

    @Override
    public void end(boolean interrupted)
    {
        this.drive.stop();
    }
}
