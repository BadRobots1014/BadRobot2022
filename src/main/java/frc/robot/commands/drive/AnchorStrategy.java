package frc.robot.commands.drive;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.subsystems.drive.DriveTrainSubsystem;
import frc.robot.util.GyroUtil;

/**
 * A {@link DriveStrategy} for anchoring the robot, or mainting the current
 * angle and position, using PID.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class AnchorStrategy implements DriveStrategy {

    private final DriveTrainSubsystem drive;
    private final PIDController pidController;
    private final GyroUtil gyro;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Anchor";
    }

    @Override
    public void reset() {
        this.drive.stop();
        this.pidController.setSetpoint(this.gyro.getYaw());
    }

    @Override
    public void execute(double x, double y) {
        final double correction = this.pidController.calculate(this.gyro.getYaw());
        this.drive.directTankDrive(-correction, correction);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@link AnchorStrategy}.
     *
     * @param drive the {@link DriveTrainSubsystem} to control
     * @author Victor Chen <victorc.1@outlook.com>
     * @author Will Blankemeyer
     */
    public AnchorStrategy(DriveTrainSubsystem drive) {
        this.drive = drive;
        this.pidController = GyroUtil.createRotationalPid(0.02, 0, 0);
        this.gyro = GyroUtil.get();
    }

}
