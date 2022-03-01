package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GathererSubsystem;

/**
 * Deploys the gatherer and starts the collector. On command end,
 * stops the collector.
 */
public class GatherCommand extends CommandBase {
    
    private final GathererSubsystem m_subsystem;
    
    public GatherCommand(GathererSubsystem subsystem) {
        m_subsystem = subsystem;

        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_subsystem.deployGatherer();
        m_subsystem.startCollector();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.stopGatherer();
        m_subsystem.stopCollector();
    }
}
