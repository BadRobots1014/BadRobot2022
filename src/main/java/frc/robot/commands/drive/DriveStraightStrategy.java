package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

/**
 * A {@link DriveStrategy} for driving straight using PID.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class DriveStraightStrategy implements DriveStrategy {

    private final DriveTrainSubsystem m_drive;
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
    public void execute(double x, double power) {
        final double correction = m_gyro.getRotationalPid();
        m_drive.tankDrive(power - correction, power + correction);
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
    public DriveStraightStrategy(DriveTrainSubsystem drive, GyroSubsystem gyro) {
        m_drive = drive;
        m_gyro = gyro;
    }

}
