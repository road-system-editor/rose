package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;

/**
 * Represents a one way road. (as in part of a freeway)
 * A Base {@link Segment} is a simple {@link Segment} that only has one entrance and one exit.
 */
public class Base implements Segment {

  /**
   * Provides the {@link Connector} describing the entrance of the Base Segment.
   *
   * @return the {@link Connector} describing the entrance of the Base Segment.
   */
  Connector getEntry() {
    return null;
  }

  /**
   * Provides the {@link Connector} describing the exit of the base Segment.
   *
   * @return the {@link Connector} describing the exit of the base Segment.
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
  public void rotate(int degrees) {

  }

  @Override
  public int getRotation() {
    return 0;
  }

  @Override
  public Position getRotatedConnectorPosition(Connector connector) {
    return null;
  }

  @Override
  public int compareTo(Segment o) {
    return 0;
  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public Element getThis() {
    return this;
  }

  @Override
  public void addSubscriber(UnitObserver<Element> observer) {

  }

  @Override
  public void removeSubscriber(UnitObserver<Element> observer) {

  }
}
