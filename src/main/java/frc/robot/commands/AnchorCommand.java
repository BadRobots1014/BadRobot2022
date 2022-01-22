package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants.DriveTrainConstants;
import frc.robot.util.GyroProvider;
import frc.robot.subsystems.DriveTrainSubsystem;

public class AnchorCommand extends CommandBase {
    private final DriveTrainSubsystem drive;
    private final GyroProvider gyro;
    private final PIDController angularController = new PIDController(
        DriveTrainConstants.kP,
        DriveTrainConstants.kI,
        DriveTrainConstants.kD
    );
    private final PIDController displacementController = new PIDController(
        DriveTrainConstants.kP,
        DriveTrainConstants.kI,
        DriveTrainConstants.kD
    );

    public AnchorCommand(DriveTrainSubsystem drive, GyroProvider gyro) {
        this.drive = drive;
        this.gyro = gyro;
        super.addRequirements(this.drive);

        this.angularController.setSetpoint(gyro.getAngle());
        this.angularController.enableContinuousInput(-180, 180);

        this.displacementController.setSetpoint(gyro.getDisplacementX());
    }

    @Override
    public void initialize() {
        this.angularController.reset();
        this.displacementController.reset();
    }

    @Override
    public void execute() {
        final double angularOutput = this.angularController.calculate(this.gyro.getAngle());
        final double linearOutput = this.displacementController.calculate(this.gyro.getDisplacementX());
        this.drive.tankDrive(linearOutput + angularOutput, linearOutput - angularOutput);
    }

    @Override
    public void end(boolean interrupted)
    {
        this.drive.stop();
    }
}
