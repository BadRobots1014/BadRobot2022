package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GathererSubsystem;

/**
 * Retracts the gatherer from stored position to deployed position.
 */
public class DeployGathererCommand extends CommandBase {
    
    private final GathererSubsystem m_subsystem;
    
    public DeployGathererCommand(GathererSubsystem subsystem) {
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
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.retractGatherer();
    }
}
