package frc.robot.commands;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberSubsystem;

public class LeftClimbingCommand extends CommandBase {

    private final ClimberSubsystem m_subsystem;
    private DoubleSupplier joystick;
    
    public LeftClimbingCommand(ClimberSubsystem subsystem, DoubleSupplier joystickIn) {
        m_subsystem = subsystem;
        joystick = joystickIn;
        addRequirements(m_subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        //System.out.println("Left climber execute");
        double joystickScale = joystick.getAsDouble() * -0.5;
        m_subsystem.setLeftClimberPower(joystickScale);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_subsystem.stopClimber();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}

