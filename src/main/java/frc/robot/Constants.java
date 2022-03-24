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
    public static final class VisionConstants {
        // See <https://docs.limelightvision.io/en/latest/getting_started.html#basic-programming>.
        public static final int kVisionProcessorCamMode = 0;

        public static final int kRedCargoPipelineId = 1;
        public static final int kBlueCargoPipelineId = 2;
        public static final int kHubPipelineId = 3;
    }

    /**
     * Constants related to the shooter.
     */
    public static final class ShooterConstants {
        public static final int kShooterPort = 7;

        //Make inversions here for forward/backward
        //Positive = forward or toward gatherer
        //Negative = backward or away from gatherer
        public static final double farShotPower = 0.75;
        public static final double farBackShotPower = -0.75;
        public static final double closeShotPower = 0.68;
        public static final double closeBackShotPower = -0.6;
    }

    /**
     * Constants relating to driver controls.
     */
    public final class ControllerConstants {
        public static final int kControllerPort = 0;

        public static final int kFollowTargetButton = 3;
        public static final int kThrottleButton = 2;
        
        //TODO: Shooting to be read in from another joystick still
        //Maybe use 6 and 7 for shooting forward and back in the future
        public static final int kShootButton = 7;

        public static final int kGatherButton = 1;

        public static final int kLowerIndexerButton = 10;
        // Upper indexer is in RobotConatiner as it is second controller

        public static final int kClimbingButton = 8; // Still needs set

        public static final double kDeadzoneRadius = .2;
        public static final double kDriveStraightRegionHalfBaseLength = .2;
        public static final double kPivotTurnRegionHalfBaseLength = .2;
    }

    public final class DriveTrainConstants {
        public static final int kDriveTrainLeftAPort = 12;
        public static final int kDriveTrainLeftBPort = 4;
        public static final int kDriveTrainRightAPort = 2;
        public static final int kDriveTrainRightBPort = 3;

        public static final double kRotationalP = .03;
        public static final double kRotationalI = 0;
        public static final double kRotationalD = 0;

        public static final double kPositionalP = 1.0;
        public static final double kPositionalI = 0;
        public static final double kPositionalD = 0;

        public static final double autoDriveDistance = 20.0;
    }
    public final class GathererConstants {
        public static final int kCollectorSpeedController = 13;
        public static final int kGathererSpeedController = 14;
    }

    public final class IndexerConstants {
        public static final int kLowerIndexerSpeedController = 29;
        public static final int kUpperIndexerSpeedController = 15;

        public static final int kIndexerMaxSpeed = 1;
    }
    public final class ClimberConstants {
        public static final int kRightClimberID = 34;
        public static final int kLeftClimberID = 35;
        public static final double climberUpSpeed = 0.5;
        public static final double climberDownSpeed = -0.25;
    }
}
