package frc.robot.commands.drive;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.drive.DriveTrainSubsystem;

public class FollowTargetCommand extends CommandBase {

    private final DriveTrainSubsystem drive;
    private final VisionSubsystem vision;

    private final Supplier<Double> xSource;
    private final Supplier<Double> ySource;

    private final DriveStrategy strategy;

    /**
     * Construct a new {@command FollowTargetCommand} using the {@code drive} and
     * {@code vision} subsystems, and reading controls input from {@code xSource}
     * and {@code ySource}
     * 
     * @param drive   the {@link DriveTrainSubsystem}
     * @param vision  the {@link VisionSubsystem}
     * @param xSource the source for x-coordinate input
     * @param ySource the source for y-coordinate input
     * @requires -1 <= [return value of xSource] <= 1 and -1 <= [return value of
     *           ySource] <= 1 and 0 <= [return value of throttleSource] <= 1
     */
    public FollowTargetCommand(DriveTrainSubsystem drive, VisionSubsystem vision, Supplier<Double> xSource,
            Supplier<Double> ySource) {
        this.drive = drive;
        this.vision = vision;

        this.xSource = xSource;
        this.ySource = ySource;

        this.strategy = new FollowTargetStrategy(drive, vision);

        addRequirements(this.drive, this.vision);
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
        this.strategy.execute(this.xSource.get(), this.ySource.get());
    }

    @Override
    public void end(boolean interrupted) {
        this.drive.stop();
    }
}
