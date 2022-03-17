package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.GyroUtil;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

/**
 * Represents the drive train subsystem and exposes methods to control it.
 */
public class DriveTrainSubsystem extends SubsystemBase {
    /*
     * Private constants ------------------------------------------------------
     */
    private static final int LEFT_A_PORT = 12;
    private static final int LEFT_B_PORT = 4;
    private static final int RIGHT_A_PORT = 2;
    private static final int RIGHT_B_PORT = 3;

    /**
     * The track width of the robot, in meters.
     */
    private static final double TRACK_WIDTH = 0.60325;

    /*
     * Speed controllers ------------------------------------------------------
     */
    private final CANSparkMax leftA;
    private final CANSparkMax leftB;
    private final CANSparkMax rightA;
    private final CANSparkMax rightB;

    /*
     * Encoders ---------------------------------------------------------------
     */
    private final RelativeEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;

    /*
     * Utility objects --------------------------------------------------------
     */

    private final DifferentialDrive driveTrain;

    private final GyroUtil gyro;
    private final DifferentialDriveOdometry odometry;

    private final DifferentialDriveKinematics kinematics;

    /*
     * Shuffleboard -----------------------------------------------------------
     */

    private final ShuffleboardTab shuffleTab;

    /*
     * Private helper methods -------------------------------------------------
     */

    /**
     * Clamps {@code power} between -1 and 1.
     * 
     * @param power the value to clamp
     * @return {@code power} clamped between -1 and 1
     */
    private static double clampPower(double power) {
        return MathUtil.clamp(power, -1.0, 1.0);
    }

    /**
     * Update odometry.
     */
    private void updateOdometry() {
        this.odometry.update(Rotation2d.fromDegrees(-this.gyro.getYaw()), this.leftEncoder.getPosition(),
                this.rightEncoder.getPosition());
    }

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructs a new {@link DriveTrainSubsystem}.
     */
    public DriveTrainSubsystem() {
        /*
         * Initialize speed controllers
         */

        this.leftA = new CANSparkMax(LEFT_A_PORT, MotorType.kBrushless);
        this.leftB = new CANSparkMax(LEFT_B_PORT, MotorType.kBrushless);
        this.rightA = new CANSparkMax(RIGHT_A_PORT, MotorType.kBrushless);
        this.rightB = new CANSparkMax(RIGHT_B_PORT, MotorType.kBrushless);

        /*
         * Initialize encoders
         */
        this.leftEncoder = this.leftA.getEncoder();
        this.rightEncoder = this.rightA.getEncoder();

        /*
         * Initialize utility objects
         */

        this.driveTrain = new DifferentialDrive(this.leftA, this.rightA);

        this.gyro = GyroUtil.get();
        this.odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(-this.gyro.getYaw()));

        this.kinematics = new DifferentialDriveKinematics(TRACK_WIDTH);

        /*
         * Configure speed controllers
         */

        this.leftA.setInverted(true);

        this.leftB.follow(this.leftA, false);
        this.rightB.follow(this.rightA, false);

        /*
         * Shuffleboard setup
         */

        this.shuffleTab = Shuffleboard.getTab("Drivetrain");

        this.shuffleTab.addNumber("Left Power", this.leftA::get);
        this.shuffleTab.addNumber("Right Power", this.rightA::get);
    }

    /*
     * Exposed control methods ------------------------------------------------
     */

    public void arcadeDrive(double speed, double rotation) {
        this.driveTrain.arcadeDrive(speed, rotation, true);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        this.driveTrain.tankDrive(clampPower(leftSpeed), clampPower(rightSpeed), true);
    }

    public void stop() {
        this.driveTrain.stopMotor();
    }

    /*
     * SubsystemBase methods --------------------------------------------------
     */

    @Override
    public void periodic() {
        updateOdometry();
    }

}
