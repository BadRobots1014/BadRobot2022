package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;

/**
 * A {@link DriveStrategy} that contains the algorithm to drive with arcade
 * drive.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public final class PivotTurnStrategy implements DriveStrategy {

    /**
     * The DriveTrainSubsystem to control.
     */
    private final DriveTrainSubsystem m_subsystem;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Pivot-turn";
    }

    @Override
    public void reset() {
        // Do nothing.
    }

    @Override
    public void execute(double x, double y) {
        this.m_subsystem.arcadeDrive(0.0, x);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@code PivotTurnStrategy} to control the given
     * {@code subsystem}.
     * 
     * @param subsystem The {@link DriveTrainSubsystem} to control.
     */
    public PivotTurnStrategy(DriveTrainSubsystem subsystem) {
        m_subsystem = subsystem;
    }

}
