// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
    public final class ShooterConstants {
        public static final int kShooterPort = 7;

    }

    /**
     * Constants relating to driver controls.
     */
    public final class ControllerConstants {
        public static final int kControllerPort = 0;
        public static final int kPrototypePort = 1;

        public static final int kThrottleButton = 1;
        public static final int kShootButton = 2;

        public static final double kDeadzoneRadius = .5;
        public static final double kDriveStraightRegionHalfBaseLength = .2;
        public static final double kPivotTurnRegionHalfBaseLength = .2;
    }

    public final class DriveTrainConstants {
        public static final int kDriveTrainLeftPort = 9;
        public static final int kDriveTrainRightPort = 8;

        public static final double kRotationalP = .01;
        public static final double kRotationalI = 0;
        public static final double kRotationalD = 0;

        public static final double kPositionalP = 1.0;
        public static final double kPositionalI = 0;
        public static final double kPositionalD = 0;
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
