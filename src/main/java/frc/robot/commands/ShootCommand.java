package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.IndexerSubsystem;


public class ShootCommand extends CommandBase {
    
    private final ShooterSubsystem m_subsystem;
    private final IndexerSubsystem m_indexSubsystem;
    
    public ShootCommand(ShooterSubsystem subsystem, IndexerSubsystem indexSubsystem) {
        m_subsystem = subsystem;
        m_indexSubsystem = indexSubsystem;

        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        //m_subsystem.run(0.5);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_subsystem.run(0.5);
        if (Math.abs(m_subsystem.getDeltaDesiredVelocity()) < ShooterConstants.kMaxTolerance) {
            m_indexSubsystem.runUpperMotor();
        }
        // else {
        //     m_indexSubsystem.stopUpperMotor();
        // }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.stop();
        m_indexSubsystem.stopUpperMotor();
        System.out.println("Stopping everything");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
