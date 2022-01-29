package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;

/**
 * A {@link DriveStrategy} for arcade-driving.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class ArcadeDriveStrategy implements DriveStrategy {

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
    public WheelSpeeds execute(double rotation, double power) {
        return DifferentialDrive.arcadeDriveIK(power, rotation, false);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@link ArcadeDriveStrategy}.
     */
    public ArcadeDriveStrategy() {}

}
