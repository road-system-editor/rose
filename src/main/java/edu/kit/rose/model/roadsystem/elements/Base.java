package edu.kit.rose.model.roadsystem.elements;


import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a one way road. (as in part of a freeway)
 * A Base {@link Segment} is a simple {@link Segment} that only has one entrance and one exit.
 */
public class Base extends HighwaySegment {

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
  protected void initConnectors(List<AttributeAccessor<?>> entryAttributesList,
                                List<AttributeAccessor<?>> exitAttributesList) {
    this.entryConnector = new MovableConnector(ConnectorType.ENTRY,
        new Position(getCenter().getX(),
            getCenter().getY() + INITIAL_CONNECTOR_DISTANCE_TO_CENTER),
        entryAttributesList);
    this.exitConnector = new MovableConnector(ConnectorType.EXIT,
        new Position(getCenter().getX(),
            getCenter().getY() - INITIAL_CONNECTOR_DISTANCE_TO_CENTER),
        exitAttributesList);
    connectors.add(entryConnector);
    connectors.add(exitConnector);
  }
}
