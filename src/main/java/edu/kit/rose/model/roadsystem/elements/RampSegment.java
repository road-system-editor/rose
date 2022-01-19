package edu.kit.rose.model.roadsystem.elements;

/**
 * A {@link Base} segment with a ramp {@link Connector}.
 */
abstract class RampSegment extends Base {

  /**
   * Returns the ramp {@link Connector} of the RampSegment.
   *
   * @return the ramp Connector.
   */
  public Connector getRamp() {
    return null;
  }
}
