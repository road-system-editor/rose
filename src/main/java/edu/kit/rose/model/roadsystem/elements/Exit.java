package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a freeway exit.
 * An Exit Segment is a {@link Segment} that has one entrance to the main road and one exit from it
 * as well as a singular ramp by which cars can leave the Road.
 */
public class Exit extends RampSegment {

  private final Set<UnitObserver<Element>> observers = new HashSet<>();

  //TODO: add javadoc
  /**
   *
   */
  public Exit() {
    super(SegmentType.EXIT);
  }

  //TODO: add javadoc
  /**
   * @param name
   */
  public Exit(String name) {
    super(SegmentType.EXIT, name);
  }

  @Override
  public void addSubscriber(UnitObserver<Element> observer) {
    observers.add(observer);
  }

  @Override
  public void removeSubscriber(UnitObserver<Element> observer) {
    observers.remove(observer);
  }

  @Override
  public void notifySubscribers() {
    observers.forEach(o -> o.notifyChange(this));
  }
}
