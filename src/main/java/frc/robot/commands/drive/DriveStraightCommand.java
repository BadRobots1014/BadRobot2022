package frc.robot.commands.drive;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.DriveTrainSubsystem;

/**
 * PID drive-straight commmand.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class DriveStraightCommand extends CommandBase {

    /*
     * Private members --------------------------------------------------------
     */

    private final DriveTrainSubsystem drive;

    private final Supplier<Double> power;

    private final DriveStrategy strategy;

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructs a new {@link DriveStraightCommand}.
     * 
     * @param drive the {@link DriveTrainSubsystem} to control
     * @param power the percentage power to drive at
     * @requires -1 <= [the return value of power] <= 1
     */
    public DriveStraightCommand(DriveTrainSubsystem drive, Supplier<Double> power) {
        this.drive = drive;
        this.power = power;
        this.strategy = new DriveStraightStrategy(drive);

        addRequirements(drive);
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
        this.strategy.execute(0, this.power.get());
    }

    @Override
    public void end(boolean isFinished) {
        this.drive.stop();
    }

}
