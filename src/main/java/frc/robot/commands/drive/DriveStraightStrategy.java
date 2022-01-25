package frc.robot.commands.drive;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.DriveTrainConstants;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

/**
 * A {@link DriveStrategy} that contains the algorithm to drive straight using
 * PID.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class DriveStraightStrategy implements DriveStrategy {

    /**
     * The {@link DriveTrainSubsystem} to control.
     */
    private final DriveTrainSubsystem m_drive;

    /**
     * The {@link GyroSubsystem} to obtain gyroscope readings from.
     */
    private final GyroSubsystem m_gyro;

    /**
     * The {@link PIDController} used to perform PID calculations.
     */
    private final PIDController m_controller;

    /**
     * The {@link DriveStrategyContext} associated with {@code this}.
     */
    private DriveStrategyContext m_context;

    @Override
    public void reset() {
        this.m_gyro.resetYaw();
        this.m_controller.setSetpoint(this.m_gyro.getYaw());
    }

    @Override
    public void execute(double x, double y) {
        final double correction = this.m_controller.calculate(this.m_gyro.getYaw());
        m_drive.tankDrive(y - correction, y + correction);
    }

    @Override
    public void setContext(DriveStrategyContext context) {
        this.m_context = context;
    }

    /**
     * Constructs a new {@code DriveStraightStrategy} that controls the given
     * {@code drive} using gyroscope readings from {@code gyro} for straight
     * driving.
     * 
     * @param drive
     * @param gyro
     * @author Victor Chen <victorc.1@outlook.com>
     * @author Will Blankemeyer
     */
    public DriveStraightStrategy(DriveTrainSubsystem drive, GyroSubsystem gyro) {

        this.m_drive = drive;
        this.m_gyro = gyro;

        this.m_controller = new PIDController(
                DriveTrainConstants.kAngularP,
                DriveTrainConstants.kAngularI,
                DriveTrainConstants.kAngularD);
        this.m_controller.enableContinuousInput(-180, 180);

        this.reset();
    }

}
