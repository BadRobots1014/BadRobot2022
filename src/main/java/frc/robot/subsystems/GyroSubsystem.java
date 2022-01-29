package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.PIDController;

import com.kauailabs.navx.frc.AHRS;

import frc.robot.Constants.DriveTrainConstants;

public class GyroSubsystem extends SubsystemBase {
    /**
     * The underlying gyroscope.
     */
    private final AHRS gyro;
    /**
     * A PID controller that maintains the current yaw angle.
     */
    private final PIDController rotationalPid;
    /**
     * A PID controller that maintains the current X displacement.
     *
     * The X-axis is parallel to the robot's wheels; when the robot drives straight, it moves along
     * this axis.
     */
    private final PIDController positionalPid;

    /** Constructs a new {@link GyroSubsystem}. */
    public GyroSubsystem() {
        // Initialize the gyro with default settings.
        this.gyro = new AHRS();

        this.rotationalPid = new PIDController(
            DriveTrainConstants.kRotationalP,
            DriveTrainConstants.kRotationalI,
            DriveTrainConstants.kRotationalD
        );

        // The robot can rotate continuously; we indicate this continuity by defining the minimum
        // and maximum angles that should be considered equivalent ([-180, 180] is the range of
        // outputs from {@link #getYaw}).
        this.rotationalPid.enableContinuousInput(-180, 180);

        this.positionalPid = new PIDController(
            DriveTrainConstants.kPositionalP,
            DriveTrainConstants.kPositionalI,
            DriveTrainConstants.kPositionalD
        );
    }

    /**
     * Zeroes the yaw angle and rotational PID setpoint.
     */
    public void zeroYaw() {
        this.gyro.reset();
        this.rotationalPid.setSetpoint(this.getYaw());
    }

    /**
     * Zeroes the measured displacement in all axes (i.e., the position of the robot) as well as the
     * positional PID setpoint.
     */
    public void zeroDisplacement() {
        this.gyro.resetDisplacement();
        this.positionalPid.setSetpoint(this.getDisplacementX());
    }

    /**
     * Returns the output of the rotational PID controller.
     *
     * @return  The PID output.
     */
    public double getRotationalPid() {
        return this.rotationalPid.calculate(this.getYaw());
    }

    /**
     * Returns the output of the positional PID controller.
     *
     * @return  The PID output.
     */
    public double getPositionalPid() {
        return this.positionalPid.calculate(this.getDisplacementX());
    }

    /**
     * Returns the current yaw.
     *
     * @return  The current yaw, in degrees, in the range [-180, 180].
     */
    public double getYaw() {
        return this.gyro.getYaw();
    }

    /**
     * Returns the current displacement in the X-axis.
     *
     * @return  The current X displacement, in meters.
     */
    public double getDisplacementX() {
        return this.gyro.getDisplacementX();
    }
}
