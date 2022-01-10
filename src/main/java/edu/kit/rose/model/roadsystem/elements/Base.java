package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.*;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;

/**
 * Represents a one way road. (as in part of a freeway)
 * A Base {@link Segment} is a simple {@link Segment} that only has one entrance and one exit.
 */
public class Base implements Segment {

    /**
     *
     * @return the {@link Connector} describing the entrance of the Base Segment.
     */
    Connector getEntry() {
        return null;
    }

    /**
     *
     * @return the {@link Connector} describing the exit of the base Segment
     */
    Connector getExit() {
        return null;
    }

    @Override
    public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public SegmentType getSegmentType() {
        return null;
    }

    @Override
    public SortedBox<Measurement<?>> getMeasurements() {
        return null;
    }

    @Override
    public Box<Connector> getConnectors() {
        return null;
    }

    @Override
    public Position getCenter() {
        return null;
    }

    @Override
    public void move(Movement movement) {

    }

    @Override
    public int compareTo(Segment o) {
        return 0;
    }

    @Override
    public void notifySubscribers() {

    }

    @Override
    public void addSubscriber(UnitObserver<Element> observer) {

    }

    @Override
    public void removeSubscriber(UnitObserver<Element> observer) {

    }
}
