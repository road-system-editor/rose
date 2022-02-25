package edu.kit.rose.model.roadsystem.elements;


import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.RoseUnitObservable;
import edu.kit.rose.infrastructure.SortedBox;
import java.util.Objects;

/**
 * A connection between two {@link Connector}s.
 */
public class Connection extends RoseUnitObservable<Connection> {

  private final Connector connector1;
  private final Connector connector2;
  private Position center;

  /**
   * Constructor.
   *
   * @param connector1 the first connector of this connection.
   * @param connector2 the second connector of this connection, must be different to the first one.
   * @param center the absolute position of the center of this connection.
   */
  public Connection(Connector connector1, Connector connector2, Position center) {
    Objects.requireNonNull(connector1);
    Objects.requireNonNull(connector2);
    Objects.requireNonNull(center);

    if (connector1 == connector2) {
      throw new IllegalArgumentException("can not connect a connector to itself");
    }

    this.connector1 = connector1;
    this.connector2 = connector2;
    this.center = new Position(center.getX(), center.getY());
  }

  @Override
  public Connection getThis() {
    return this;
  }

  /**
   * Gives the {@link Connector}s that are connected with this Connection.
   *
   * @return The {@link Connector}s that are connected with this Connection.
   */
  public SortedBox<Connector> getConnectors() {
    return new RoseSortedBox<>(connector1, connector2);
  }

  /**
   * Provides the other {@link Connector} held within respectively.
   *
   * @param knownConnector the connector that is known.
   * @return the other connector.
   */
  public Connector getOther(Connector knownConnector) {
    if (getConnectors().contains(knownConnector)) {
      return knownConnector.equals(connector1) ? connector2 : connector1;
    } else {
      throw new IllegalArgumentException("unknown connector.");
    }
  }

  /**
   * Moves the center of this connection by a given movement.
   *
   * @param movement the movement by which to move the center.
   */
  public void move(Movement movement) {
    this.center = this.center.add(movement);
    notifySubscribers();
  }

  /**
   * Provides a position indicating the center of this connection.
   *
   * @return the center position.
   */
  public Position getCenter() {
    return new Position(center.getX(), center.getY());
  }
}
