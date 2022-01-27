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
public class AnchorStrategy implements DriveStrategy {

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
    private final PIDController m_angularPid;

    /**
     * The {@link PIDController} used to perform PID calculations.
     */
    private final PIDController m_displacementPid;

    /*
     * DriveStrategy interface methods ----------------------------------------
     */

    public String getName() {
        return "Anchor";
    }

    @Override
    public void reset() {
        this.m_gyro.resetYaw();
        this.m_angularPid.setSetpoint(this.m_gyro.getYaw());

        // According to 'AHRS::getDisplacementX' (upon which 'GyroProvider::getDisplacementX' is
        // based), displacement measurement quickly becomes inaccurate over time. Resetting
        // displacment upon every invocation of this command attempts to mitigate this.
        this.m_gyro.resetDisplacement();

        this.m_displacementPid.reset();
        // Note that 'gyro.getDisplacementX()' should return approximately 0.
        this.m_displacementPid.setSetpoint(this.m_gyro.getDisplacementX());
    }

    @Override
    public void execute(double x, double y) {
        double power = this.m_displacementPid.calculate(m_gyro.getDisplacementX());
        double correction = this.m_angularPid.calculate(this.m_gyro.getYaw());
        // m_drive.tankDrive(power - correction, power + correction);
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
    public AnchorStrategy(DriveTrainSubsystem drive, GyroSubsystem gyro) {

        this.m_drive = drive;
        this.m_gyro = gyro;

        this.m_angularPid = new PIDController(
                DriveTrainConstants.kAngularP,
                DriveTrainConstants.kAngularI,
                DriveTrainConstants.kAngularD);
        this.m_angularPid.enableContinuousInput(-180, 180);

        this.m_displacementPid = new PIDController(
            DriveTrainConstants.kDisplacementP,
            DriveTrainConstants.kDisplacementI,
            DriveTrainConstants.kDisplacementD
        );

        this.reset();
    }

}
