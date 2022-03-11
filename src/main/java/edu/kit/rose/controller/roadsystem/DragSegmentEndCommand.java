package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ConnectionCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.MovableConnector;
import java.util.Objects;

/**
 * Encapsulates the functionality of a street segment end dragging
 * and makes it changeable.
 */
public class DragSegmentEndCommand implements ChangeCommand {
  private MovableConnector connector;
  private final Movement movement;
  private final RoadSystem roadSystem;
  private final ReplacementLog replacementLog;

  private Connection connection;
  private boolean isExecuteFirstCall = true;

  /**
   * Creates a {@link DragSegmentEndCommand}
   * that drags a given end of a segments by a specified movement.
   *
   * @param roadSystem       the {@link RoadSystem} the {@link Connector} belongs to
   * @param replacementLog   the replacement log to find the current version of the {@code
   *     segmentEnd} in, may not be {@code null}.
   * @param segmentEnd       the segment end to drag, may not be {@code null}.
   * @param translation      the translation of the segment end, may not be {@code null}.
   */
  public DragSegmentEndCommand(RoadSystem roadSystem, ReplacementLog replacementLog,
                               MovableConnector segmentEnd, Movement translation) {
    this.roadSystem = Objects.requireNonNull(roadSystem);
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.connector = Objects.requireNonNull(segmentEnd);
    this.movement = Objects.requireNonNull(translation);
  }

  @Override
  public void execute() {
    getCurrentConnector().move(movement);
    if (isExecuteFirstCall) {
      this.connection = ConnectionBuilder.buildConnection(this.roadSystem, connector);
      this.isExecuteFirstCall = false;
    } else {
      ConnectionCopier copier = new ConnectionCopier(this.replacementLog, this.roadSystem);
      copier.copyConnection(this.connection);
    }
  }

  @Override
  public void unexecute() {
    getCurrentConnector().move(getInverseMovement(movement));
  }

  private static Movement getInverseMovement(Movement movement) {
    return new Movement(-movement.getX(), -movement.getY());
  }

  private MovableConnector getCurrentConnector() {
    this.connector = this.replacementLog.getCurrentConnectorVersion(this.connector);
    return this.connector;
  }
}
