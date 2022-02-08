package frc.robot.subsystems;

import java.util.Optional;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.VisionConstants;

public class VisionSubsystem extends SubsystemBase {
    private final PIDController rotationalPid;
    private final NetworkTableEntry camMode;
    private final NetworkTableEntry pipeline;
    private final NetworkTableEntry targetIsVisible;
    private final NetworkTableEntry targetX;
    private final NetworkTableEntry targetY;
    private final ShuffleboardTab tab;
    private final SendableChooser<Integer> pipelineChooser;

    public VisionSubsystem() {
        this.rotationalPid = new PIDController(.01, 0, 0);
        this.rotationalPid.setSetpoint(0.0);

        final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        this.camMode = table.getEntry("camMode");
        this.camMode.setNumber(VisionConstants.kVisionProcessorCamMode);
        this.pipeline = table.getEntry("pipeline");

        this.targetIsVisible = table.getEntry("tv");
        this.targetX = table.getEntry("tx");
        this.targetY = table.getEntry("ty");

        this.tab = Shuffleboard.getTab("Vision");
        this.tab.addBoolean("Found Target?", this::targetIsVisible);
        this.tab.addNumber("X", this::getTargetX);
        this.tab.addNumber("Y", this::getTargetY);
        this.pipelineChooser = new SendableChooser<>();
        this.pipelineChooser.setDefaultOption("Red Cargo", VisionConstants.kRedCargoPipelineId);
        this.pipelineChooser.addOption("Red Cargo", VisionConstants.kRedCargoPipelineId);
        this.pipelineChooser.addOption("Blue Cargo", VisionConstants.kBlueCargoPipelineId);
        this.tab.add("Pipeline", this.pipelineChooser);
    }

    public void applyChosenPipeline() {
        final int id = this.pipelineChooser.getSelected();
        this.pipeline.setNumber(id);
    }

    public double getRotationalPid() {
        return this.rotationalPid.calculate(this.getTargetX());
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
