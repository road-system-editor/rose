package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Segment;

/**
 * The {@link ConnectorWrapper} class is a wrapper around
 * {@link edu.kit.rose.model.roadsystem.elements.Connector} instance. That
 * transforms the absolute coordinates of a {@link edu.kit.rose.model.roadsystem.elements.Connector}
 * into the offset to the belonging {@link Segment}s center
 * {@link edu.kit.rose.infrastructure.Position}.
 *
 * @param <T> the {@link edu.kit.rose.model.roadsystem.elements.SegmentType} of target
 *            {@link Segment}
 */
public class ConnectorWrapper<T extends Segment> implements UnitObserver<Connector> {

  private final T segment;
  private final Connector connector;
  private Runnable connectorPositionChangedCallback;

  /**
   * Creates a new instance of the {@link ConnectorWrapper} class.
   *
   * @param segment   the segment that belongs to the connector
   * @param connector the target connector
   */
  public ConnectorWrapper(T segment, Connector connector) {
    this.segment = segment;
    this.connector = connector;
    connector.addSubscriber(this);
  }

  public void detach() {
    connector.removeSubscriber(this);
  }

  @Override
  public void notifyChange(Connector unit) {
    if (connectorPositionChangedCallback != null) {
      connectorPositionChangedCallback.run();
    }
  }

  /**
   * Returns the translation of the connector position to the segment center position.
   *
   * @return the translation of the connector position to the segment center position
   */
  public Movement getMovement() {
    return new Movement(
        this.connector.getPosition().getX() - this.segment.getCenter().getX(),
        this.connector.getPosition().getY() - this.segment.getCenter().getY());
  }

  public void setOnConnectorPositionChangedCallback(Runnable consumer) {
    this.connectorPositionChangedCallback = consumer;
  }
}