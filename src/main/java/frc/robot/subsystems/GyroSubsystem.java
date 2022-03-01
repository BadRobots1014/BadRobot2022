package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.PIDController;

import com.kauailabs.navx.frc.AHRS;

public class GyroSubsystem extends SubsystemBase {
    /**
     * The underlying gyroscope.
     */
    private final AHRS gyro;

    /** Constructs a new {@link GyroSubsystem}. */
    public GyroSubsystem() {
        // Initialize the gyro with default settings.
        this.gyro = new AHRS();
    }

    public static PIDController createRotationalPid(final double p, final double i, final double d) {
        final PIDController pid = new PIDController(p, i, d);

        // The robot can rotate continuously; we indicate this continuity by defining the minimum
        // and maximum angles that should be considered equivalent ([-180, 180] is the range of
        // outputs from {@link #getYaw}).
        pid.enableContinuousInput(-180, 180);

        return pid;
    }

    /**
     * Zeroes the yaw angle.
     */
    public void zeroYaw() {
        this.gyro.reset();
    }

    /**
     * Returns the current yaw.
     *
     * @return  The current yaw, in degrees, in the range [-180, 180].
     */
    public double getYaw() {
        return this.gyro.getYaw();
    }
}
