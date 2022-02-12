package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;

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
    public void execute(double rotation, double y) {
        super.execute(rotation, 0);
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
    public PivotTurnStrategy(DriveTrainSubsystem drive) {
        super(drive);
    }

}
