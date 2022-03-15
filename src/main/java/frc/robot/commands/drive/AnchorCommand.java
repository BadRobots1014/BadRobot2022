package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.drive.DriveTrainSubsystem;

/**
 * PID-based anchor command. This uses PID to help the robot maintain its
 * position.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class AnchorCommand extends CommandBase {

    /*
     * Private members --------------------------------------------------------
     */

    private final DriveStrategy strategy;

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructs a new {@link AnchorCommand}.
     * 
     * @param drive the {@link DriveTrainSubsystem} to control
     * @param gyro  the {@link GyroSubsystem} to use for PID
     */
    public AnchorCommand(DriveTrainSubsystem drive, GyroSubsystem gyro) {
        this.strategy = new AnchorStrategy(drive, gyro);

        addRequirements(drive, gyro);
    }

    /*
     * Command methods --------------------------------------------------------
     */

    @Override
    public void initialize() {
        this.strategy.reset();
    }

    @Override
    public void execute() {
        this.strategy.execute(0, 0);
    }

    @Override
    public void end(boolean isFinished) {

    }

}
