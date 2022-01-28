package edu.kit.rose.model.roadsystem.elements;

/**
 * A factory for different kinds of {@link Segment}s.
 */
public class SegmentFactory {

  /**
   * creates a new {@link Base} segment.
   *
   * @return the new base segment
   */
  public Base createBaseSegment() {
    return null;
  }

  /**
   * creates a new {@link Exit} segment.
   *
   * @return the new exit segment
   */
  public Exit createExitSegment() {
    return null;
  }

  /**
   * creates a new {@link Entrance} segment.
   *
   * @return the new entrance segment
   */
  public Entrance createEntranceSegment() {
    return null;
  }

  /**
   * Creates a new {@link Segment} and uses the {@link Connector}s,
   * the {@link edu.kit.rose.model.roadsystem.measurements.Measurement}s and
   * the {@link edu.kit.rose.model.roadsystem.attributes.AttributeAccessor}s
   * of the segment parameter for the new segment.
   *
   * @param segment the segment with target {@link Connector}s,
   *                {@link edu.kit.rose.model.roadsystem.measurements.Measurement}s
   *                and {@link edu.kit.rose.model.roadsystem.attributes.AttributeAccessor}s
   * @return the new created segment
   *
   * @throws IllegalArgumentException thrown if segment parameter is null
   */
  public Segment createSegmentFrom(Segment segment) throws IllegalArgumentException {
    if (segment == null) {
      throw new IllegalArgumentException("Can not create a new segment out of an old segment, if"
          + " the old segment is null");
    }

    return switch (segment.getSegmentType()) {
      case BASE -> createBaseFrom((Base) segment);
      case EXIT -> createExitFrom((Exit) segment);
      case ENTRANCE -> createEntranceFrom((Entrance) segment);
    };
  }

  private Base createBaseFrom(Base baseSegment) {
    Base newBaseSegment = new Base();
    return null;
  }

  private Exit createExitFrom(Exit exit) {
    return null;
  }

  private Entrance createEntranceFrom(Entrance entrance) {
    return null;
  }
}
