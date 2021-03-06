package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ConnectionCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates the functionality of a street segment dragging
 * and makes it changeable.
 */
public class DragStreetSegmentsCommand implements ChangeCommand {
  private final ReplacementLog replacementLog;
  private final Project project;
  private final List<Segment> segments;
  private final Connector dragConnector;
  private final Movement movement;

  private final Set<Connection> previouslyExistingConnections;
  private Connection connectionCreatedAfterDragging;
  private boolean isExecuteFirstCall = true;

  /**
   * Creates a {@link DragStreetSegmentsCommand}
   * that drags a given set of segments by a specified movement.
   *
   * @param replacementLog the replacement log to look up current segment versions in.
   * @param project        the model facade to execute {@link DragStreetSegmentsCommand on}
   * @param segments       the segments to drag
   * @param movement       the translation of the segments
   * @param previouslyExistingConnections the set of connections the segments had before they
   *                                      were moved
   * @param dragConnector  the connector that is used to drag the segments of null
   */
  public DragStreetSegmentsCommand(ReplacementLog replacementLog, Project project,
                                   List<Segment> segments, Movement movement,
                                   Set<Connection> previouslyExistingConnections,
                                   Connector dragConnector) {
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.project = Objects.requireNonNull(project);
    this.segments = Objects.requireNonNull(segments);
    this.movement = Objects.requireNonNull(movement);
    this.dragConnector = dragConnector;

    if (previouslyExistingConnections != null) {
      this.previouslyExistingConnections = new HashSet<>(
          Objects.requireNonNull(previouslyExistingConnections));
    } else { //Prevent error that previouslyExistingConnections has not been initialized
      this.previouslyExistingConnections = null;
    }
  }

  @Override
  public void execute() {
    this.project.getRoadSystem().moveSegments(getCurrentSegments(), movement);
    if (isExecuteFirstCall && this.segments.size() == 1 && this.dragConnector != null) {
      this.connectionCreatedAfterDragging = ConnectionBuilder.buildConnection(
          this.project.getRoadSystem(), this.dragConnector);
      isExecuteFirstCall = false;
    } else  if (this.previouslyExistingConnections != null) {
      if (this.segments.size() == 1 && this.dragConnector != null) {
        ConnectionCopier copier = new ConnectionCopier(
            this.replacementLog, this.project.getRoadSystem());
        copier.copyConnection(connectionCreatedAfterDragging);
      }
    }
  }

  @Override
  public void unexecute() {
    this.project.getRoadSystem().moveSegments(getCurrentSegments(), getInverseMovement(movement));
    restoreConnections();
  }

  private static Movement getInverseMovement(Movement movement) {
    return new Movement(-movement.getX(), -movement.getY());
  }

  private void restoreConnections() {
    if (previouslyExistingConnections != null) {
      ConnectionCopier copier
          = new ConnectionCopier(this.replacementLog, this.project.getRoadSystem());
      for (Connection connection : this.previouslyExistingConnections) {
        copier.copyConnection(connection);
      }
    }
  }

  private List<Segment> getCurrentSegments() {
    return this.segments.stream()
        .map(this.replacementLog::getCurrentVersion)
        .toList();
  }
}
