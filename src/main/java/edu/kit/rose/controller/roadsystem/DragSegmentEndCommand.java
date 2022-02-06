package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.MovableConnector;

/**
 * Encapsulates the functionality of a street segment end dragging
 * and makes it changeable.
 */
public class DragSegmentEndCommand implements ChangeCommand {

  private final MovableConnector connector;
  private final Movement movement;

  /**
   * Creates a {@link DragSegmentEndCommand}
   * that drags a given end of a segments by a specified movement.
   *
   * @param segmentEnd       the segment end to drag
   * @param translation      the translation of the segment end
   */
  public DragSegmentEndCommand(MovableConnector segmentEnd, Movement translation) {
    this.connector = segmentEnd;
    this.movement = translation;
  }

  @Override
  public void execute() {
    connector.move(movement);
  }

  @Override
  public void unexecute() {
    connector.move(getInverseMovement(movement));
  }

  private static Movement getInverseMovement(Movement movement) {
    return new Movement(-movement.getX(), -movement.getY());
  }
}
