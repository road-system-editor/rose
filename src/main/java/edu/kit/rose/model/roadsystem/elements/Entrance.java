package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;

/**
 * Represents a freeway entrance.
 * An Entrance Segment is a {@link Segment} that provides one entrance from the main road
 * and one exit from it
 * as well as a ramp by which cars can enter the Road.
 */
public class Entrance extends RampSegment {

  /**
   *
   */
  public Entrance() {
    super();
  }

  /**
   * @param name
   */
  public Entrance(String name) {
    super(name);
  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public void addSubscriber(UnitObserver<Entrance> observer) {

  }

  @Override
  public void removeSubscriber(UnitObserver<Entrance> observer) {

  }

  @Override
  public SegmentType getSegmentType() {
    return SegmentType.ENTRANCE;
  }
}
