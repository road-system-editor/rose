package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.List;

/**
 * A Connector that can be moved by anyone that dares.
 */
public class MovableConnector extends Connector {

  /**
   * Constructor.
   *
   * @param type      the {@link ConnectorType} for this RoseConnector.
   * @param position  the {@link Position} that this RoseConnector is supposed to be at.
   * @param accessors The {@link AttributeAccessor}s that this RoseConnector is supposed to have.
   */
  MovableConnector(ConnectorType type, Position position,
                   List<AttributeAccessor<?>> accessors) {
    super(type, position, accessors);
  }

  @Override
  public void move(Movement movement) {
    this.getPositionInstance().setX(this.getPosition().getX() + movement.getX());
    this.getPositionInstance().setY(this.getPosition().getY() + movement.getY());
    notifySubscribers();
  }

  /**
   * Sets the {@link MovableConnector}s position to a specified position,
   * but does not notify the {@link MovableConnector}s position.
   *
   * @apiNote This method does not call observers.
   *
   * @param targetPosition the new position of the {@link MovableConnector}
   */
  void setPosition(Position targetPosition) {
    this.getPositionInstance().setX(targetPosition.getX());
    this.getPositionInstance().setY(targetPosition.getY());
  }
}
