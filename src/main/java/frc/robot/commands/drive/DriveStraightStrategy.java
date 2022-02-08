package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.subsystems.GyroSubsystem;

/**
 * A {@link DriveStrategy} for driving straight using PID.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class DriveStraightStrategy implements DriveStrategy {

    private final DriveTrainSubsystem m_drive;
    private final PIDController m_rotationalPid;
    /**
     * The {@link GyroSubsystem} to obtain gyroscope readings from.
     */
    private final GyroSubsystem m_gyro;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    @Override
    public String getName() {
        return "Drive Straight";
    }

    @Override
    public void reset() {
        m_gyro.zeroYaw();
    }

    @Override
    public void execute(double x, double power) {
        final double correction = m_rotationalPid.calculate(m_gyro.getYaw());
        m_drive.tankDrive(power - correction, power + correction);
    }

    @Override
    public boolean shouldLockPosition() {
        return false;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

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
        m_drive = drive;
        m_rotationalPid = GyroSubsystem.createRotationalPid(.02, 0, 0);
        m_rotationalPid.setSetpoint(0);
        m_gyro = gyro;
    }

}
