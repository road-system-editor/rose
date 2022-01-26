package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A base Class for a {@link Segment} that implements the basic functionality all Segments share.
 */
public abstract class HighwaySegment
    extends RoseSetObservable<Element, Element>
    implements Segment {

  protected static final int INITIAL_CONNECTOR_DISTANCE_TO_CENTER = 50;

  protected final ArrayList<AttributeAccessor<?>> attributeAccessors =
      new ArrayList<>();
  protected final Set<Connector> connectors = new HashSet<>();
  protected final Set<Measurement<?>> measurements = new HashSet<>();

  private final SegmentType segmentType;
  private final Position center = new Position(0, 0);
  private final Long creationTime;

  protected Connector entryConnector;
  protected Connector exitConnector;

  private String name;
  private int length = 2 * INITIAL_CONNECTOR_DISTANCE_TO_CENTER;
  private int pitch = 0;
  private int nrOfEntryLanes = 1;
  private int nrOfExitLanes = 1;
  private boolean conurbation = false;
  private int speedLimit = 100;




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
    AttributeAccessor<String> nameAccessor =
        new AttributeAccessor<>(AttributeType.NAME, () -> name,
            s -> name = s);
    attributeAccessors.add(nameAccessor);
    AttributeAccessor<Integer> lengthAccessor =
        new AttributeAccessor<>(AttributeType.LENGTH, () -> length,
            s -> length = s);
    attributeAccessors.add(lengthAccessor);
    AttributeAccessor<Integer> pitchAccessor =
        new AttributeAccessor<>(AttributeType.SLOPE, () -> pitch,
            s -> pitch = s);
    attributeAccessors.add(pitchAccessor);
    AttributeAccessor<Integer> nrOfEntryLanesAccessor =
        new AttributeAccessor<>(AttributeType.LANE_COUNT, () -> nrOfEntryLanes,
            s -> nrOfEntryLanes = s);
    attributeAccessors.add(nrOfEntryLanesAccessor);
    AttributeAccessor<Integer> nrOfExitLanesAccessor =
        new AttributeAccessor<>(AttributeType.LANE_COUNT, () -> nrOfExitLanes,
            s -> nrOfExitLanes = s);
    attributeAccessors.add(nrOfExitLanesAccessor);
    AttributeAccessor<Boolean> conurbationAccessor =
        new AttributeAccessor<>(AttributeType.CONURBATION, () -> conurbation,
            s -> conurbation = s);
    attributeAccessors.add(conurbationAccessor);
    AttributeAccessor<Integer> speedLimitAccessor =
        new AttributeAccessor<>(AttributeType.MAX_SPEED, () -> speedLimit,
            s -> speedLimit = s);
    attributeAccessors.add(speedLimitAccessor);

    List<AttributeAccessor<?>> entryAttributesList =
        Arrays.asList(lengthAccessor, nrOfEntryLanesAccessor);

    List<AttributeAccessor<?>> exitAttributesList =
        Arrays.asList(lengthAccessor, nrOfExitLanesAccessor);

    initConnectors(entryAttributesList, exitAttributesList);
  }

  protected void initConnectors(List<AttributeAccessor<?>> entryAttributesList,
                                List<AttributeAccessor<?>> exitAttributesList) {
    this.entryConnector = new Connector(ConnectorType.ENTRY,
        new Position(getCenter().getX() - INITIAL_CONNECTOR_DISTANCE_TO_CENTER,
            getCenter().getY()),
        entryAttributesList);
    this.exitConnector = new Connector(ConnectorType.EXIT,
        new Position(getCenter().getX() + INITIAL_CONNECTOR_DISTANCE_TO_CENTER,
            getCenter().getY()),
        exitAttributesList);
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
    return new RoseSortedBox<>(this.attributeAccessors);
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
  public Box<Measurement<?>> getMeasurements() {
    return new RoseBox<>(this.measurements);
  }

  @Override
  public Box<Connector> getConnectors() {
    return new RoseBox<>(this.connectors);
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
    notifySubscribers();
  }

  @Override
  public int compareTo(Segment segment) {
    try {
      var highWaySegment = (HighwaySegment) segment;
      return creationTime.compareTo(highWaySegment.getCreationTime());
    } catch (ClassCastException e) {
      return segment.compareTo(this);
    }
  }

  @Override
  public HighwaySegment getThis() {
    return this;
  }

  private Long getCreationTime() {
    return this.creationTime;
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
}
