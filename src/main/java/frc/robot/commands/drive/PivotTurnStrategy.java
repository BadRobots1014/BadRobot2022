package frc.robot.commands.drive;

import frc.robot.subsystems.drive.DriveTrainSubsystem;

/**
 * A {@link DriveStrategy} for pivot-turning.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class PivotTurnStrategy implements DriveStrategy {

    private final DriveTrainSubsystem m_drive;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Pivot Turn";
    }

    @Override
    public void reset() {
        // Do nothing.
    }

    @Override
    public void execute(double rotation, double y) {
        m_drive.arcadeDrive(0, rotation);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@link PivotTurnStrategy}.
     */
    public PivotTurnStrategy(DriveTrainSubsystem drive) {
        m_drive = drive;
    }

}
