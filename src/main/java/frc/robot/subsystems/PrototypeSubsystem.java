package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Do not use this in production! This subsystem creates bindings to control
 * arbitrary speed controllers. Currently, this is being used so that the four
 * speed controllers that are not connected to anything can be controlled via
 * joysticks so that the mechanical teams are able to use it to prototype their
 * designs.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class PrototypeSubsystem extends SubsystemBase {

    private final CANSparkMax m_motor1 = new CANSparkMax(7, MotorType.kBrushless);
    private final TalonFX m_motor2 = new TalonFX(11);
    private final TalonFX m_motor3 = new TalonFX(13);
    private final TalonFX m_motor4 = new TalonFX(14);

    /**
     * Constructor.
     */
    public PrototypeSubsystem() {

    }

    /**
     * Runs motor 1, the SPARK MAX, at the set {@code speed}, which is a value
     * between [-1, 1].
     * 
     * @param speed the speed to run the motor at
     */
    public void runMotor1(double speed) {
        m_motor1.set(speed);
    }

    /**
     * Runs motor 2, a TalonFX, at the set {@code speed}, which is a value between
     * [-1, 1].
     * 
     * @param speed the speed to run the motor at
     */
    public void runMotor2(double speed) {
        m_motor2.set(TalonFXControlMode.PercentOutput, speed);
    }

    /**
     * Runs motor 3, a TalonFX, at the set {@code speed}, which is a value between
     * [-1, 1].
     * 
     * @param speed the speed to run the motor at
     */
    public void runMotor3(double speed) {
        m_motor3.set(TalonFXControlMode.PercentOutput, speed);
    }

    /**
     * Runs motor 4, a TalonFX, at the set {@code speed}, which is a value between
     * [-1, 1].
     * 
     * @param speed the speed to run the motor at
     */
    public void runMotor4(double speed) {
        m_motor4.set(TalonFXControlMode.PercentOutput, speed);
    }

}
