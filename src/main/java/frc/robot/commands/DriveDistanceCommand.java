package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;

public class DriveDistanceCommand extends CommandBase{
    private final DriveTrainSubsystem m_driveSubsystem;
    private final GyroSubsystem m_gyro;
    
    private double startTime;
    private double seconds;
    private double speed;
    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public DriveDistanceCommand(DriveTrainSubsystem subsystem, GyroSubsystem gyro, double distance) {
      this.m_driveSubsystem = subsystem;
      this.m_gyro = gyro;

      // TODO: Replace 5.0 with actual speed at .3 power using regression
      this.speed = .3;
      if(distance < 0){
        speed = -speed;
      }
      this.seconds = distance / 5.0;

      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(m_driveSubsystem, m_gyro);
    }
  
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        this.startTime = System.currentTimeMillis();
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        DriveStrategyCommand.driveStraight(m_driveSubsystem, m_gyro, () -> speed).withTimeout(seconds);
    }

  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (System.currentTimeMillis() - startTime) > seconds;
    }
}