package frc.robot.subsystems;

import java.lang.Math;

import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
    public static class RangeConfig {
        /**
         * The initial angle of the shooter, in degrees.
         */
        public double initialAngle;
        /**
         * A coefficient that scales the output of {@link ShooterSubsystem#powerToHitTarget}.
         */
        public double powerCoefficient;

        public RangeConfig(double initialAngle, double powerCoefficient) {
            this.initialAngle = initialAngle;
            this.powerCoefficient = powerCoefficient;
        }
    }

    private final PWMTalonFX speedController = new PWMTalonFX(ShooterConstants.kShooterPort);

    public ShooterSubsystem() {

    }

    public void run(double power) {
        this.speedController.set(power);
    }

    public void stop() {
        this.speedController.stopMotor();
    }

    /**
     * Returns the motor power necessary to hit a target point.
     * 
     * This value is intended as an input to {@link #run}.
     * 
     * @param targetX The target range, in meters.
     * @param targetY The target height, in meters.
     * @return        The power, as a percentage. This is positive for the forward direction and
     *                negative otherwise.
     */
    public double powerToHitTarget(double targetX, double targetY) {
        RangeConfig config;
        if(targetX < ShooterConstants.kInitialAngleDeterminantRange) {
            config = ShooterConstants.kRangeConfigClose;
        } else {
            config = ShooterConstants.kRangeConfigFar;
        }

        double initialVelocity = initialVelocityToHitTarget(config.initialAngle, targetX, targetY);
        double power = initialVelocity / ShooterConstants.kMaxInitialBallVelocity;
        
        return power * config.powerCoefficient;
    }

    /**
     * Returns the necessary initial velocity to hit a target point with the given initial angle.
     * 
     * @param initialAngle  The initial angle, in degrees.
     * @param targetX       The target range, in meters.
     * @param targetY       The target height, in meters.
     * @return              The initial velocity, in m/s.
     */
    private double initialVelocityToHitTarget(double initialAngle, double targetX, double targetY) {
        // This function solves the following system of equations for `initVel`:
        //   { x =  cos(initAngle) * initVel * time
        //   { y = (sin(initAngle) * initVel * time) - ((1/2) * gravityAccel * (time^2))

        initialAngle = Math.toRadians(initialAngle);
        double numerator = ShooterConstants.kGravityAcceleration * Math.pow(targetX, 2.0);
        double denominator = (2 * Math.pow(Math.cos(initialAngle), 2)) *
            ((-1.0 * targetY) + (targetX * Math.tan(initialAngle)));
        
        return Math.sqrt(numerator / denominator);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
