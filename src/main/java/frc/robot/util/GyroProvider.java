package frc.robot.util;

import com.kauailabs.navx.frc.AHRS;

public class GyroProvider {
    private final AHRS m_gyro = new AHRS();

    public void resetDisplacement() {
        m_gyro.resetDisplacement();
    }

    public double getAngle() {
        return Math.IEEEremainder(m_gyro.getAngle(), 360);
    }

    public double getDisplacementX() {
        return m_gyro.getDisplacementX();
    }
}
