package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.*;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;

/**
 * Represents a freeway exit.
 * An Exit Segment is a {@link Segment} that has one entrance to the main road and one exit from it as well as a singular ramp
 * by which cars can leave the Road.
 */
public class Exit extends RampSegment {

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
