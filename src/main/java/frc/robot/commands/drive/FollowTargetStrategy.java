package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.VisionSubsystem;

/**
 * A {@link DriveStrategy} for following cargo using PID.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class FollowTargetStrategy implements DriveStrategy {

    private final DriveTrainSubsystem m_drive;
    /**
     * The {@link VisionSubsystem} to obtain gyroscope readings from.
     */
    private final VisionSubsystem m_vision;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Follow Cargo";
    }

    @Override
    public void reset() {
        // Do nothing.
    }

    @Override
    public void execute(double x, double power) {
        final double correction = m_vision.getRotationalPid();
        m_drive.tankDrive(power - correction, power + correction);
        // TODO: m_drive.arcadeDrive(power, correction);
    }

    @Override
    public boolean shouldLockPosition() {
        return false;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@code FollowTargetStrategy} that controls the given
     * {@code drive} using camera readings from {@code vision} for following
     * the cargo.
     *
     * @param drive
     * @param gyro
     * @author Victor Chen <victorc.1@outlook.com>
     * @author Will Blankemeyer
     */
    public FollowTargetStrategy(DriveTrainSubsystem drive, VisionSubsystem vision) {
        m_drive = drive;
        m_vision = vision;
    }

}
