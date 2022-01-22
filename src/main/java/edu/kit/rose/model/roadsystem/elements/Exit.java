package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;

/**
 * Represents a freeway exit.
 * An Exit Segment is a {@link Segment} that has one entrance to the main road and one exit from it
 * as well as a singular ramp by which cars can leave the Road.
 */
public class Exit extends RampSegment {

  /**
   *
   */
  public Exit() {
    super();
  }

  /**
   * @param name
   */
  public Exit(String name) {
    super(name);
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
