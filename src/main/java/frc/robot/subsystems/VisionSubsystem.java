package frc.robot.subsystems;

import java.util.Optional;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.Constants.VisionConstants;

public class VisionSubsystem {
    private final NetworkTableEntry targetIsVisible;
    private final NetworkTableEntry targetX;
    private final NetworkTableEntry targetY;
    private final ShuffleboardTab tab;

    public VisionSubsystem() {
        final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        table.getEntry("camMode").setNumber(VisionConstants.kCamModeVisionProcessor);
        table.getEntry("pipeline").setNumber(VisionConstants.kPipelineId);

        this.targetIsVisible = table.getEntry("tv");
        this.targetX = table.getEntry("tx");
        this.targetY = table.getEntry("ty");

        this.tab = Shuffleboard.getTab("Vision");
        this.tab.addBoolean("Found Target?", this::targetIsVisible);
        this.tab.addNumber("X", this::getTargetX);
        this.tab.addNumber("Y", this::getTargetY);
    }

    public boolean targetIsVisible() {
        return this.targetIsVisible.getBoolean(false);
    }

    public static class Position {
        public double x;
        public double y;

        private Position(final double x, final double y) {
            this.x = x;
            this.y = y;
        }
    }

    public Optional<Position> findTarget() {
        if (this.targetIsVisible()) {
            return Optional.of(new Position(
                this.getTargetX(),
                this.getTargetY()
            ));
        } else {
            return Optional.empty();
        }
    }

    private double getTargetX() {
        return this.targetX.getDouble(0.0);
    }
    
    private double getTargetY() {
        return this.targetY.getDouble(0.0);
    }
}
