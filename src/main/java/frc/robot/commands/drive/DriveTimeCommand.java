package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

/**
 * This command is used to drive forward a set distance.
 * 
 */
public class DriveTimeCommand extends CommandBase {

    /*
     * Private constants ------------------------------------------------------
     */

    /**
     * The power to drive the robot at.
     */
    private final double POWER = 0.5;

    /**
     * The speed of the robot when driven at {@link #POWER}.
     */
    //private final double SPEED = 5.0;

    /*
     * Private members --------------------------------------------------------
     */

    private final DriveTrainSubsystem drive;
    private final GyroSubsystem gyro;

    private final Timer timer;

    private final boolean forwards;
    private final double time;

    private final DriveStrategy strategy;

    /*
     * Private helper methods -------------------------------------------------
     */

    /**
     * Calculates the time required to move a certain distance at a certain speed.
     * 
     * @param distance the distance to traverse
     * @param speed    the speed to move at
     * @return the amount of time it takes to move {@code distance} at {@code speed}
     * @requires distance >= 0 and speed > 0
     */
    // private static double calculateTime(double distance, double speed) {
    //     return Math.abs(distance) / speed;
    // }

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructs a new {@link DriveTimeCommand}.
     * 
     * @param subsystem the {@link DriveTrainSubsystem} to control
     * @param gyro      the {@link GyroSubsystem} to read from
     * @param distance  the distance (in what units???) to move; positive is
     *                  forwards
     */
    public DriveTimeCommand(DriveTrainSubsystem drive, GyroSubsystem gyro, double time) {
        this.drive = drive;
        this.gyro = gyro;

        this.timer = new Timer();

        this.forwards = true;
        this.time = time;
        //this.time = 2.0;

        this.strategy = new DriveStraightStrategy(drive, gyro);

        addRequirements(this.drive, this.gyro);
    }

    @Override
    public void initialize() {
        this.strategy.reset();
        this.timer.reset();
        this.timer.start();
        // System.out.println("DEBUG: DriveDistance Init finished");
    }

    @Override
    public void execute() {
        // System.out.println("DEBUG: DriveDistance Execute");
        if (this.forwards) {
            this.strategy.execute(0, POWER);
        } else {
            this.strategy.execute(0, -POWER);
        }
    }

    @Override
    public void end(boolean interrupted) {
        // System.out.println("DEBUG: DriveDistance End");
        this.drive.stop();
        this.timer.stop();
    }

    @Override
    public boolean isFinished() {
        return this.timer.hasElapsed(this.time);
    }
}
