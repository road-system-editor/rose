package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;

/**
 * A {@link Base} segment with a ramp {@link Connector}.
 */
abstract class RampSegment extends Base {

  //TODO: add attributes for rampConnector

  private Connector rampConnector;

  public RampSegment(SegmentType segmentType) {
    super(segmentType);
    initRamp();
    connectors.add(rampConnector);
  }

  public RampSegment(SegmentType segmentType, String name) {
    super(segmentType, name);
  }

  private static void initRamp() {
    //TODO: implement, needs to set up ramp connector position and add attributeAccessors
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
