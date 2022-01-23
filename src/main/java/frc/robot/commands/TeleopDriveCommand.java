package frc.robot.commands;

import java.util.Map;
import java.util.function.Supplier;
import java.util.Optional;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.Constants.ControllerConstants;

public class TeleopDriveCommand extends CommandBase {
    private final Supplier<Double> m_xSource;
    private final Supplier<Double> m_ySource;
    private double m_cachedX = 0;
    private double m_cachedY = 0;
    private final AnchorCommand m_anchorCommand;
    private final PivotTurnCommand m_pivotTurnCommand;
    private final DriveStraightCommand m_driveStraightCommand;
    private final ArcadeDriveCommand m_arcadeDriveCommand;
    private final Map<CommandKind, Command> m_commandMap;
    private final ShuffleboardTab m_tab = Shuffleboard.getTab("Teleop Drive");
    private Optional<CommandKind> m_currentCommandKind = Optional.empty();

    public TeleopDriveCommand(
        DriveTrainSubsystem drive,
        GyroSubsystem gyro,
        Supplier<Double> xSource,
        Supplier<Double> ySource
    ) {
        m_xSource = xSource;
        m_ySource = ySource;

        m_anchorCommand = new AnchorCommand(drive, gyro);
        m_pivotTurnCommand = new PivotTurnCommand(drive, () -> m_cachedX);
        m_driveStraightCommand = new DriveStraightCommand(drive, gyro, () -> m_cachedY);
        m_arcadeDriveCommand = new ArcadeDriveCommand(drive, () -> m_cachedY, () -> m_cachedX);

        m_commandMap = Map.ofEntries(
            Map.entry(CommandKind.ANCHOR, m_anchorCommand),
            Map.entry(CommandKind.PIVOT_TURN, m_pivotTurnCommand),
            Map.entry(CommandKind.DRIVE_STRAIGHT, m_driveStraightCommand),
            Map.entry(CommandKind.ARCADE_DRIVE, m_arcadeDriveCommand)
        );

        addRequirements(drive);

        m_tab.addString(
            "Current Command",
            () -> {
                return m_currentCommandKind
                    .map((kind) -> m_commandMap.get(kind).getName())
                    .orElse("None");
            }
        );
    }

    @Override
    public void execute() {
        // TODO: This is seriously evil stuff. Please find a less error-prone way to do this,
        // preferably using an existing WPILIB class.

        final CommandKind newCommandKind = this.selectCommandKind();
        final Command newCommand = m_commandMap.get(newCommandKind);

        m_currentCommandKind.ifPresentOrElse(
            (currentCommandKind) -> {
                final Command currentCommand = m_commandMap.get(currentCommandKind);

                if (newCommandKind != currentCommandKind) {
                    currentCommand.end(false);
                    newCommand.initialize();
                }
            },
            () -> {
                newCommand.initialize();
            }
        );

        m_currentCommandKind = Optional.of(newCommandKind);
        newCommand.execute();
    }

    private enum CommandKind {
        ANCHOR,
        PIVOT_TURN,
        DRIVE_STRAIGHT,
        ARCADE_DRIVE
    };

    private CommandKind selectCommandKind() {
        // We use an arcade-drive system with controller deadzones that select specialized commands.

        final double x = m_xSource.get();
        final double y = m_ySource.get();

        // Avoid calling suppliers again in the final, specialized command by caching them here.
        m_cachedX = x;
        m_cachedY = y;

        // 'x' and 'y' represent a Cartesian point. If that point is within a small circular deadzone
        // centered at the origin, then we anchor.
        final double originDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        if (originDistance <= ControllerConstants.kAnchorDeadzoneRadius) {
            return CommandKind.ANCHOR;
        }

        // There are also deadzones for strictly driving straight and strictly pivot-turning. These
        // deadzones are, geometrically, isosceles triangles that are symmetric along the X and Y axes
        // with an apex at the origin. Thus, the legs of each triangle can be modelled with a linear
        // function. If the output of that function is ever less than the length of the associated
        // triangle's base, then the input to that function is within a deadzone.

        // TODO: Explain this better; make better variable names.

        final double driveStraightAxisDistance = Math.abs(x);
        final double pivotTurnAxisDistance = Math.abs(y);

        final double driveStraightDeadzone = ControllerConstants.kDriveStraightDeadzoneBaseLength * pivotTurnAxisDistance;
        if (driveStraightAxisDistance <= driveStraightDeadzone) {
            return CommandKind.DRIVE_STRAIGHT;
        }

        final double pivotTurnDeadzone = ControllerConstants.kPivotTurnDeadzoneBaseLength * driveStraightAxisDistance;
        if (pivotTurnAxisDistance <= pivotTurnDeadzone) {
            return CommandKind.PIVOT_TURN;
        }

        return CommandKind.ARCADE_DRIVE;
    }

    @Override
    public void end(boolean interrupted) {
        m_currentCommandKind.ifPresent((kind) -> m_commandMap.get(kind).end(false));
        m_currentCommandKind = Optional.empty();
    }
}
