package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;

/**
 * Interface that defines strategies for driving control.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public interface DriveStrategy {

    public String getName();

    /**
     * Resets the strategy to an initial state.
     */
    public void reset();

    /**
     * Does the thing.
     *
     * @param x The x-axis reading from the joystick
     * @param y The y-axis reading from the joystick
     * @author Victor Chen <victorc.1@outlook.com>
     * @author Will Blankemeyer
     */
    public WheelSpeeds execute(double x, double y);

    public boolean shouldLockPosition();

}
