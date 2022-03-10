package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link Base} segment with a ramp {@link Connector}.
 */
public abstract class RampSegment extends HighwaySegment {
  protected static final int INITIAL_RAMP_DISTANCE_TO_CENTER_Y = -14;

  private Integer laneCountRamp;
  private final AttributeAccessor<Integer> laneCountRampAccessor;
  private SpeedLimit rampSpeedLimit;
  private final AttributeAccessor<SpeedLimit> maxSpeedRampAccessor;
  private String junctionName;
  private final AttributeAccessor<String> junctionNameAccessor;

  protected Connector rampConnector;

  /**
   * Creates a ramp segment with the given segment type and uses the type name as the segment name.
   *
   * @param segmentType the type enum value the subclass of this class represents.
   */
  protected RampSegment(SegmentType segmentType) {
    this(segmentType, segmentType.name());
  }

  /**
   * Constructor.
   *
   * @param segmentType the type of ramp segment this is
   * @param name the name this ramp segment is to have
   */
  protected RampSegment(SegmentType segmentType, String name) {
    super(segmentType, name);

    this.laneCountRampAccessor = new AttributeAccessor<>(
        AttributeType.LANE_COUNT_RAMP, this::getLaneCountRamp, this::setLaneCountRamp);
    this.maxSpeedRampAccessor = new AttributeAccessor<>(
        AttributeType.MAX_SPEED_RAMP, this::getMaxSpeedRamp, this::setMaxSpeedRamp);
    this.junctionNameAccessor = new AttributeAccessor<>(
        AttributeType.JUNCTION, this::getJunctionName, this::setJunctionName);
    super.attributeAccessors.addAll(
        List.of(this.laneCountRampAccessor, this.junctionNameAccessor, this.maxSpeedRampAccessor));

    initRamp();
    connectors.add(rampConnector);
  }

  private void initRamp() {
    List<AttributeAccessor<?>> rampAttributesList =
        Arrays.asList(laneCountRampAccessor, this.maxSpeedRampAccessor);

    initRampConnector(rampAttributesList);

    this.connectors.add(rampConnector);
  }

  protected abstract void initRampConnector(List<AttributeAccessor<?>> rampAttributesList);

  protected void initConnectors(List<AttributeAccessor<?>> entryAttributesList,
                                List<AttributeAccessor<?>> exitAttributesList) {
    this.entryConnector = new Connector(ConnectorType.ENTRY,
        new Position(0, INITIAL_ENTRY_DISTANCE_TO_CENTER),
        entryAttributesList);
    this.exitConnector = new Connector(ConnectorType.EXIT,
        new Position(0, -INITIAL_EXIT_DISTANCE_TO_CENTER),
        exitAttributesList);
    connectors.add(entryConnector);
    connectors.add(exitConnector);
  }


  /**
   * Returns the ramp {@link Connector} of the RampSegment.
   *
   * @return the ramp Connector.
   */
  public Connector getRamp() {
    return this.rampConnector;
  }

  /**
   * Returns the {@link AttributeType#LANE_COUNT} for the ramp connector.
   */
  public Integer getLaneCountRamp() {
    return this.laneCountRamp;
  }

  /**
   * Sets the {@link AttributeType#LANE_COUNT} for the ramp connector to the given value.
   */
  public void setLaneCountRamp(Integer laneCountRamp) {
    this.laneCountRamp = laneCountRamp;

    this.laneCountRampAccessor.notifySubscribers();
    this.notifySubscribers();
  }

  /**
   * Returns the {@link AttributeType#JUNCTION} for the ramp connector.
   */
  public String getJunctionName() {
    return this.junctionName;
  }

  /**
   * Sets the {@link AttributeType#JUNCTION} for the ramp connector to the given value.
   */
  public void setJunctionName(String junctionName) {
    this.junctionName = junctionName;

    this.junctionNameAccessor.notifySubscribers();
    this.notifySubscribers();
  }

  /**
   * Returns the {@link AttributeType#MAX_SPEED} of the ramp.
   */
  public SpeedLimit getMaxSpeedRamp() {
    return this.rampSpeedLimit;
  }

  /**
   * Sets the {@link AttributeType#MAX_SPEED} of the ramp to the given value.
   */
  public void setMaxSpeedRamp(SpeedLimit maxSpeedRamp) {
    this.rampSpeedLimit = maxSpeedRamp;

    this.maxSpeedRampAccessor.notifySubscribers();
    this.notifySubscribers();
  }
}
