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
abstract class RampSegment extends HighwaySegment {

  private static final int INITIAL_RAMP_DISTANCE_TO_CENTER = 50;
  private int nrOfRampLanes = 1;
  private final AttributeAccessor<Integer> nrOfRampLanesAccessor;
  private SpeedLimit rampSpeedLimit = SpeedLimit.NONE;
  private final AttributeAccessor<SpeedLimit> maxSpeedRampAccessor;

  protected Connector rampConnector;

  public RampSegment(SegmentType segmentType) {
    this(segmentType, segmentType.name());
  }

  public RampSegment(SegmentType segmentType, String name) {
    super(segmentType, name);

    this.nrOfRampLanesAccessor = new AttributeAccessor<>(
        AttributeType.LANE_COUNT_RAMP, this::getNrOfRampLanes, this::setNrOfRampLanes);
    this.maxSpeedRampAccessor = new AttributeAccessor<>(
        AttributeType.MAX_SPEED_RAMP, this::getMaxSpeedRamp, this::setMaxSpeedRamp);
    super.attributeAccessors.addAll(List.of(this.nrOfRampLanesAccessor, this.maxSpeedRampAccessor));

    initRamp();
    connectors.add(rampConnector);
  }


  private void initRamp() {
    Position rampConnectorPosition = new Position(this.getCenter().getX(),
        this.getCenter().getY() - INITIAL_RAMP_DISTANCE_TO_CENTER);

    List<AttributeAccessor<?>> rampAttributesList =
        Arrays.asList(nrOfRampLanesAccessor, this.maxSpeedRampAccessor);

    initRampConnector(rampAttributesList, rampConnectorPosition);

    this.connectors.add(rampConnector);
  }

  protected abstract void initRampConnector(List<AttributeAccessor<?>> rampAttributesList,
                                   Position rampPosition);


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
  public int getNrOfRampLanes() {
    return this.nrOfRampLanes;
  }

  /**
   * Sets the {@link AttributeType#LANE_COUNT} for the ramp connector to the given value.
   */
  public void setNrOfRampLanes(int nrOfRampLanes) {
    this.nrOfRampLanes = nrOfRampLanes;

    this.nrOfRampLanesAccessor.notifySubscribers();
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
