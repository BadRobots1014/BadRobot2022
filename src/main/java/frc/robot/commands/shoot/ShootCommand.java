package frc.robot.commands.shoot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class ShootCommand extends CommandBase {

    /*
     * Private constants ------------------------------------------------------
     */

    /**
     * The amount of time, in seconds, to wait between activating the shooter motor
     * and activating the indexer motor.
     */
    private static final double WAIT_TIME = 1.0;

    /*
     * Private instance members -----------------------------------------------
     */

    /**
     * The {@link ShooterSubsystem} controlled by this command.
     */
    private final ShooterSubsystem shooter;

    /**
     * The {@link IndexerSubsystem} controlled by this command.
     */
    private final IndexerSubsystem indexer;

    /**
     * A {@link Timer} used to implement a delay between activating the shooter
     * motor and activating the indexer motor.
     */
    private final Timer timer;

    /**
     * The percentage output to drive the shooter at.
     */
    private final double power;

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructs a new {@link ShootCommand}, parameterized by the subsystems
     * ({@code shooter}, {@code indexer}) to control, and the shooter power output.
     * 
     * @param shooter the {@link ShooterSubsystem} controlled by this command
     * @param indexer the {@link IndexerSubsystem} controlled by this command
     * @param power   the percentage output to run the shooter at
     * @requires {@code power} is in the range [-1.0, 1.0]
     */
    public ShootCommand(ShooterSubsystem shooter, IndexerSubsystem indexer, double power) {
        this.shooter = shooter;
        this.indexer = indexer;

        this.timer = new Timer();

        this.power = power;

        addRequirements(shooter, indexer);
    }

    /*
     * Command methods --------------------------------------------------------
     */

    @Override
    public void initialize() {
        this.timer.reset();
        this.timer.start();

        this.shooter.run(this.power);
    }

    @Override
    public void execute() {
        this.shooter.run(this.power);

        if (this.timer.hasElapsed(WAIT_TIME)) {
            this.indexer.runUpperMotor();
        }
    }

    @Override
    public void end(boolean interrupted) {
        this.shooter.stop();
        this.indexer.stopUpperMotor();
        System.out.println("Stopping everything");
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
