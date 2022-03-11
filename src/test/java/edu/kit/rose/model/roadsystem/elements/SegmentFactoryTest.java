package edu.kit.rose.model.roadsystem.elements;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SegmentFactory}.
 */
class SegmentFactoryTest {
  @Test
  void testConstructorThrows() {
    assertThrows(UnsupportedOperationException.class, SegmentFactory::new);
  }

  @Test
  void testCreateSegmentWithSegmentTypes() {
    Segment base = SegmentFactory.createSegment(SegmentType.BASE);
    assertNotNull(base);
    assertSame(SegmentType.BASE, base.getSegmentType());

    Segment entrance = SegmentFactory.createSegment(SegmentType.ENTRANCE);
    assertNotNull(entrance);
    assertSame(SegmentType.ENTRANCE, entrance.getSegmentType());

    Segment exit = SegmentFactory.createSegment(SegmentType.EXIT);
    assertNotNull(exit);
    assertSame(SegmentType.EXIT, exit.getSegmentType());
  }

  @Test
  void testCreateSegmentWithNull() {
    assertThrows(NullPointerException.class, () -> SegmentFactory.createSegment(null));
  }
}
