package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;

/**
 * A {@link Base} segment with a ramp {@link Connector}.
 */
abstract class RampSegment extends Base {

  private Connector rampConnector;

  public RampSegment() {
    super();
  }

  public RampSegment(String name) {
    super(name);
    AttributeAccessor<?> rampLaneCount = new AttributeAccessor<>();
    AttributeAccessor<?> rampSpeedLimit = new AttributeAccessor<>();
    this.attributeAccessors.add(rampLaneCount);
    this.attributeAccessors.add(rampSpeedLimit);
  }

  /**
   * Returns the ramp {@link Connector} of the RampSegment.
   *
   * @return the ramp Connector.
   */
  public Connector getRamp() {
    return null;
  }


}
