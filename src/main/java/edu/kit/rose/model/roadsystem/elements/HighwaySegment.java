package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SimpleBox;
import edu.kit.rose.infrastructure.SimpleSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A base Class for a {@link Segment} that implements the basic functionality all Segments share.
 */
public abstract class HighwaySegment implements Segment {

  //Attributes for the Element Interface
  protected List<AttributeAccessor<?>> attributeAccessors;
  private String name;
  private boolean isContainer;
  //Attributes for the Segment Interface
  private Position position;
  protected SegmentType segmentType;
  protected List<Measurement<?>> measurements;
  private Connector entryConnector;
  private Connector exitConnector;

  private final Date date;

  HighwaySegment() {
    this.date = new Date();
    this.isContainer = false;
    this.position = new Position(0, 0);
    initializeAttributesAndConnectors(name);

    //TODO: set Position
  }

  HighwaySegment(String name) {
    this.date = new Date();
    this.isContainer = false;
    this.position = new Position(0, 0);
    initializeAttributesAndConnectors(name);
  }

  private void initializeAttributesAndConnectors(String name) {
    final int standardLength = 100;
    final int standardPitch = 0;
    final int standardNrOfLanes = 1;
    final boolean standardConurbation = false;
    final int standardSpeedLimit = 100;
    final int standardxcoordinate = 0;
    final int standardyCoordinate = 0;

    //Create all AttributeAccessors.
    AttributeAccessor<String> nameAttribute = new AttributeAccessor<String>();
    nameAttribute.setValue(name);
    AttributeAccessor<Integer> lengthAttribute = new AttributeAccessor<Integer>();
    lengthAttribute.setValue(standardLength);
    AttributeAccessor<Integer> pitchAttribute = new AttributeAccessor<Integer>();
    pitchAttribute.setValue(standardPitch);
    AttributeAccessor<Integer> nrOfEntryLanesAttribute = new AttributeAccessor<Integer>();
    nrOfEntryLanesAttribute.setValue(standardNrOfLanes);
    AttributeAccessor<Integer> nrOfExitLanesAttribute = new AttributeAccessor<Integer>();
    nrOfExitLanesAttribute.setValue(standardNrOfLanes);
    AttributeAccessor<Boolean> conurbationAttribute = new AttributeAccessor<Boolean>();
    conurbationAttribute.setValue(standardConurbation);
    AttributeAccessor<Integer> speedLimitAttribute = new AttributeAccessor<Integer>();
    speedLimitAttribute.setValue(standardSpeedLimit);

    //TODO: AttributeAccessor need Constructor that takes AttributeType.

    //Create Lists out of the Attribute Accessors.
    List<AttributeAccessor<?>> attributeAccessorList = Arrays.asList(nameAttribute,
        lengthAttribute, pitchAttribute, nrOfEntryLanesAttribute, nrOfExitLanesAttribute,
        conurbationAttribute,
        speedLimitAttribute);

    List<AttributeAccessor<?>> entryAttributesList =
        Arrays.asList(lengthAttribute, nrOfEntryLanesAttribute);

    List<AttributeAccessor<?>> exitAttributesList =
        Arrays.asList(lengthAttribute, nrOfExitLanesAttribute);

    //Set the lists as the attributes.
    this.attributeAccessors = new ArrayList<>(attributeAccessorList);
    this.entryConnector = new Connector(ConnectorType.ENTRY,
        new Position(standardxcoordinate - standardLength / 2,
            standardyCoordinate - standardLength / 2),
        entryAttributesList);
    this.exitConnector = new Connector(ConnectorType.EXIT,
        new Position(standardxcoordinate + standardLength / 2,
            standardyCoordinate + standardLength / 2), exitAttributesList);

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
    return new SimpleSortedBox<>(this.attributeAccessors);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean isContainer() {
    return false;
  }

  @Override
  public SegmentType getSegmentType() {
    return this.segmentType;
  }

  @Override
  public SortedBox<Measurement<?>> getMeasurements() {
    return new SimpleSortedBox<>(this.measurements);
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
