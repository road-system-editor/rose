package edu.kit.rose.model.roadsystem.elements;


import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;

/**
 * A connection between two {@link Connector}s.
 */
public class Connection implements UnitObservable<Connection> {

  @Override
  public void addSubscriber(UnitObserver<Connection> observer) {

  }

  @Override
  public void removeSubscriber(UnitObserver<Connection> observer) {

  }

  @Override
  public void notifySubscribers() {

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
  public Box<Connector> getConnectors() {
    return null;
  }

  /**
   * Privides the other {@link Connector} held within respectively.
   *
   * @param knownConnector the connector that is known.
   * @return the other connector.
   */
  public Connector getOther(Connector knownConnector) {
    return null;
  }
}
