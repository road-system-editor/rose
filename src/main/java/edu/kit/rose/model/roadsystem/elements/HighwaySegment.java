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

  protected final List<AttributeAccessor<?>> attributeAccessors = new ArrayList<>();
  protected final Set<Connector> connectors = new HashSet<>();
  protected final Set<Measurement<?>> measurements = new HashSet<>();

  private final SegmentType segmentType;
  private final Position center = new Position(0, 0);
  private final Long creationTime;

  protected Connector entryConnector;
  protected Connector exitConnector;

  private String name;
  private final AttributeAccessor<String> nameAccessor;
  private String comment;
  private final AttributeAccessor<String> commentAccessor;
  private int length = 2 * INITIAL_CONNECTOR_DISTANCE_TO_CENTER;
  private final AttributeAccessor<Integer> lengthAccessor;
  private int pitch = 0;
  private final AttributeAccessor<Integer> slopeAccessor;
  private int laneCount = 1;
  private final AttributeAccessor<Integer> laneCountAccessor;
  private boolean conurbation = false;
  private final AttributeAccessor<Boolean> conurbationAccessor;
  private int speedLimit = 100;
  private final AttributeAccessor<Integer> speedLimitAccessor;

  private int rotation = 0;

  HighwaySegment(SegmentType segmentType) {
    this(segmentType, segmentType.name());
  }

  HighwaySegment(SegmentType segmentType, String name) {
    this.creationTime = System.nanoTime();
    this.segmentType = segmentType;
    this.name = name;

    this.nameAccessor = new AttributeAccessor<>(AttributeType.NAME, this::getName, this::setName);
    this.commentAccessor = new AttributeAccessor<>(
        AttributeType.COMMENT, this::getComment, this::setComment);
    this.lengthAccessor = new AttributeAccessor<>(
        AttributeType.LENGTH, this::getLength, this::setLength);
    this.slopeAccessor = new AttributeAccessor<>(
        AttributeType.SLOPE, this::getSlope, this::setSlope);
    this.laneCountAccessor = new AttributeAccessor<>(
        AttributeType.LANE_COUNT, this::getLaneCount, this::setLaneCount);
    this.conurbationAccessor = new AttributeAccessor<>(
        AttributeType.CONURBATION, this::getConurbation, this::setConurbation);
    this.speedLimitAccessor = new AttributeAccessor<>(
        AttributeType.MAX_SPEED, this::getMaxSpeed, this::setMaxSpeed);

    init();
  }

  private void init() {
    this.attributeAccessors.addAll(List.of(
        this.nameAccessor,
        this.commentAccessor,
        this.lengthAccessor,
        this.slopeAccessor,
        this.laneCountAccessor,
        this.conurbationAccessor,
        this.speedLimitAccessor
    ));

    List<AttributeAccessor<?>> entryAttributesList = List.of(lengthAccessor);

    List<AttributeAccessor<?>> exitAttributesList = List.of(lengthAccessor);

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
    this.rotation = Math.floorMod(this.rotation + degrees, 360);
    this.subscribers.forEach(s -> s.notifyChange(this));
  }

  @Override
  public int getRotation() {
    return this.rotation;
  }

  @Override
  public Position getRotatedConnectorPosition(Connector connector) {
    if (!this.connectors.contains(connector)) {
      throw new IllegalArgumentException("connector is not part of this segment");
    }

    var s = Math.sin(Math.toRadians(rotation));
    var c = Math.cos(Math.toRadians(rotation));

    var x = connector.getPosition().getX();
    var y = connector.getPosition().getY();

    // translate point back to origin:
    x -= center.getX();
    y -= center.getY();

    // rotate point
    double newX = y * s - x * c;
    double newY = -x * s - y * c;

    // translate point back:
    return new Position((int) Math.round(newX + center.getX()),
        (int) Math.round(newY + center.getY()));
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;

    this.nameAccessor.notifySubscribers();
    this.notifySubscribers();
  }

  @Override
  public String getComment() {
    return this.comment;
  }

  @Override
  public void setComment(String comment) {
    this.comment = comment;

    this.commentAccessor.notifySubscribers();
    this.notifySubscribers();
  }

  /**
   * Returns the {@link AttributeType#LENGTH} of this highway segment.
   */
  public Integer getLength() {
    return this.length;
  }

  /**
   * Sets the {@link AttributeType#LENGTH} of this highway segment to the given {@code length}.
   */
  public void setLength(Integer length) {
    this.length = length;

    this.lengthAccessor.notifySubscribers();
    this.notifySubscribers();
  }

  /**
   * Returns the {@link AttributeType#SLOPE} of this highway segment.
   */
  public Integer getSlope() {
    return this.pitch;
  }

  /**
   * Sets the {@link AttributeType#SLOPE} of this highway segment to the given {@code slope}.
   */
  public void setSlope(Integer slope) {
    this.pitch = slope;

    this.slopeAccessor.notifySubscribers();
    this.notifySubscribers();
  }

  /**
   * Returns the {@link AttributeType#LANE_COUNT} for the entry connector.
   */
  public int getLaneCount() {
    return this.laneCount;
  }

  /**
   * Sets the {@link AttributeType#LANE_COUNT} for the entry connector to the given value.
   */
  public void setLaneCount(int laneCount) {
    this.laneCount = laneCount;

    this.laneCountAccessor.notifySubscribers();
    this.notifySubscribers();
  }

  /**
   * Returns the {@link AttributeType#CONURBATION} of this highway segment.
   */
  public boolean getConurbation() {
    return this.conurbation;
  }

  /**
   * Sets the {@link AttributeType#CONURBATION} of this highway segment to the given value.
   */
  public void setConurbation(boolean conurbation) {
    this.conurbation = conurbation;

    this.conurbationAccessor.notifySubscribers();
    this.notifySubscribers();
  }

  /**
   * Returns the {@link AttributeType#MAX_SPEED} of this highway segment.
   */
  public int getMaxSpeed() {
    return speedLimit;
  }

  /**
   * Sets the {@link AttributeType#MAX_SPEED} of this highway segment to the given value.
   */
  public void setMaxSpeed(int maxSpeed) {
    this.speedLimit = maxSpeed;

    this.speedLimitAccessor.notifySubscribers();
    this.notifySubscribers();
  }
}
