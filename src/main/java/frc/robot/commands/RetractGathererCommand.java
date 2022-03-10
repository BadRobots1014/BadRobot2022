//LOWERS ARM

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GathererSubsystem;

/**
 * Retracts the gatherer from deployed position to stored position.
 */
public class RetractGathererCommand extends CommandBase {
    
    private final GathererSubsystem m_subsystem;
    
    public RetractGathererCommand(GathererSubsystem subsystem) {
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
        m_subsystem.retractGatherer();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.stopGatherer();
    }
}
