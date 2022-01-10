package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.*;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;

import java.util.Collection;

/**
 * A Connector is part of a {@link edu.kit.rose.model.roadsystem.elements.Segment} and represents the movable end points
 * of the same (see Pflichtenheft: Straßensegment). A Connector can also be part of a {@link Connection}.
 */
public class Connector implements UnitObservable<Connector> {

    /**
     * Constructor.
     * @param type the {@link ConnectorType} for this SimpleConnector.
     * @param position the {@link Position} that this SimpleConnector is supposed to be at.
     * @param accessors The {@link AttributeAccessor}s that this SimpleConnector is supposed to have.
     */
    Connector(ConnectorType type, Position position, Collection<AttributeAccessor<?>> accessors) {

    }
    @Override
    public void addSubscriber(UnitObserver<Connector> observer) {

    }

    @Override
    public void removeSubscriber(UnitObserver<Connector> observer) {

    }

    /**
     * Returns the {@link Position} of the connector.
     * @return the {@link Position} of the connector.
     */
    public Position getPosition() {
        return null;
    }

    /**
     * Gives the {@link AttributeAccessor}s to the Attributes that are specific for this Connector.
     * The referenced Attributes are part of the Segment this Connector is part of, though only the accessors for
     * the connector specific attributes will get returned. i.e. the lane count accessor returned by this method will
     * give access to the lane count attribute of the end point of the segment represented by this connector.
     * @return a {@link SortedBox} containing the specific {@link AttributeAccessor}s of this Connector.
     */
    public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
        return null;
    }

    /**
     *
     * @return the Type of Connector this is.
     */
    public ConnectorType getType() {
        return null;
    }

    @Override
    public void notifySubscribers() {

    }

    /**
     * Moves Connector.
     * @param movement the movement that is to be applied.
     */
    void move(Movement movement) {

    }
}
