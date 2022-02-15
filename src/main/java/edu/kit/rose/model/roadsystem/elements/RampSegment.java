package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link Base} segment with a ramp {@link Connector}.
 */
public abstract class RampSegment extends HighwaySegment {

  protected static final int INITIAL_RAMP_DISTANCE_TO_CENTER_Y = -14;
  private int nrOfRampLanes = 1;
  private final AttributeAccessor<Integer> nrOfRampLanesAccessor;
  private int rampSpeedLimit = 100;
  private final AttributeAccessor<Integer> maxSpeedRampAccessor;

  protected Connector rampConnector;

  public RampSegment(SegmentType segmentType) {
    this(segmentType, segmentType.name());
  }

  /**
   * Constructor.
   *
   * @param segmentType the type of ramp segment this is
   * @param name the name this ramp segment is to have
   */
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
    List<AttributeAccessor<?>> rampAttributesList =
        Arrays.asList(nrOfRampLanesAccessor, this.maxSpeedRampAccessor);

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
  public int getMaxSpeedRamp() {
    return this.rampSpeedLimit;
  }

  /**
   * Sets the {@link AttributeType#MAX_SPEED} of the ramp to the given value.
   */
  public void setMaxSpeedRamp(int maxSpeedRamp) {
    this.rampSpeedLimit = maxSpeedRamp;

    this.maxSpeedRampAccessor.notifySubscribers();
    this.notifySubscribers();
  }
}
