package edu.kit.rose.model.roadsystem.elements;

import java.util.Objects;

/**
 * A factory for different kinds of {@link Segment}s.
 */
public final class SegmentFactory {
  /**
   * Instantiating this class is unsupported.
   *
   * @see #createSegment(SegmentType) to create a segment statically.
   */
  SegmentFactory() {
    throw new UnsupportedOperationException(
        "can not instantiate segment factory, use createSegment(SegmentType) statically!");
  }

  /**
   * Creates a new segment for a given segment type.
   *
   * @param segmentType the type of segment to create, may not be {@code null}.
   * @return the new base segment
   */
  public static Segment createSegment(SegmentType segmentType) {
    Objects.requireNonNull(segmentType, "segmentType may not be null!");

    return switch (segmentType) {
      case BASE -> new Base();
      case ENTRANCE -> new Entrance();
      case EXIT -> new Exit();
    };
  }
}
