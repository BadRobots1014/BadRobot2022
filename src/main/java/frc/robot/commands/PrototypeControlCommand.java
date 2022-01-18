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

    /**
     * A reference to the {@code PrototypeSubsystem}.
     */
    private final PrototypeSubsystem m_subsystem;

    /**
     * The double-valued function that supplies the speed value [-1, 1] to run
     * the speed controller at.
     */
    private final DoubleSupplier m_speedSupplier;

    /**
     * Constructs a new {@code PrototypeControlCommand}. Each
     * {@code PrototypeControlCommand} can control one motor.
     * 
     * @param subsystem     The {@code PrototypeSubsystem}
     * @param speedSupplier The double-valued method that provides a speed value
     *                      [-1, 1]
     */
    public PrototypeControlCommand(PrototypeSubsystem subsystem, DoubleSupplier speedSupplier) {
        m_subsystem = subsystem;
        m_speedSupplier = speedSupplier;

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
        m_subsystem.run(m_speedSupplier.getAsDouble());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.run(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
