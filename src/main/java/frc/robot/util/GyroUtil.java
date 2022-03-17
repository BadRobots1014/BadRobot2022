package frc.robot.util;

import edu.wpi.first.math.controller.PIDController;

import com.kauailabs.navx.frc.AHRS;

public final class GyroUtil {
    /*
     * Private members --------------------------------------------------------
     */

    /**
     * The underlying gyroscope.
     */
    private final AHRS gyro;

    /**
     * The {@link GyroUtil} to return when one is requested using {@link #get}.
     */
    private static GyroUtil gyroUtil;

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructs a new {@link GyroUtil}.
     */
    private GyroUtil() {
        // Initialize the gyro with default settings.
        this.gyro = new AHRS();
    }

    /*
     * Public static utility methods ------------------------------------------
     */

    /**
     * Creates a new {@link PIDController} for rotational motion.
     * 
     * @param p the proportional term
     * @param i the integral term
     * @param d the derivative term
     * @return a {@link PIDController} with the given PID parameters
     */
    public static PIDController createRotationalPid(final double p, final double i, final double d) {
        PIDController pid = new PIDController(p, i, d);

        /*
         * The robot can rotate continuously; we indicate this continuity by defining
         * the minimum and maximum angles that should be considered equivalent ([-180,
         * 180] is the range of outputs from {@link #getYaw}).
         */
        pid.enableContinuousInput(-180, 180);

        return pid;
    }

    /**
     * Get the singleton {@link GyroUtil} instance.
     * 
     * @return the {@link GyroUtil}
     */
    public static GyroUtil get() {
        if (gyroUtil == null) {
            gyroUtil = new GyroUtil();
        }

        return gyroUtil;
    }

    /*
     * Public instance utility methods ----------------------------------------
     */

    /**
     * Returns the current yaw.
     *
     * @return The current yaw, in degrees, in the range [-180, 180].
     */
    public double getYaw() {
        return this.gyro.getYaw();
    }
}
