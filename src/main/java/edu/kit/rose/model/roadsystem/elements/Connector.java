package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.RoseUnitObservable;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.List;

/**
* A Connector is part of a {@link edu.kit.rose.model.roadsystem.elements.Segment}
* and represents the movable end points
* of the same (see Pflichtenheft: Straßensegment). A Connector can also be part of a
* {@link Connection}.
*/
public class Connector extends RoseUnitObservable<Connector>
          implements UnitObservable<Connector> {
  private final ConnectorType type;
  private final Position position;
  private final List<AttributeAccessor<?>> accessors;

  /**
   * Constructor.
   *
   * @param type      the {@link ConnectorType} for this Connector.
   * @param position  the {@link Position} that this Connector is supposed to be at.
   * @param accessors The {@link AttributeAccessor}s that this Connector is supposed to have.
   */
  Connector(ConnectorType type, Position position, List<AttributeAccessor<?>> accessors) {
    this.type = type;
    this.position = position;
    this.accessors = accessors;
  }

  /**
   * Returns the {@link Position} of the connector.
   *
   * @return the {@link Position} of the connector.
   */
  public Position getPosition() {
    return new Position(this.position.getX(), this.position.getY());
  }


  protected Position getPositionInstance() {
    return this.position;
  }

  /**
   * Gives the {@link AttributeAccessor}s to the Attributes that are specific for this Connector.
   * The referenced Attributes are part of the Segment this Connector is part of, though only the
   * accessors for the connector specific attributes will get returned. i.e.
   * the lane count accessor returned by this method will give access to the lane count attribute
   * of the end point of the segment represented by this connector.
   *
   * @return a {@link SortedBox} containing the specific {@link AttributeAccessor}s of this
   *      Connector.
   */
  public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
    return new RoseSortedBox<>(this.accessors);
  }

  /**
   * Provides the Type of Connector this is.
   *
   * @return the Type of Connector this is.
   */
  public ConnectorType getType() {
    return this.type;
  }

  @Override
  public Connector getThis() {
    return this;
  }

  /**
   * Moves Connector.
   * As connector positions are relative to their respective segment center, this does not
   * actually move anything. The method still needs to be called as subscribers will have to be
   * notified regardless.
   *
   * @param movement the movement that is to be applied.
   */
  void move(Movement movement) {
    notifySubscribers();
  }

}