package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import java.util.HashSet;
import java.util.List;
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
   * Standard Constructor.
   * initializes all values to default ones.
   */
  public Exit() {
    super(SegmentType.EXIT);
  }

  /**
   * Constructor.
   * Uses the name and initializes all values to default ones.
   *
   * @param name the name for the Exit Segment
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

  @Override
  protected void initRamp(List<AttributeAccessor<?>> rampAttributesList, Position rampPosition) {
    this.rampConnector = new Connector(ConnectorType.RAMP_EXIT,
        rampPosition, rampAttributesList);
  }
}
