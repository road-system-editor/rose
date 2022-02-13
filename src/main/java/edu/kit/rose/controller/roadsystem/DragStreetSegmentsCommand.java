package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates the functionality of a street segment dragging
 * and makes it changeable.
 */
public class DragStreetSegmentsCommand implements ChangeCommand {
  private final ReplacementLog replacementLog;
  private final Project project;
  private final List<Segment> segments;
  private final Movement movement;

  /**
   * Creates a {@link DragStreetSegmentsCommand}
   * that drags a given set of segments by a specified movement.
   *
   * @param replacementLog   the replacement log to look up current segment versions in.
   * @param project          the model facade to execute {@link DragStreetSegmentsCommand on}
   * @param segments         the segments to drag
   * @param movement         the translation of the segments
   */
  public DragStreetSegmentsCommand(ReplacementLog replacementLog, Project project,
                                   List<Segment> segments, Movement movement) {
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.project = Objects.requireNonNull(project);
    this.segments = Objects.requireNonNull(segments);
    this.movement = Objects.requireNonNull(movement);
  }

  @Override
  public void execute() {
    this.project.getRoadSystem().moveSegments(getCurrentSegments(), movement);
  }

  @Override
  public void unexecute() {
    this.project.getRoadSystem().moveSegments(getCurrentSegments(), getInverseMovement(movement));
  }

  private static Movement getInverseMovement(Movement movement) {
    return new Movement(-movement.getX(), -movement.getY());
  }

  private List<Segment> getCurrentSegments() {
    return this.segments.stream()
        .map(this.replacementLog::getCurrentVersion)
        .toList();
  }
}
