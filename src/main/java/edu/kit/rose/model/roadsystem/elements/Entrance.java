package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;

/**
 * Represents a freeway entrance.
 * An Entrance Segment is a {@link Segment} that provides one entrance from the main road
 * and one exit from it
 * as well as a ramp by which cars can enter the Road.
 */
public class Entrance extends RampSegment {

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
}
