package frc.robot.commands.drive;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.util.GyroUtil;

/**
 * A {@link DriveStrategy} for driving straight using PID.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class DriveStraightStrategy implements DriveStrategy {

    private final DriveTrainSubsystem drive;
    private final PIDController pidController;
    /**
     * The {@link GyroUtil} to obtain gyroscope readings from.
     */
    private final GyroUtil gyro;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Drive Straight";
    }

    @Override
    public void reset() {
        this.pidController.setSetpoint(this.gyro.getYaw());
    }

    @Override
    public void execute(double x, double power) {
        final double correction = this.pidController.calculate(this.gyro.getYaw());
        this.drive.directTankDrive(power - correction, power + correction);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@link DriveStraightStrategy}.
     *
     * @param drive
     * @author Victor Chen <victorc.1@outlook.com>
     * @author Will Blankemeyer
     */
    public DriveStraightStrategy(DriveTrainSubsystem drive) {
        this.drive = drive;
        this.pidController = GyroUtil.createRotationalPid(.02, 0, 0);
        this.gyro = GyroUtil.get();
    }

}
