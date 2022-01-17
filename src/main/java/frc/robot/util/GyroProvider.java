package frc.robot.util;

import com.kauailabs.navx.frc.AHRS;

public class GyroProvider {
    
    private final AHRS m_gyro = new AHRS();

    public double getAngle(){
        return m_gyro.getAngle();
    }

}
