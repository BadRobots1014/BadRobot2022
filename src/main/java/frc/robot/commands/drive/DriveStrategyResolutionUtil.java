package frc.robot.commands.drive;

/**
 * Utility class to check which {@link DriveStrategy} a given joystick input
 * corresponds to.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public final class DriveStrategyResolutionUtil {

    /**
     * Checks whether or not the given joystick inputs {@code x} and {@code y} fall
     * within the range that corresponds the arcade drive strategy.
     * 
     * @param x The x-axis reading from the joystick.
     * @param y The y-axis reading from the joystick.
     * @return If {@code x} and {@code y} correspond to inputs that should be
     *         interpreted as arcade drive.
     * @author Victor Chen <victorc.1@outlook.com>
     */
    public static boolean isInputForArcadeDrive(double x, double y) {
        // TODO: Implement this method.

        // This line included to make the program compatible.
        return false;
    }

    /*
     * TODO: Write methods similar to the one above to resolve inputs for drive
     * straight, pivot turn, and hold place.
     */

    public static boolean isInputForDriveStraight(double x, double y) {
        // TODO: Implement this method.

        // This line included to make the program compatible.
        return false;
    }

    public static boolean isInputForPivotTurn(double x, double y) {
        // TODO: Implement this method.

        // This line included to make the program compatible.
        return false;
    }

    public static boolean isInputForHoldPlace(double x, double y) {
        // TODO: Implement this method.

        // This line included to make the program compatible.
        return false;
    }

}
