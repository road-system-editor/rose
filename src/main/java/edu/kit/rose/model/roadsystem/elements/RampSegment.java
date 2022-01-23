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

  private static final int INITIAL_RAMP_DISTANCE_TO_CENTER = 50;
  private int nrOfRampLanes = 1;
  private int rampSpeedLimit = 100;

  private Connector rampConnector;

  public RampSegment(SegmentType segmentType) {
    super(segmentType);
    initRamp();
    connectors.add(rampConnector);
  }

  public RampSegment(SegmentType segmentType, String name) {
    super(segmentType, name);
    initRamp();
  }

  private void initRamp() {
    AttributeAccessor<String> nrOfRampLanesAccessor = new AttributeAccessor<>();
    attributeAccessors.add(nrOfRampLanesAccessor);
    AttributeAccessor<String> rampSpeedLimitAccessor = new AttributeAccessor<>();
    attributeAccessors.add(rampSpeedLimitAccessor);
    //TODO: Redo when AttributeAccessors are implemented

    Position rampConnectorPosition = new Position(this.getCenter().getX(),
        this.getCenter().getY() - INITIAL_RAMP_DISTANCE_TO_CENTER);

    List<AttributeAccessor<?>> rampAttributesList =
        Arrays.asList(nrOfRampLanesAccessor, rampSpeedLimitAccessor);

    switch (getSegmentType()) {
      case ENTRANCE -> this.rampConnector = new Connector(ConnectorType.ENTRY,
          rampConnectorPosition, rampAttributesList);
      case EXIT -> this.rampConnector = new Connector(ConnectorType.EXIT,
          rampConnectorPosition, rampAttributesList);
      default -> throw new IllegalArgumentException("Illegal SegmentType");
    }

    this.connectors.add(rampConnector);
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
