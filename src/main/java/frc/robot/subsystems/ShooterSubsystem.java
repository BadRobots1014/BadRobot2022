package frc.robot.subsystems;

import java.lang.Math;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
    public static class RangeConfig {
        /**
         * The initial angle of the shooter, in degrees.
         */
        public final double initialAngle;
        /**
         * The maximum initial velocity of a ball launched in this configuration, in m/s.
         * 
         * This is measured with the flywheel motor running at full speed.
         */
        public final double maxInitialVelocity;
        /**
         * Whether or not the motor power should be multiplied by -1.
         */
        public final boolean motorIsInverted;

        public RangeConfig(
            double initialAngle,
            double maxInitialVelocity,
            boolean motorIsInverted
        ) {
            this.initialAngle = initialAngle;
            this.maxInitialVelocity = maxInitialVelocity;
            this.motorIsInverted = motorIsInverted;
        }
    }
    private final WPI_TalonFX speedController = new WPI_TalonFX(ShooterConstants.kShooterPort);

    public ShooterSubsystem() {

    }

    /**
     * Sets the flywheel motor to the given power.
     * 
     * @param power The motor power, as a percentage. This is positive for the forward direction and
     *              negative otherwise.
     */
    public void run(double power) {
        this.speedController.set(TalonFXControlMode.PercentOutput, 0.5);
        System.out.println("Running shooter");
    }

    /**
     * Stops the flywheel motor.
     */
    public void stop() {
        //this.speedController.set(TalonFXControlMode.Velocity, 0.0);
        //this.speedController.set(TalonFXControlMode.PercentOutput, 0.0);
        this.speedController.stopMotor();
        System.out.println("Shooter stopped");
    }

    /**
     * Returns the motor power necessary to hit a target point.
     * 
     * The output of this method is intended as an input to {@link #run}.
     * 
     * @param targetX The target range, in meters.
     * @param targetY The target height, in meters.
     * @return        The power, as a percentage.
     */
    public double powerToHitTarget(double targetX, double targetY) {
        RangeConfig config;
        if(targetX < ShooterConstants.kInitialAngleDeterminantRange) {
            config = ShooterConstants.kRangeConfigClose;
        } else {
            config = ShooterConstants.kRangeConfigFar;
        }

        double initialVelocity = initialVelocityToHitTarget(config.initialAngle, targetX, targetY);
        double power = initialVelocity / config.maxInitialVelocity;
        if(config.motorIsInverted) {
            power *= -1.0;
        }
        
        return power;
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
        // This function solves the following system of equations for `initVel` by substituting
        // `time`:
        //   { x =  cos(initAngle) * initVel * time
        //   { y = (sin(initAngle) * initVel * time) - ((1/2) * gravityAccel * (time^2))

        initialAngle = Math.toRadians(initialAngle);
        double numerator = ShooterConstants.kGravityAcceleration * Math.pow(targetX, 2.0);
        double denominator = (2 * Math.pow(Math.cos(initialAngle), 2)) *
            ((-1.0 * targetY) + (targetX * Math.tan(initialAngle)));
        
        return Math.sqrt(numerator / denominator);
    }

    public double getDeltaDesiredVelocity() {
        System.out.println(this.speedController.getSelectedSensorVelocity());
        return this.speedController.getSelectedSensorVelocity() - (ShooterConstants.kGoalSpeed * 2);
    }
}
