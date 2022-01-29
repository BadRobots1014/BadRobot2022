package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;
import frc.robot.subsystems.GyroSubsystem;

/**
 * A {@link DriveStrategy} for driving straight using PID.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class DriveStraightStrategy implements DriveStrategy {

    /**
     * The {@link GyroSubsystem} to obtain gyroscope readings from.
     */
    private final GyroSubsystem m_gyro;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Drive Straight";
    }

    @Override
    public void reset() {
        m_gyro.zeroYaw();
    }

    @Override
    public WheelSpeeds execute(double x, double power) {
        final double correction = m_gyro.getRotationalPid();
        return new WheelSpeeds(power - correction, power + correction);
    }

    @Override
    public boolean shouldLockPosition() {
        return false;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@code DriveStraightStrategy} that controls the given
     * {@code drive} using gyroscope readings from {@code gyro} for straight
     * driving.
     *
     * @param drive
     * @param gyro
     * @author Victor Chen <victorc.1@outlook.com>
     * @author Will Blankemeyer
     */
    public DriveStraightStrategy(GyroSubsystem gyro) {
        m_gyro = gyro;
    }

}
