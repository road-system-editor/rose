package edu.kit.rose.model.roadsystem.elements;


import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.SimpleBox;
import edu.kit.rose.infrastructure.SimpleSortedBox;
import edu.kit.rose.infrastructure.SimpleUnitObservable;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;
import java.util.Arrays;
import java.util.List;

/**
 * A connection between two {@link Connector}s.
 */
public class Connection extends SimpleUnitObservable<Connection> {

  private final Connector connector1;
  private final Connector connector2;

  public Connection(Connector connector1, Connector connector2) {
    this.connector1 = connector1;
    this.connector2 = connector2;
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
    return new SimpleSortedBox<>(List.of(connector1, connector2));
  }

  /**
   * Privides the other {@link Connector} held within respectively.
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
}
