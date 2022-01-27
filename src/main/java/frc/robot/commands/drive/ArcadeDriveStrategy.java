package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;

/**
 * A {@link DriveStrategy} that contains the algorithm to drive with arcade
 * drive.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public final class ArcadeDriveStrategy implements DriveStrategy {

    /**
     * The DriveTrainSubsystem to control.
     */
    private final DriveTrainSubsystem m_subsystem;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Arcade-drive";
    }

    @Override
    public void reset() {
        // Do nothing.
    }

    @Override
    public void execute(double x, double y) {
        this.m_subsystem.arcadeDrive(y, x);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@code ArcadeDriveStrategy} to control the given
     * {@code subsystem}.
     * 
     * @param subsystem The {@link DriveTrainSubsystem} to control.
     */
    public ArcadeDriveStrategy(DriveTrainSubsystem subsystem) {
        m_subsystem = subsystem;
    }

}
