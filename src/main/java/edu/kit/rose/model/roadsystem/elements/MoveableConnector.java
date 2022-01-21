package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.Collection;

/**
 * A Connector that can be moved by anyone that dares.
 */
public class MoveableConnector extends Connector {

  /**
   * Constructor.
   *
   * @param type      the {@link ConnectorType} for this RoseConnector.
   * @param position  the {@link Position} that this RoseConnector is supposed to be at.
   * @param accessors The {@link AttributeAccessor}s that this RoseConnector is supposed to have.
   */
  MoveableConnector(ConnectorType type, Position position,
                    Collection<AttributeAccessor<?>> accessors) {
    super(type, position, accessors);
  }

  @Override
  public void move(Movement movement) {
    Position position = this.getPosition();
    position.setX(position.getX() + movement.getX());
    position.setY(position.getY() + movement.getY());
  }
}
