package frc.robot.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PrototypeSubsystem;

/**
 * Do not use this in production! This command enables control of the various
 * speed controllers in PrototypeSubsystem and is to be used only for prototype
 * testing.
 * 
 * @author Victor Chen <victorc.1@outlook.com>
 */
public class PrototypeControlCommand extends CommandBase {

    private final PrototypeSubsystem m_subsystem;

    private final int m_motorID;

    private final DoubleSupplier m_speedSupplier;

    /**
     * Constructs a new {@code PrototypeControlCommand}. Each
     * {@code PrototypeControlCommand} can control one motor.
     * 
     * @param subsystem     The {@code PrototypeSubsystem}
     * @param motor         The id of the motor to control
     * @param speedSupplier The double-valued method that provides a speed value
     *                      [-1, 1]
     */
    public PrototypeControlCommand(PrototypeSubsystem subsystem, int motor, DoubleSupplier speedSupplier) {
        m_subsystem = subsystem;
        m_speedSupplier = speedSupplier;
        m_motorID = motor;

        // This line required for WPILib.
        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        switch (m_motorID) {
            case 1:
                m_subsystem.runMotor1(m_speedSupplier.getAsDouble());
            case 2:
                m_subsystem.runMotor2(m_speedSupplier.getAsDouble());
            case 3:
                m_subsystem.runMotor3(m_speedSupplier.getAsDouble());
            case 4:
                m_subsystem.runMotor4(m_speedSupplier.getAsDouble());
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        switch (m_motorID) {
            case 1:
                m_subsystem.runMotor1(0);
            case 2:
                m_subsystem.runMotor2(0);
            case 3:
                m_subsystem.runMotor3(0);
            case 4:
                m_subsystem.runMotor4(0);
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
