package frc.robot.util;

import com.kauailabs.navx.frc.AHRS;

public class GyroProvider {
    private final AHRS m_gyro = new AHRS();

    /**
     * Resets the accumulated yaw.
     */
    public void resetYaw() {
        m_gyro.reset();
    }

    /**
     * Zeroes the measured displacement in all axes, i.e., the position of the robot.
     */
    public void resetDisplacement() {
        m_gyro.resetDisplacement();
    }

    /**
     * Returns the current yaw.
     *
     * @return  The current yaw, in degrees, in the range [0, 360].
     */
    public double getYaw() {
        return Math.IEEEremainder(m_gyro.getAngle(), 360);
    }

    /**
     * Returns the current displacement in the X-axis.
     *
     * @return  The current X displacement, in meters.
     */
    public double getDisplacementX() {
        return m_gyro.getDisplacementX();
    }
}
