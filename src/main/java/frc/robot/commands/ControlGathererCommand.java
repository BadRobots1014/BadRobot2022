//LOWERS ARM

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GathererSubsystem;


public class ControlGathererCommand extends CommandBase {
    
    private final GathererSubsystem m_subsystem;
    private final double m_speed;
    
    public ControlGathererCommand(GathererSubsystem subsystem, double speed) {
        m_subsystem = subsystem;
        m_speed = speed;

        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_subsystem.runGatherer(m_speed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.stopGatherer();
    }
}
