package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SimpleBox;
import edu.kit.rose.infrastructure.SimpleUnitObservable;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A base Class for a {@link Segment} that implements the basic functionality all Segments share.
 */
public abstract class HighwaySegment implements Segment {

  //Attributes for the Element Interface
  private SortedBox<AttributeAccessor<?>> attributeAccessors;
  private String name;
  private boolean isContainer;
  //Attributes for the Segment Interface
  private Position position;
  private SegmentType segmentType;
  private SortedBox<Measurement<?>> measurements;
  private Connector entryConnector;
  private Connector exitConnector;

  private Date date;

  HighwaySegment() {
    this.date = new Date();
    this.isContainer = false;
  }

  HighwaySegment(String name) {
    this.date = new Date();
    this.name = name;
    this.isContainer = false;
  }

  /**
   * Provides the entry Connector for this Segment.
   *
   * @return the entry Connector for this Segment.
   */
  public Connector getEntry() {
    return entryConnector;
  }

  /**
   * Provides the exit Connector for this Segment.
   *
   * @return the exit Connector for this Segment.
   */
  public Connector getExit() {
    return this.entryConnector;
  }

  @Override
  public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
    return this.attributeAccessors;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean isContainer() {
    return this.isContainer;
  }

  @Override
  public SegmentType getSegmentType() {
    return this.segmentType;
  }

  @Override
  public SortedBox<Measurement<?>> getMeasurements() {
    return this.measurements;
  }

  @Override
  public Box<Connector> getConnectors() {
    return new SimpleBox<Connector>(Arrays.asList(this.entryConnector, this.exitConnector));
  }

  @Override
  public Position getCenter() {
    return this.position;
  }

  @Override
  public void move(Movement movement) {
    position.setX(position.getX() + movement.getX());
    position.setY(position.getY() + movement.getY());
  }

  @Override
  public int compareTo(Segment segment) {
    return date.compareTo(segment.getSegmentDate());
  }

  @Override
  public Element getThis() {
    return this;
  }

  @Override
  public Date getSegmentDate() {
    return this.date;
  }
}
