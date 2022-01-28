package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.List;

/**
 * Encapsulates the functionality of a street segment dragging
 * and makes it changeable.
 */
public class DragStreetSegmentCommand implements ChangeCommand {

  private final Project project;
  private final List<Segment> segments;
  private final Movement movement;

  /**
   * Creates a {@link DragStreetSegmentCommand}
   * that drags a given set of segments by a specified movement.
   *
   * @param project          the model facade to execute {@link DragStreetSegmentCommand on}
   * @param segments         the segments to drag
   * @param movement         the translation of the segments
   */
  public DragStreetSegmentCommand(Project project, List<Segment> segments, Movement movement) {
    this.project = project;
    this.segments = segments;
    this.movement = movement;
  }

  @Override
  public void execute() {
    this.project.getRoadSystem().moveSegments(segments, movement);
  }

  @Override
  public void unexecute() {
    this.project.getRoadSystem().moveSegments(segments, getInverseMovement(movement));
  }

  private static Movement getInverseMovement(Movement movement) {
    return new Movement(-movement.getX(), -movement.getY());
  }
}
