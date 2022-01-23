package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.kauailabs.navx.frc.AHRS;

public class GyroSubsystem extends SubsystemBase {
    private final AHRS gyro = new AHRS();

    /** Constructs a new {@link GyroSubsystem}. */
    public GyroSubsystem() {}

    /**
     * Resets the accumulated yaw.
     */
    public void resetYaw() {
        this.gyro.reset();
    }

    /**
     * Zeroes the measured displacement in all axes, i.e., the position of the robot.
     */
    public void resetDisplacement() {
        this.gyro.resetDisplacement();
    }

    /**
     * Returns the current yaw.
     *
     * @return  The current yaw, in degrees, in the range [0, 360].
     */
    public double getYaw() {
        return Math.IEEEremainder(this.gyro.getAngle(), 360);
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
