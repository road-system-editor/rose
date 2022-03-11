package edu.kit.rose.controller.commons;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 * Contains unit test for the {@link HierarchyCopier} class.
 */
public class HierarchyCopierTest {

  private static final double DOUBLE_COMPARE_THRESHOLD = 0.005;

  private Base sourceSegment;

  private HierarchyCopier hierarchyCopier;

  private RoadSystem roadSystem;

  /**
   * Sets up the {@link HierarchyCopier} and the {@link Base}.
   */
  @BeforeEach
  public void setUp() {
    this.sourceSegment = new Base();
    sourceSegment.getExit().move(new Movement(100, 200));
    sourceSegment.getEntry().move(new Movement(-100, -200));
    this.sourceSegment.move(new Movement(500, 500));

    this.roadSystem = Mockito.mock(RoadSystem.class);
    Mockito.when(this.roadSystem.createSegment(ArgumentMatchers.any(SegmentType.class)))
        .thenReturn(new Base());

    this.hierarchyCopier = new HierarchyCopier(null, this.roadSystem);
  }

  @Test
  public void testCopyBaseSegmentPositionData() {
    Base baseCopy = (Base) this.hierarchyCopier.copySegment(this.sourceSegment);

    Assertions.assertTrue(Math.abs(baseCopy.getCenter().getX()
        - sourceSegment.getCenter().getX()) < DOUBLE_COMPARE_THRESHOLD);

    Assertions.assertTrue(Math.abs(baseCopy.getCenter().getY()
        - sourceSegment.getCenter().getY()) < DOUBLE_COMPARE_THRESHOLD);

    Assertions.assertTrue(Math.abs(baseCopy.getEntry().getPosition().getX()
        - sourceSegment.getEntry().getPosition().getX()) < DOUBLE_COMPARE_THRESHOLD);

    Assertions.assertTrue(Math.abs(baseCopy.getEntry().getPosition().getY()
        - sourceSegment.getEntry().getPosition().getY()) < DOUBLE_COMPARE_THRESHOLD);

    Assertions.assertTrue(Math.abs(baseCopy.getExit().getPosition().getX()
        - sourceSegment.getExit().getPosition().getX()) < DOUBLE_COMPARE_THRESHOLD);

    Assertions.assertTrue(Math.abs(baseCopy.getExit().getPosition().getY()
        - sourceSegment.getExit().getPosition().getY()) < DOUBLE_COMPARE_THRESHOLD);
  }
}
