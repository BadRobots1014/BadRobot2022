package frc.robot.commands;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.drive.ArcadeDriveStrategy;
import frc.robot.commands.drive.DriveStraightStrategy;
import frc.robot.commands.drive.DriveStrategy;
import frc.robot.commands.drive.DriveStrategyContext;
import frc.robot.commands.drive.DriveStrategyResolutionUtil;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

public class TeleopDriveCommand extends CommandBase implements DriveStrategyContext {
    private final DriveTrainSubsystem m_drive;
    private final GyroSubsystem m_gyro;
    private final Supplier<Double> m_xSource;
    private final Supplier<Double> m_ySource;

    public final DriveStrategy m_arcadeStrategy;
    public final DriveStrategy m_driveStraightStrategy;
    public final DriveStrategy m_pivotTurnStrategy;
    public final DriveStrategy m_holdPlaceStrategy;

    private DriveStrategy m_strategy;

    public TeleopDriveCommand(DriveTrainSubsystem drive, GyroSubsystem gyro, Supplier<Double> xSource,
            Supplier<Double> ySource) {

        m_drive = drive;
        m_gyro = gyro;
        m_xSource = xSource;
        m_ySource = ySource;

        m_arcadeStrategy = new ArcadeDriveStrategy(m_drive);
        m_driveStraightStrategy = new DriveStraightStrategy(m_drive, m_gyro);

        // TODO: Create PivotTurnStrategy and HoldPlaceStrategy.

        // These lines to make the program compatible.
        m_pivotTurnStrategy = null;
        m_holdPlaceStrategy = null;

        addRequirements(drive);
        addRequirements(gyro);
    }

    /**
     * Sets the strategy that will be used to interpret controls to the given
     * {@code strategy}.
     * 
     * @param strategy The new strategy to use
     * @author Victor Chen <victorc.1@outlook.com>
     */
    public void setStrategy(DriveStrategy strategy) {
        /*
         * When we change strategies, we want to reset the new strategy object to an
         * initial state.
         */
        strategy.reset();
        this.m_strategy = strategy;
    }

    public DriveStrategy getNextDriveStrategy() {
        /*
         * For now, let's just default to ArcadeDriveStrategy.
         */
        DriveStrategy next = this.m_arcadeStrategy;

        if (DriveStrategyResolutionUtil.isInputForDriveStraight(this.m_xSource.get(), this.m_ySource.get())) {
            next = this.m_driveStraightStrategy;
        }

        if (DriveStrategyResolutionUtil.isInputForPivotTurn(this.m_xSource.get(), this.m_ySource.get())) {
            next = this.m_pivotTurnStrategy;
        }

        if (DriveStrategyResolutionUtil.isInputForHoldPlace(this.m_xSource.get(), this.m_ySource.get())) {
            next = this.m_holdPlaceStrategy;
        }

        return next;
    }

    /*
     * Command methods --------------------------------------------------------
     */

    @Override
    public void execute() {
        /*
         * Execute the strategy set in m_strategy.
         */
        m_strategy.execute(m_xSource.get(), m_ySource.get());
    }

    @Override
    public void end(boolean interrupted) {

    }
}
