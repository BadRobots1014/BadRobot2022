package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;

import frc.robot.Constants.DriveTrainConstants;
import frc.robot.util.GyroProvider;
import frc.robot.subsystems.DriveTrainSubsystem;

public class AnchorCommand extends DriveStraightCommand {
    private final GyroProvider gyro;
    /**
     * A PID controller whose process variable is the current displacement in the X-axis.
     */
    private final PIDController displacementPid = new PIDController(
        DriveTrainConstants.kDisplacementP,
        DriveTrainConstants.kDisplacementI,
        DriveTrainConstants.kDisplacementD
    );

    /**
     * Constructs a new {@link AnchorCommand}.
     *
     * @param drive The {@link DriveTrainSubsystem}.
     * @param gyro  The {@link GyroProvider}.
     */
    public AnchorCommand(DriveTrainSubsystem drive, GyroProvider gyro) {
        // Conceptually, anchoring is driving straight with a base power that is usually 0 but
        // adjusts according to displacement. This allows us to reuse the angular PID controller
        // code from 'DriveStraightCommand'.
        super(drive, gyro, () -> 0.0);
        // This comes after the superclass constructor as it necessarily references the displacement
        // PID controller instance field.
        super.setPowerSource(() -> this.displacementPid.calculate(gyro.getDisplacementX()));

        this.gyro = gyro;
    }

    @Override
    public void initialize() {
        super.initialize();

        // According to 'AHRS::getDisplacementX' (upon which 'GyroProvider::getDisplacementX' is
        // based), displacement measurement quickly becomes inaccurate over time. Resetting
        // displacment upon every invocation of this command attempts to mitigate this.
        this.gyro.resetDisplacement();

        this.displacementPid.reset();
        // Note that 'gyro.getDisplacementX()' should return approximately 0.
        this.displacementPid.setSetpoint(this.gyro.getDisplacementX());
    }
}
