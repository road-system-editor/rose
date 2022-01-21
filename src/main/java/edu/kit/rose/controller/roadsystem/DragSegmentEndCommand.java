package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Connector;

/**
 * Encapsulates the functionality of a street segment end dragging
 * and makes it changeable.
 */
public class DragSegmentEndCommand implements ChangeCommand {

  /**
   * Creates a {@link DragSegmentEndCommand}
   * that drags a given end of a segments by a specified movement.
   *
   * @param project          the model facade to execute {@link DragStreetSegmentCommand on}
   * @param segmentEnd       the segment end to drag
   * @param startingPosition the starting position of the segment end
   * @param translation      the translation of the segment end
   */
  public DragSegmentEndCommand(Project project, Connector segmentEnd, Position startingPosition,
                               Movement translation) {

  }

  @Override
  public void execute() {

  }

  @Override
  public void unexecute() {

  }
}
