package frc.robot.util;

import com.kauailabs.navx.frc.AHRS;

public class GyroProvider {
    private static final AHRS m_gyro = new AHRS();

    public static double getAngle(){
        return Math.IEEEremainder(m_gyro.getAngle(), 360);
    }
}
