package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;
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
    public void execute(double x, double y) {
        super.execute(0, 0);
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
    public AnchorStrategy(DriveTrainSubsystem drive, GyroSubsystem gyro) {
        super(drive, gyro);
    }

}
