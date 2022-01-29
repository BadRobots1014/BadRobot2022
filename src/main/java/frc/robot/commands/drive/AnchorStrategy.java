package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

/**
 * A {@link DriveStrategy} that contains the algorithm to drive straight using
 * PID.
 *
 * @author Victor Chen <victorc.1@outlook.com>
 * @author Will Blankemeyer
 */
public class AnchorStrategy extends DriveStraightStrategy {

    /**
     * The {@link GyroSubsystem} to obtain gyroscope readings from.
     */
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
        super.reset();
        m_gyro.zeroDisplacement();
    }

    @Override
    public void execute(double x, double y) {
        super.execute(0, m_gyro.getPositionalPid());
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
        super(drive, gyro);
        m_gyro = gyro;
    }

}
