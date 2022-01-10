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
     * @param type the {@link ConnectorType} for this SimpleConnector.
     * @param position the {@link Position} that this SimpleConnector is supposed to be at.
     * @param accessors The {@link AttributeAccessor}s that this SimpleConnector is supposed to have.
     */
    MoveableConnector(ConnectorType type, Position position, Collection<AttributeAccessor<?>> accessors) {
        super(type, position, accessors);
    }

    @Override
    public void move(Movement movement){}
}
