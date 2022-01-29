package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;

import frc.robot.subsystems.GyroSubsystem;

/**
 * A {@link DriveStrategy} for anchoring the robot, or mainting the current angle and position,
 * using PID.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class AnchorStrategy extends DriveStraightStrategy {

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Anchor";
    }

    @Override
    public WheelSpeeds execute(double x, double y) {
        return super.execute(0, 0);
    }

    @Override
    public boolean shouldLockPosition() {
        return true;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@link AnchorStrategy} that controls the given
     * {@code drive} using gyroscope readings from {@code gyro} for straight
     * driving.
     *
     * @param drive
     * @param gyro
     * @author Victor Chen <victorc.1@outlook.com>
     * @author Will Blankemeyer
     */
    public AnchorStrategy(GyroSubsystem gyro) {
        super(gyro);
    }

}