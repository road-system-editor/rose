package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;

import java.util.List;

/**
 * Encapsulates the functionality of a street segment dragging
 * and makes it changeable.
 */
public class DragStreetSegmentCommand implements ChangeCommand {

  /**
   * Creates a {@link DragStreetSegmentCommand} that drags a given set of segments by a specified movement
   *
   * @param project          the model facade to execute {@link DragStreetSegmentCommand on}
   * @param segments         the segments to drag
   * @param startingPosition the starting position of the segments
   * @param movement         the translation of the segments
   */
  public DragStreetSegmentCommand(Project project, List<Segment> segments,
                                  Position startingPosition, Movement movement) {

  }

  @Override
  public void execute() {

  }

  @Override
  public void unexecute() {

  }
}
