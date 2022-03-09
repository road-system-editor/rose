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
   * 90 km/h.
   */
  T90(90),
  /**
   * 100 km/h.
   */
  T100(100),
  /**
   * 120 km/h.
   */
  T120(120),
  /**
   * 130 km/h.
   */
  T130(130),
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

  /**
   * Provides the Speedlimit as an integer in km/h.
   *
   * @return the Speed limit in km/h. 0 in case the speed limit does not have a fixed value.
   */
  public int getValue() {
    return this.value;
  }
}
