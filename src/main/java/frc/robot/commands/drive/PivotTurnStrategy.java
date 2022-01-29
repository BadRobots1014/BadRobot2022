package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;

/**
 * A {@link DriveStrategy} for pivot-turning.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class PivotTurnStrategy extends ArcadeDriveStrategy {

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Pivot-turn";
    }

    @Override
    public WheelSpeeds execute(double rotation, double y) {
        return super.execute(rotation, 0);
    }

    @Override
    public boolean shouldLockPosition() {
        return true;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@link PivotTurnStrategy}.
     */
    public PivotTurnStrategy() {}

}
