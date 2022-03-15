package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.commands.drive.DriveStraightStrategy;
import frc.robot.commands.drive.DriveStrategy;
import frc.robot.subsystems.GyroSubsystem;

/**
 * This command is used to drive forward a set distance.
 * 
 */
public class DriveDistanceCommand extends CommandBase {

    /*
     * Private constants ------------------------------------------------------
     */

    /**
     * The power to drive the robot at.
     */
    private final double POWER = 0.3;

    /**
     * The speed of the robot when driven at {@link #POWER}.
     */
    private final double SPEED = 5.0; // TODO: Measure what this should be

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
    private static double calculateTime(double distance, double speed) {
        return distance / speed;
    }

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructs a new {@link DriveDistanceCommand}.
     * 
     * @param subsystem the {@link DriveTrainSubsystem} to control
     * @param gyro      the {@link GyroSubsystem} to read from
     * @param distance  the distance (in what units???) to move; positive is
     *                  forwards
     */
    public DriveDistanceCommand(DriveTrainSubsystem drive, GyroSubsystem gyro, double distance) {
        this.drive = drive;
        this.gyro = gyro;

        this.timer = new Timer();

        this.forwards = distance >= 0;
        this.time = calculateTime(distance, SPEED);

        this.strategy = new DriveStraightStrategy(drive, gyro);

        addRequirements(this.drive, this.gyro);
    }

    @Override
    public void initialize() {
        this.strategy.reset();
        this.timer.reset();
    }

    @Override
    public void execute() {
        if (this.forwards) {
            this.strategy.execute(0, POWER);
        } else {
            this.strategy.execute(0, -POWER);
        }
    }

    @Override
    public void end(boolean interrupted) {
        this.drive.stop();
    }

    @Override
    public boolean isFinished() {
        return this.timer.get() > this.time;
    }
}
