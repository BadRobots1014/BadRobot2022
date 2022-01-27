package frc.robot.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GathererSubsystem;


public class MoveGathererCommand extends CommandBase {
    
    private final GathererSubsystem m_subsystem;
    private final Encoder m_encoder = new Encoder(0, 1);
    private final double m_speed;
    
    public MoveGathererCommand(GathererSubsystem subsystem, double speed) {
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

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_encoder.getStopped();
    }
}
