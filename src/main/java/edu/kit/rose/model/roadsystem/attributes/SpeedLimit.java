package edu.kit.rose.model.roadsystem.attributes;

/**
 * Different types of speed limits as specified in the YAML format.
 */
public enum SpeedLimit {
  /**
   * no speed limit.
   */
  NONE(0),
  /**
   * 60km/h.
   */
  T60(60),
  /**
   * 80 km/h.
   */
  T80(80),
  /**
   * 100 km/h.
   */
  T100(100),
  /**
   * 120 km/h.
   */
  T120(120),
  /**
   * route control system - because that's a speed limit too.
   */
  SBA(0),
  /**
   * tunnel km/h.
   */
  TUNNEL(0);

  private final int value;

  SpeedLimit(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }
}
