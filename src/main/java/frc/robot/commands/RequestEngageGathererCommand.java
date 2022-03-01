package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GathererSubsystem;


public class RequestEngageGathererCommand extends CommandBase {
    
    private final GathererSubsystem subsystem;
    
    public RequestEngageGathererCommand(GathererSubsystem subsystem) {
        this.subsystem = subsystem;

        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        subsystem.requestEngage();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        subsystem.requestDisengage();
    }
}
