package frc.robot.commands.drive;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

/**
 * A {@link DriveStrategy} for anchoring the robot, or mainting the current angle and position,
 * using PID.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class AnchorStrategy implements DriveStrategy {

    private final DriveTrainSubsystem m_drive;
    private final PIDController m_rotationalPid;
    private final GyroSubsystem m_gyro;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Anchor";
    }

    @Override
    public void reset() {
        m_drive.stop();
        m_gyro.zeroYaw();
    }

    @Override
    public void execute(double x, double y) {
        final double correction = m_rotationalPid.calculate(m_gyro.getYaw());
        m_drive.tankDrive(-correction, correction);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new {@link AnchorStrategy} that controls the given
     * {@code drive} using gyroscope readings from {@code gyro} for straight
     * driving.
     *
     * @param drive
     * @param gyro
     * @author Victor Chen <victorc.1@outlook.com>
     * @author Will Blankemeyer
     */
    public AnchorStrategy(DriveTrainSubsystem drive, GyroSubsystem gyro) {
        m_drive = drive;
        m_rotationalPid = GyroSubsystem.createRotationalPid(0.02, 0, 0);
        m_rotationalPid.setSetpoint(0);
        m_gyro = gyro;
    }

}
