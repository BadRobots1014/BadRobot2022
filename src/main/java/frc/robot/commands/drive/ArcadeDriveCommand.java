package frc.robot.commands.drive;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.DriveTrainSubsystem;

/**
 * Arcade drive command.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class ArcadeDriveCommand extends CommandBase {

    /*
     * Private members --------------------------------------------------------
     */

    private final DriveTrainSubsystem drive;

    private final Supplier<Double> power;

    private final Supplier<Double> rotation;

    private final DriveStrategy strategy;

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructs a new {@link ArcadeDriveCommand}.
     * 
     * @param drive the {@link DriveTrainSubsystem} to control
     * @param power the percentage power to drive at
     * @param rotation the rotation speed to drive at; positive values correspond to clockwise rotation 
     * @requires ( -1 <= [the return value of power] <= 1 ) and ( -1 <= [the return value of rotation] <= 1 )
     */
    public ArcadeDriveCommand(DriveTrainSubsystem drive, Supplier<Double> power, Supplier<Double> rotation) {
        this.drive = drive;
        this.power = power;
        this.rotation = rotation;
        this.strategy = new ArcadeDriveStrategy(drive);

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
        this.strategy.execute(this.power.get(), this.rotation.get());
    }

    @Override
    public void end(boolean isFinished) {
        this.drive.stop();
    }

}
