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
 * Represents a freeway entrance.
 * An Entrance Segment is a {@link Segment} that provides one entrance from the main road
 * and one exit from it
 * as well as a ramp by which cars can enter the Road.
 */
public class Entrance extends RampSegment {

  private final Set<UnitObserver<Element>> observers = new HashSet<>();

  /**
   * Standard Constructor.
   * Initializes all values to default ones.
   */
  public Entrance() {
    super(SegmentType.ENTRANCE);
  }

  /**
   * Constructor.
   * Uses the name and initializes all values to default ones.
   *
   * @param name the name for the Entrance Segment
   */
  public Entrance(String name) {
    super(SegmentType.ENTRANCE, name);
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
    this.rampConnector = new Connector(ConnectorType.RAMP_ENTRY, rampPosition, rampAttributesList);
  }
}
