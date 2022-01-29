package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.commands.drive.DriveStrategy;
import frc.robot.commands.drive.PivotTurnStrategy;
import frc.robot.commands.drive.AnchorStrategy;
import frc.robot.commands.drive.ArcadeDriveStrategy;
import frc.robot.commands.drive.DriveStraightStrategy;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

public class DriveStrategyCommand extends CommandBase {
    private final DriveStrategy strategy;
    private final DriveTrainSubsystem drive;
    private final Supplier<Double> xSource;
    private final Supplier<Double> ySource;

    public static DriveStrategyCommand anchor(
        final DriveTrainSubsystem drive,
        final GyroSubsystem gyro
    ) {
        final DriveStrategyCommand self = new DriveStrategyCommand(
            new AnchorStrategy(gyro),
            drive,
            () -> 0.0,
            () -> 0.0
        );
        self.addRequirements(gyro);

        return self;
    }

    public static DriveStrategyCommand arcadeDrive(
        final DriveTrainSubsystem drive,
        final Supplier<Double> powerSource,
        final Supplier<Double> rotationSource
    ) {
        return new DriveStrategyCommand(
            new ArcadeDriveStrategy(),
            drive,
            rotationSource,
            powerSource
        );
    }

    /**
     * Returns a command that tank-drives with a supplied power while maintaining the initial yaw
     * angle.
     *
     * @param drive         The {@link DriveTrainSubsystem}.
     * @param gyro          The {@link GyroSubsystem}.
     * @param powerSource   A supplier whose output value is in [-1.0, 1.0].
     */
    public static DriveStrategyCommand driveStraight(
        final DriveTrainSubsystem drive,
        final GyroSubsystem gyro,
        final Supplier<Double> powerSource
    ) {
        final DriveStrategyCommand self = new DriveStrategyCommand(
            new DriveStraightStrategy(gyro),
            drive,
            () -> 0.0,
            powerSource
        );
        self.addRequirements(gyro);

        return self;
    }

    public static DriveStrategyCommand pivotTurn(
        final DriveTrainSubsystem drive,
        final Supplier<Double> rotationSource
    ) {
        return new DriveStrategyCommand(
            new PivotTurnStrategy(),
            drive,
            rotationSource,
            () -> 0.0
        );
    }

    private DriveStrategyCommand(
        final DriveStrategy strategy,
        final DriveTrainSubsystem drive,
        final Supplier<Double> xSource,
        final Supplier<Double> ySource
    ) {
        this.strategy = strategy;
        this.drive = drive;
        this.xSource = xSource;
        this.ySource = ySource;

        super.addRequirements(drive);
    }

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
