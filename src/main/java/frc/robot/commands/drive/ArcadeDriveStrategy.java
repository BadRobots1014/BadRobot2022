package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;

/**
 * A {@link DriveStrategy} that contains the algorithm to drive with arcade
 * drive.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public final class ArcadeDriveStrategy implements DriveStrategy {

    /**
     * The DriveTrainSubsystem to control.
     */
    private final DriveTrainSubsystem m_subsystem;

    /**
     * The {@link DriveStrategyContext} associated with {@code this}.
     */
    private DriveStrategyContext m_context;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public void reset() {
        // Do nothing.
    }

    @Override
    public void execute(double x, double y) {
        /*
         * Depending on what the current joystick input is, we may want to execute a
         * state change.
         */
        if (DriveStrategyResolutionUtil.isInputForArcadeDrive(x, y)) {
            this.m_subsystem.arcadeDrive(y, x);
        } else {
            this.m_context.setStrategy(this.m_context.getNextDriveStrategy());
        }
    }

    @Override
    public void setContext(DriveStrategyContext context) {
        this.m_context = context;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@code ArcadeDriveStrategy} to control the given
     * {@code subsystem}.
     * 
     * @param subsystem The {@link DriveTrainSubsystem} to control.
     */
    public ArcadeDriveStrategy(DriveTrainSubsystem subsystem) {
        m_subsystem = subsystem;
    }

}
