package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;

/**
 * A {@link DriveStrategy} for arcade-driving.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class ArcadeDriveStrategy implements DriveStrategy {

    private final DriveTrainSubsystem m_drive;

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
    public void execute(double rotation, double power) {
        m_drive.arcadeDrive(power, rotation);
    }

    @Override
    public boolean shouldLockPosition() {
        return false;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@link ArcadeDriveStrategy}.
     */
    public ArcadeDriveStrategy(DriveTrainSubsystem drive) {
        m_drive = drive;
    }

}
