package edu.kit.rose.model.roadsystem.attributes;

/**
 * Different types of speed limits as specified in the YAML format.
 */
public enum SpeedLimit {
  /**
   * no speed limit.
   */
  NONE,
  /**
   * 80 km/h.
   */
  T80,
  /**
   * 100 km/h.
   */
  T100,
  /**
   * 120 km/h.
   */
  T120,
  /**
   * route control system - because that's a speed limit too.
   */
  SBA,
  /**
   * tunnel km/h.
   */
  TUNNEL
}
