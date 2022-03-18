package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.GyroUtil;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

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

    /**
     * Diameter of the wheels. in meters.
     */
    private static final double WHEEL_DIAMETER = 0.2032;

    /**
     * 
     * Gear ratio of the motors.
     */
    private static final double GEAR_RATIO = 10.75;

    /**
     * The conversion factor from revolutions of the motor, as reported by the
     * encoder, to distance covered by the wheels.
     */
    private static final double ENCODER_CONVERSION_FACTOR = Math.PI * WHEEL_DIAMETER / GEAR_RATIO;

    /**
     * The proportional coefficient for PID velocity control of drive train motors.
     */
    private static final double PID_PROPORTIONAL_COEFFICIENT = 0.0022395;

    /**
     * The required voltage to overcome static friction, in V, for feedforward
     * control.
     * 
     * This is the coefficient kS in the following equation:
     * V = kS * sgn(v) + kV * v + kA * a
     * 
     * @see <a href=
     *      "https://docs.wpilib.org/en/stable/docs/software/pathplanning/system-identification/introduction.html">Introduction
     *      to System Identification</a>
     */
    private static final double FEEDFORWARD_STATIC_COEFFICIENT = 0.49638;

    /**
     * The proportionality constant of the velocity term, in V*s/m, for feedforward
     * control.
     * 
     * This is the coefficient kV in the following equation:
     * V = kS * sgn(v) + kV * v + kA * a
     * 
     * @see <a href=
     *      "https://docs.wpilib.org/en/stable/docs/software/pathplanning/system-identification/introduction.html">Introduction
     *      to System Identification</a>
     */
    private static final double FEEDFORWARD_VELOCITY_COEFFICIENT = 2.3537;

    /**
     * The proportionality constant of the acceleration term, in V*s^2/m, for
     * feedforward control.
     * 
     * This is the coefficient kA in the following equation:
     * V = kS * sgn(v) + kV * v + kA * a
     * 
     * @see <a href=
     *      "https://docs.wpilib.org/en/stable/docs/software/pathplanning/system-identification/introduction.html">Introduction
     *      to System Identification</a>
     */
    private static final double FEEDFORWARD_ACCELERATION_COEFFICIENT = 0.58533;

    /**
     * The maximum speed of the robot when using feedforward control, in m/s.
     */
    private static final double MAX_SPEED = 2.80;

    // TODO: Measure the actual maximum angular speed

    /**
     * The maximum rotational speed of the robot when using feedforward control,
     * in rad/s.
     */
    private static final double MAX_ANGULAR_SPEED = 1.5 * Math.PI;

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
     * PID Controllers --------------------------------------------------------
     */
    private final SparkMaxPIDController leftPIDController;
    private final SparkMaxPIDController rightPIDController;

    /*
     * Utility objects --------------------------------------------------------
     */

    private final DifferentialDrive driveTrain;

    private final GyroUtil gyro;
    private final DifferentialDriveOdometry odometry;

    private final SimpleMotorFeedforward feedforward;
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

    /**
     * Set drive train speeds using feedforward control.
     * 
     * @param left  the speed of the left wheels, in m/s
     * @param right the speed of the right wheels, in m/s
     */
    private void setSpeeds(double left, double right) {
        this.leftPIDController.setReference(this.feedforward.calculate(left), ControlType.kVelocity);
        this.rightPIDController.setReference(this.feedforward.calculate(right), ControlType.kVelocity);
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

        this.leftEncoder.setPositionConversionFactor(ENCODER_CONVERSION_FACTOR);
        this.rightEncoder.setPositionConversionFactor(ENCODER_CONVERSION_FACTOR);

        /*
         * Initialize PID controllers
         */
        this.leftPIDController = this.leftA.getPIDController();
        this.rightPIDController = this.rightA.getPIDController();

        this.leftPIDController.setP(PID_PROPORTIONAL_COEFFICIENT);
        this.rightPIDController.setP(PID_PROPORTIONAL_COEFFICIENT);

        /*
         * Initialize utility objects
         */

        this.driveTrain = new DifferentialDrive(this.leftA, this.rightA);

        this.gyro = GyroUtil.get();
        this.odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(-this.gyro.getYaw()));

        this.feedforward = new SimpleMotorFeedforward(FEEDFORWARD_STATIC_COEFFICIENT, FEEDFORWARD_VELOCITY_COEFFICIENT,
                FEEDFORWARD_ACCELERATION_COEFFICIENT);
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

        this.shuffleTab.addNumber("Odometry X Position", this::getX);
        this.shuffleTab.addNumber("Odometry Y Position", this::getY);
        this.shuffleTab.addNumber("Odometry Rotation", this::getRotation);
    }

    /*
     * Exposed control methods ------------------------------------------------
     */

    /**
     * Drive using speeds parameterized in a {@link ChassisSpeeds} object.
     * 
     * @param speeds the {@link ChassisSpeeds} object
     */
    public void parametricDrive(ChassisSpeeds speeds) {
        DifferentialDriveWheelSpeeds wheelSpeeds = this.kinematics.toWheelSpeeds(speeds);
        this.setSpeeds(wheelSpeeds.leftMetersPerSecond, wheelSpeeds.rightMetersPerSecond);
    }

    /**
     * Drive using arcade-drive, or using motion parameterized by percentage
     * tangential (forwards and backwards) and rotational power, using feedforward
     * control.
     * 
     * @param speed    the tangential power
     * @param rotation the rotational power, positive is clockwise
     * @requires (-1 <= speed <= 1) and (-1 <= rotation <= 1)
     */
    public void arcadeDrive(double speed, double rotation) {
        double tanSpeed = Math.pow(speed, 2) * MAX_SPEED;
        double rotSpeed = -Math.pow(rotation, 2) * MAX_ANGULAR_SPEED;
        this.parametricDrive(new ChassisSpeeds(tanSpeed, 0, rotSpeed));
    }

    /**
     * Drive using tank-drive, or motion parameterized by percentage power provided
     * to left and right wheels, using feedforward control.
     * 
     * @param leftSpeed  the power provided to the left wheels
     * @param rightSpeed the power provided to the right wheels
     * @requires (-1 <= leftSpeed <= 1) and (-1 <= rightSpeed <= 1)
     */
    public void tankDrive(double leftSpeed, double rightSpeed) {
        this.setSpeeds(leftSpeed * MAX_SPEED, rightSpeed * MAX_SPEED);
    }

    /**
     * Drive using arcade-drive, or using motion parameterized by percentage
     * tangential (forwards and backwards) and rotational power, without feedforward
     * control.
     * 
     * @param speed    the tangential power
     * @param rotation the rotational power, positive is clockwise
     * @requires (-1 <= speed <= 1) and (-1 <= rotation <= 1)
     */
    public void directArcadeDrive(double speed, double rotation) {
        this.driveTrain.arcadeDrive(speed, rotation, true);
    }

    /**
     * Drive using tank-drive, or motion parameterized by percentage power provided
     * to left and right wheels, without feedforward control.
     * 
     * @param leftSpeed  the power provided to the left wheels
     * @param rightSpeed the power provided to the right wheels
     * @requires (-1 <= leftSpeed <= 1) and (-1 <= rightSpeed <= 1)
     */
    public void directTankDrive(double leftSpeed, double rightSpeed) {
        this.driveTrain.tankDrive(clampPower(leftSpeed), clampPower(rightSpeed), true);
    }

    /**
     * Stop all drive train motors.
     */
    public void stop() {
        this.leftPIDController.setReference(0, ControlType.kDutyCycle);
        this.rightPIDController.setReference(0, ControlType.kDutyCycle);
        this.driveTrain.stopMotor();
    }

    /*
     * Exposed odometry methods -----------------------------------------------
     */

    /**
     * Get the position of the robot, in meters, in a {@link Pose2d} object.
     * 
     * @return the {@link Pose2d} object parameterizing the robot's position
     */
    public Pose2d getPose() {
        return this.odometry.getPoseMeters();
    }

    /**
     * Get the x position, as reported by {@link #odometry}, in meters.
     * 
     * @return the x position
     */
    public double getX() {
        return this.odometry.getPoseMeters().getX();
    }

    /**
     * Get the y position, as reported by {@link #odometry}, in meters.
     * 
     * @return the y position
     */
    public double getY() {
        return this.odometry.getPoseMeters().getY();
    }

    /**
     * Get the rotation, as reported by {@link #odometry}, in degrees.
     * 
     * @return the rotation
     */
    public double getRotation() {
        return this.odometry.getPoseMeters().getRotation().getDegrees();
    }

    /*
     * SubsystemBase methods --------------------------------------------------
     */

    @Override
    public void periodic() {
        updateOdometry();
    }

}
