package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

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

    /*
     * Speed controllers ------------------------------------------------------
     */
    private final CANSparkMax leftA;
    private final CANSparkMax leftB;
    private final CANSparkMax rightA;
    private final CANSparkMax rightB;

    /*
     * Utility objects --------------------------------------------------------
     */

    private final DifferentialDrive driveTrain;

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

        this.leftA = new CANSparkMax(LEFT_A_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.leftB = new CANSparkMax(LEFT_B_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.rightA = new CANSparkMax(RIGHT_A_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.rightB = new CANSparkMax(RIGHT_B_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);

        /*
         * Initialize utility objects
         */

        this.driveTrain = new DifferentialDrive(this.leftA, this.rightA);

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
}
