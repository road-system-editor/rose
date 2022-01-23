package edu.kit.rose.model.roadsystem.elements;

/**
 * A factory for different kinds of {@link Segment}s.
 */
public class SegmentFactory {

  /**
   * creates a new segment by type.
   *
   * @return the new base segment
   */
  public static Segment createSegment(SegmentType segmentType) {
    return switch (segmentType) {
      case BASE -> new Base();
      case ENTRANCE -> new Entrance();
      case EXIT -> new Exit();
    };
  }
}
