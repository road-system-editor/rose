package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.Collection;
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
    this.getPositionInstance().setX(getPosition().getX() + movement.getX());
    this.getPositionInstance().setY(getPosition().getY() + movement.getY());
    notifySubscribers();
  }
}
