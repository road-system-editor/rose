package edu.kit.rose.model.roadsystem.elements;


import edu.kit.rose.infrastructure.UnitObserver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a one way road. (as in part of a freeway)
 * A Base {@link Segment} is a simple {@link Segment} that only has one entrance and one exit.
 */
public class Base extends HighwaySegment implements Segment {

  private final Set<UnitObserver<Element>> observers = new HashSet<>();

  /**
   * Standard Constructor.
   * Initializes all values to default ones.
   */
  public Base() {
    super(SegmentType.BASE);
  }

  /**
   * Constructor.
   * Uses the name and initializes all values to default ones.
   *
   * @param name the name for the Base Segment
   */
  public Base(String name) {
    super(SegmentType.BASE, name);
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
