package edu.kit.rose.infrastructure;

/**
 * A range consists of two constants and includes all values between the two.
 * Closed range.
 */
public class Range<T extends Comparable<T>> {
  private final T lowerEndPoint;
  private final T upperEndPoint;

  /**
   * Constructor. Provided bounds are sorted automatically.
   *
   * @param lowerEndPoint first bound
   * @param upperEndPoint second bound.
   */
  public Range(T lowerEndPoint, T upperEndPoint) {
    if (lowerEndPoint.compareTo(upperEndPoint) > 0) {
      this.upperEndPoint = lowerEndPoint;
      this.lowerEndPoint = upperEndPoint;
    } else {
      this.upperEndPoint = upperEndPoint;
      this.lowerEndPoint = lowerEndPoint;
    }
  }

  /**
   * Returns true in case the provided value is in thi range. false otherwise.
   *
   * @param value the value in question
   * @return true: is in range,   false: is not in range
   */
  public boolean contains(T value) {
    var aboveLowerEndPoint = lowerEndPoint.compareTo(value) <= 0;
    var belowHigherEndPoint = value.compareTo(upperEndPoint) <= 0;
    return aboveLowerEndPoint && belowHigherEndPoint;
  }
}
