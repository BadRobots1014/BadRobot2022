package frc.robot.commands.drive;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.DriveTrainSubsystem;

/**
 * Pivot turning command.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class PivotCommand extends CommandBase {

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
     * Constructs a new {@link PivotCommand}.
     * 
     * @param drive the {@link DriveTrainSubsystem} to control
     * @param power the percentage power to pivot at; positive values correspond to
     *              clockwise rotation
     * @requires -1 <= [the return value of power] <= 1
     */
    public PivotCommand(DriveTrainSubsystem drive, Supplier<Double> power) {
        this.drive = drive;
        this.power = power;
        this.strategy = new PivotTurnStrategy(drive);

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
        this.strategy.execute(this.power.get(), 0);
    }

    @Override
    public void end(boolean isFinished) {
        this.drive.stop();
    }

}
