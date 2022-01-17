// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.ShooterSubsystem.RangeConfig;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    /**
     * Constants related to the shooter.
     */
    public static final class ShooterConstants {
        public static final int kShooterPort = 7;

        /**
         * The range of the ball from the target that determines which configuration is used, in
         * meters.
         * 
         * If the actual range is less than this value, then use {@link #kRangeConfigClose}.
         * Otherwise, if it is higher than this value, then use {@link #kRangeConfigFar}.
         */
        public static final double kInitialAngleDeterminantRange = 3.0;

        public static final RangeConfig kRangeConfigClose = new RangeConfig(65.0, -1.0);
        public static final RangeConfig kRangeConfigFar = new RangeConfig(75.0, 1.0);
    
        /**
         * The acceleration of gravity, in m/s/s.
         */
        public static final double kGravityAcceleration = 9.8;

        /**
         * The maximum initial velocity of a ball, in m/s.
         * 
         * This was measured with the flywheel motor running at full speed.
         */
        public static final double kMaxInitialBallVelocity = 32.8;
    }

    /**
     * Constants relating to driver controls.
     */
    public final class ControllerConstants {
        public static final int kControllerPort = 0;
        public static final int kPrototypePort = 1;
    }

    public final class DriveTrainConstants {
        public static final int kDriveTrainLeftPort = 9;
        public static final int kDriveTrainRightPort = 8;
    }

    /**
     * Constants used for prototyping.
     */
    public final class PrototypeConstants {
        /**
         * The CAN ID of the unattached SPARK MAX.
         */
        public static final int kSpeedController1 = 7;

        /**
         * The CAN ID of one of the unattached Talon FXs.
         */
        public static final int kSpeedController2 = 11;

        /**
         * The CAN ID of one of the unattached Talon FXs.
         */
        public static final int kSpeedController3 = 13;

        /**
         * The CAN ID of one of the unattached Talon FXs.
         */
        public static final int kSpeedController4 = 14;
    }
}
