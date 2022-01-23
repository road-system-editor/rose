package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SimpleBox;
import edu.kit.rose.infrastructure.SimpleSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A base Class for a {@link Segment} that implements the basic functionality all Segments share.
 */
public abstract class HighwaySegment implements Segment {

  private static final int INITIAL_CONNECTOR_DISTANCE_TO_CENTER = 50;

  protected final Set<AttributeAccessor<?>> attributeAccessors = new HashSet<>();
  protected final Set<Connector> connectors = new HashSet<>();
  protected final Set<Measurement<?>> measurements = new HashSet<>();

  private final SegmentType segmentType;

  private String name;
  private int length = 2 * INITIAL_CONNECTOR_DISTANCE_TO_CENTER;
  private int pitch = 0;
  private int nrOfEntryLanes = 1;
  private int nrOfExitLanes = 1;
  private boolean conurbation = false;
  private int speedLimit = 100;
  private Position center = new Position(0, 0);

  private Connector entryConnector;
  private Connector exitConnector;

  private final Long creationTime;


  HighwaySegment(SegmentType segmentType) {
    this(segmentType, segmentType.name());
  }

  HighwaySegment(SegmentType segmentType, String name) {
    this.creationTime = System.nanoTime();
    this.segmentType = segmentType;
    this.name = name;
    init();
  }

  private void init() {

    //Create all AttributeAccessors.
    AttributeAccessor<String> nameAccessor = new AttributeAccessor<>();
    attributeAccessors.add(nameAccessor);
    AttributeAccessor<Integer> lengthAccessor = new AttributeAccessor<>();
    attributeAccessors.add(lengthAccessor);
    AttributeAccessor<Integer> pitchAccessor = new AttributeAccessor<>();
    attributeAccessors.add(pitchAccessor);
    AttributeAccessor<Integer> nrOfEntryLanesAccessor = new AttributeAccessor<>();
    attributeAccessors.add(nrOfEntryLanesAccessor);
    AttributeAccessor<Integer> nrOfExitLanesAccessor = new AttributeAccessor<>();
    attributeAccessors.add(nrOfExitLanesAccessor);
    AttributeAccessor<Boolean> conurbationAccessor = new AttributeAccessor<>();
    attributeAccessors.add(conurbationAccessor);
    AttributeAccessor<Integer> speedLimitAccessor = new AttributeAccessor<>();
    attributeAccessors.add(speedLimitAccessor);

    //TODO: Redo when AttributeAccessors are implemented

    List<AttributeAccessor<?>> entryAttributesList =
        Arrays.asList(lengthAccessor, nrOfEntryLanesAccessor);

    List<AttributeAccessor<?>> exitAttributesList =
        Arrays.asList(lengthAccessor, nrOfExitLanesAccessor);

    initConnectors(entryAttributesList, exitAttributesList);
  }

  private void initConnectors(List<AttributeAccessor<?>> entryAttributesList,
                              List<AttributeAccessor<?>> exitAttributesList) {
    if (this.getSegmentType() == SegmentType.BASE) {
      this.entryConnector = new MoveableConnector(ConnectorType.ENTRY,
          new Position(center.getX() - INITIAL_CONNECTOR_DISTANCE_TO_CENTER,
              center.getY()),
          entryAttributesList);
      this.exitConnector = new MoveableConnector(ConnectorType.EXIT,
          new Position(center.getX() + INITIAL_CONNECTOR_DISTANCE_TO_CENTER,
              center.getY()),
          exitAttributesList);
    } else {
      this.entryConnector = new Connector(ConnectorType.ENTRY,
          new Position(center.getX() - INITIAL_CONNECTOR_DISTANCE_TO_CENTER,
              center.getY()),
          entryAttributesList);
      this.exitConnector = new Connector(ConnectorType.EXIT,
          new Position(center.getX() + INITIAL_CONNECTOR_DISTANCE_TO_CENTER,
              center.getY()),
          exitAttributesList);
    }
    connectors.add(entryConnector);
    connectors.add(exitConnector);
  }

  /**
   * Provides the entry Connector for this Segment.
   *
   * @return the entry Connector for this Segment.
   */
  public Connector getEntry() {
    return this.entryConnector;
  }

  /**
   * Provides the exit Connector for this Segment.
   *
   * @return the exit Connector for this Segment.
   */
  public Connector getExit() {
    return this.exitConnector;
  }

  @Override
  public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
    return new SimpleSortedBox<>(new ArrayList<>(this.attributeAccessors));
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
    return new SimpleSortedBox<>(new ArrayList<>(this.measurements));
  }

  @Override
  public Box<Connector> getConnectors() {
    return new SimpleBox<>(new ArrayList<>(this.connectors));
  }

  @Override
  public Position getCenter() {
    return this.center;
  }

  @Override
  public void move(Movement movement) {
    center.setX(center.getX() + movement.getX());
    center.setY(center.getY() + movement.getY());
    connectors.forEach(c -> c.move(movement));
  }

  @Override
  public int compareTo(Segment segment) {
    //TODO: make more easy to look at
    try {
      var highWaySegment = (HighwaySegment) segment;
      return creationTime.compareTo(highWaySegment.getCreationTime());
    } catch (ClassCastException e) {
      return segment.compareTo(this);
    }
  }

  @Override
  public Element getThis() {
    return this;
  }

  private Long getCreationTime() {
    return this.creationTime;
  }
}
