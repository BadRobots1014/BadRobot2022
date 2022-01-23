package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants.DriveTrainConstants;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.util.GyroProvider;

/**
 * A command that tank-drives with a supplied power while maintaining the initial yaw angle.
 */
public class DriveStraightCommand extends CommandBase {
    private final DriveTrainSubsystem drive;
    private final GyroProvider gyro;
    private Supplier<Double> powerSource;
    /**
     * A PID controller whose process variable is the current yaw angle of the robot.
     */
    private final PIDController angularPid = new PIDController(
        DriveTrainConstants.kP,
        DriveTrainConstants.kI,
        DriveTrainConstants.kD
    );

    /**
     * Constructs a new {@link DriveStraightCommand}.
     *
     * @param drive         The {@link DriveTrainSubsystem}.
     * @param gyro          The {@link GyroProvider}.
     * @param powerSource   A supplier whose output value is in [-1.0, 1.0].
     */
    public DriveStraightCommand(
        DriveTrainSubsystem drive,
        GyroProvider gyro,
        Supplier<Double> powerSource
    ) {
        this.drive = drive;
        this.gyro = gyro;
        this.powerSource = powerSource;

        // Use addRequirements() here to declare subsystem dependencies.
        super.addRequirements(this.drive);
        this.angularPid.enableContinuousInput(-180, 180);
    }

    /**
     * Updates the base tank-drive power supplier.
     *
     * @param powerSource   A supplier whose output value is in [-1.0, 1.0].
     */
    public void setPowerSource(Supplier<Double> powerSource) {
        this.powerSource = powerSource;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // Reset the gyroscope's accumulated yaw, as well as the PID controller's error and integral
        // term, from the previous invocation of this command. Note that the yaw must be reset
        // before setting the PID setpoint.
        this.gyro.resetYaw();
        this.angularPid.reset();

        // Maintain the initial yaw angle while driving (hence "drive straight").
        this.angularPid.setSetpoint(this.gyro.getYaw());
    }

    @Override
    public void execute() {
        final double correction = this.angularPid.calculate(this.gyro.getYaw());
        final double basePower = powerSource.get();

        // Tank-drive at the speed of `basePower`, applying equal but inverse corrections to each
        // side; manually scaling the correction in proportion to 'basePower' is not necessary. This
        // causes the robot to move in an arc.
        drive.tankDrive(basePower - correction, basePower + correction);
    }

    @Override
    public void end(boolean interrupted)
    {
        this.drive.stop();
    }
}
