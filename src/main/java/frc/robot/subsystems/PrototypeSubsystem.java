package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PrototypeConstants;
import frc.robot.prototype.PrototypeCANSparkMax;
import frc.robot.prototype.PrototypeSpeedController;
import frc.robot.prototype.PrototypeTalonFX;

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

    /**
     * A reference to the "Prototype" tab object in Shuffleboard.
     */
    private final ShuffleboardTab m_prototypeTab = Shuffleboard.getTab("Prototype");

    /**
     * The speed controllers used for prototyping.
     */
    private final PrototypeSpeedController[] m_speedControllers = {
        new PrototypeCANSparkMax(new CANSparkMax(PrototypeConstants.kSpeedController1, MotorType.kBrushless)),
        new PrototypeTalonFX(new TalonFX(PrototypeConstants.kSpeedController2))
        //,new PrototypeTalonFX(new TalonFX(PrototypeConstants.kSpeedController3))
        //,new PrototypeTalonFX(new TalonFX(PrototypeConstants.kSpeedController4))
    };

    /**
     * The {@code SendableChooser} for selecting the speed controller to use.
     */
    private final SendableChooser<PrototypeSpeedController> m_speedControllerChooser = new SendableChooser<>();

    /**
     * Constructor.
     */
    public PrototypeSubsystem() {
        // Configure speed controller chooser
        m_speedControllerChooser.addOption("SPARK MAX (ID 7)", m_speedControllers[0]);
        m_speedControllerChooser.addOption("TalonFX (ID 11)", m_speedControllers[1]);
        m_speedControllerChooser.addOption("TalonFX (ID 13)", m_speedControllers[2]);
        //m_speedControllerChooser.addOption("TalonFX (ID 14)", m_speedControllers[3]);

        m_prototypeTab.add("Speed Controller", m_speedControllerChooser).withWidget(BuiltInWidgets.kComboBoxChooser);
    }

    /**
     * Runs the motor selected in the output chooser at the set {@code speed}, which
     * is a value between [-1, 1].
     * 
     * @param speed the speed to run the motor at
     */
    public void run(double speed) {
        m_speedControllerChooser.getSelected().set(speed);
    }

}
