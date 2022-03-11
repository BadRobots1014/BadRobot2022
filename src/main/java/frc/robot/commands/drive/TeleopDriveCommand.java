package frc.robot.commands.drive;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.MathUtil;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.Constants.ControllerConstants;

public class TeleopDriveCommand extends CommandBase {

    private final DriveTrainSubsystem drive;
    private final GyroSubsystem gyro;

    private final Supplier<Double> xSource;
    private final Supplier<Double> ySource;
    private final Supplier<Double> throttleSource;

    private final DriveStrategy arcadeStrategy;
    private final DriveStrategy driveStraightStrategy;
    private final DriveStrategy pivotTurnStrategy;
    private final DriveStrategy anchorStrategy;

    private DriveStrategy strategy;

    private final ShuffleboardTab m_tab;

    private static final double DEADBAND_RADIUS = 0.2;
    private static final double DEADBAND_ANGLE = Math.atan(1.0 / 0.2);

    /*
     * Helper methods ---------------------------------------------------------
     */

    /**
     * Return drive strategy corresponding to the joystick input coordinates
     * {@code x} and {@code y}.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the corresponding drive strategy
     */
    private DriveStrategy getNextDriveStrategy(double x, double y) {
        DriveStrategy result = this.arcadeStrategy;

        if (Math.hypot(x, y) < DEADBAND_RADIUS) {
            result = this.anchorStrategy;
        } else if (Math.abs(Math.atan(y / x)) < DEADBAND_ANGLE) {
            result = this.pivotTurnStrategy;
        } else if (Math.abs(Math.atan(x / y)) < DEADBAND_ANGLE) {
            result = this.driveStraightStrategy;
        }

        return result;
    }

    /**
     * Scales a joystick input coordinate to prevent jerking around the central
     * deadzone and by the throttle factor.
     * 
     * @param coord    the coordinate to scale
     * @param throttle the factor to scale the coordinate by
     * @return the transformed coordinate
     */
    private static double scaleCoordinate(double coord, final double throttle) {
        /*
         * The simplest deadzone implementation simply maps inputs around the
         * origin to zero; however, the problem with that is that there is a
         * "jerk" when transitioning from inside the deadzone to outside. The
         * provided {@code MathUtil.applyDeadBand} method takes care of this
         * issue for us.
         */

        return MathUtil.applyDeadband(coord, ControllerConstants.kDeadzoneRadius) * throttle;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructs a new TeleopDriveCommand.
     * 
     * @param drive          the {@code DriveTrainSubsystem}
     * @param gyro           the {@code GyroSubsystem}
     * @param xSource        supplies the joystick X input
     * @param ySource        supplies the joystick Y input
     * @param throttleSource supplies the throttle factor
     * @requires -1 <= [return value of xSource] <= 1 and -1 <= [return value of
     *           ySource] <= 1 and 0 <= [return value of throttleSource] <= 1
     */
    public TeleopDriveCommand(
            DriveTrainSubsystem drive,
            GyroSubsystem gyro,
            Supplier<Double> xSource,
            Supplier<Double> ySource,
            Supplier<Double> throttleSource) {

        this.drive = drive;
        this.gyro = gyro;

        this.xSource = xSource;
        this.ySource = ySource;
        this.throttleSource = throttleSource;

        this.arcadeStrategy = new ArcadeDriveStrategy(this.drive);
        this.driveStraightStrategy = new DriveStraightStrategy(this.drive, this.gyro);
        this.pivotTurnStrategy = new PivotTurnStrategy(this.drive);
        this.anchorStrategy = new AnchorStrategy(this.drive, this.gyro);

        /*
         * Initialize this.strategy to an anchor strategy. This will be changed anyways
         * if necessary.
         */
        this.strategy = this.anchorStrategy;

        m_tab = Shuffleboard.getTab("Teleop. Drive");
        m_tab.addString("Strategy", this.strategy::getName);
        m_tab.addNumber("X", this.xSource::get);
        m_tab.addNumber("Y", this.ySource::get);
        m_tab.addNumber("Throttle (%)", this.throttleSource::get);
        m_tab.addNumber("Yaw (deg.)", this.gyro::getYaw);

        addRequirements(this.drive, this.gyro);
    }

    /*
     * Command methods --------------------------------------------------------
     */

    @Override
    public void initialize() {
        this.strategy.reset();
    }

    @Override
    public void execute() {
        double throttle = this.throttleSource.get();

        double x = this.xSource.get();
        double y = this.ySource.get();

        DriveStrategy nextStrategy = this.getNextDriveStrategy(x, y);

        if (this.strategy != nextStrategy) {
            nextStrategy.reset();
            this.strategy = nextStrategy;
        }

        /*
         * The use of scaleCoordinate is necessary to eliminate deadzone jerking.
         */
        this.strategy.execute(scaleCoordinate(x, throttle), scaleCoordinate(y, throttle));
    }

    @Override
    public void end(boolean interrupted) {
        this.drive.stop();
    }
}
