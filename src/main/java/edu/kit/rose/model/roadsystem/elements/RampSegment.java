package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link Base} segment with a ramp {@link Connector}.
 */
abstract class RampSegment extends HighwaySegment {

  protected static final int INITIAL_RAMP_DISTANCE_TO_CENTER_Y = -14;
  private int nrOfRampLanes = 1;
  private int rampSpeedLimit = 100;

  protected Connector rampConnector;

  public RampSegment(SegmentType segmentType) {
    this(segmentType, segmentType.name());
  }

  public RampSegment(SegmentType segmentType, String name) {
    super(segmentType, name);
    initRamp();
    connectors.add(rampConnector);
  }


  private void initRamp() {
    AttributeAccessor<Integer> nrOfRampLanesAccessor =
        new AttributeAccessor<>(AttributeType.LANE_COUNT_RAMP, () -> nrOfRampLanes,
            s -> nrOfRampLanes = s);
    attributeAccessors.add(nrOfRampLanesAccessor);
    AttributeAccessor<Integer> rampSpeedLimitAccessor =
        new AttributeAccessor<>(AttributeType.MAX_SPEED_RAMP,
            () -> rampSpeedLimit,
              s -> rampSpeedLimit = s);
    attributeAccessors.add(rampSpeedLimitAccessor);

    List<AttributeAccessor<?>> rampAttributesList =
        Arrays.asList(nrOfRampLanesAccessor, rampSpeedLimitAccessor);

    initRampConnector(rampAttributesList);

    this.connectors.add(rampConnector);
  }

  protected abstract void initRampConnector(List<AttributeAccessor<?>> rampAttributesList);

  protected void initConnectors(List<AttributeAccessor<?>> entryAttributesList,
                                List<AttributeAccessor<?>> exitAttributesList) {
    this.entryConnector = new Connector(ConnectorType.ENTRY,
        new Position(0, INITIAL_CONNECTOR_DISTANCE_TO_CENTER),
        entryAttributesList);
    this.exitConnector = new Connector(ConnectorType.EXIT,
        new Position(0, -INITIAL_CONNECTOR_DISTANCE_TO_CENTER),
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


}
