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
public class Base extends HighwaySegment implements UnitObserver<Connector> {

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
            getCenter().getY() + INITIAL_ENTRY_DISTANCE_TO_CENTER),
        entryAttributesList);
    this.entryConnector.addSubscriber(this);
    this.exitConnector = new MovableConnector(ConnectorType.EXIT,
        new Position(getCenter().getX(),
            getCenter().getY() - INITIAL_EXIT_DISTANCE_TO_CENTER),
        exitAttributesList);
    this.exitConnector.addSubscriber(this);
    connectors.add(entryConnector);
    connectors.add(exitConnector);
  }

  @Override
  public void notifyChange(Connector unit) {
    Position absolutExitConnectorPosition = new Position(
        getCenter().getX() + getExit().getPosition().getX(),
        getCenter().getY() + getExit().getPosition().getY());

    Position absolutEntryConnectorPosition = new Position(
        getCenter().getX() + getEntry().getPosition().getX(),
        getCenter().getY() + getEntry().getPosition().getY());

    Position newCenter = getCenterBetweenPositions(
        absolutEntryConnectorPosition,
        absolutExitConnectorPosition);

    getCenter().setX(newCenter.getX());
    getCenter().setY(newCenter.getY());

    Position newRelativeExitConnectorPosition = new Position(
        absolutExitConnectorPosition.getX() - newCenter.getX(),
        absolutExitConnectorPosition.getY() - newCenter.getY());

    Position newRelativeEntryConnectorPosition = new Position(
        absolutEntryConnectorPosition.getX() - newCenter.getX(),
        absolutEntryConnectorPosition.getY() - newCenter.getY());

    ((MovableConnector) exitConnector).setPosition(newRelativeExitConnectorPosition);
    ((MovableConnector) entryConnector).setPosition(newRelativeEntryConnectorPosition);
  }

  private Position getCenterBetweenPositions(Position position1, Position position2) {
    double diffX = position1.getX() - position2.getX();
    double diffY = position1.getY() - position2.getY();

    return new Position(
        position2.getX() + diffX / 2,
        position2.getY() + diffY / 2);
  }


}
