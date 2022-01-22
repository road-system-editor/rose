package edu.kit.rose.model.roadsystem.elements;


import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Represents a one way road. (as in part of a freeway)
 * A Base {@link Segment} is a simple {@link Segment} that only has one entrance and one exit.
 */
public class Base extends HighwaySegment implements Segment, UnitObservable<Base> {

  private List<UnitObserver<Base>> observerList;
  private static SegmentType segmentType = SegmentType.BASE;

  /**
   *
   */
  public Base() {
    super();
  }

  /**
   * @param name
   */
  public Base(String name) {
    super(name);
    this.observerList = new ArrayList<>();
  }

  @Override
  public void addSubscriber(UnitObserver<Base> observer) {
    observerList.add(observer);
  }

  @Override
  public void removeSubscriber(UnitObserver<Base> observer) {

  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public SegmentType getSegmentType() {
    return SegmentType.BASE;
  }
}
