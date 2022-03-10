package edu.kit.rose.model.roadsystem.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.util.AccessorUtility;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link RampSegment}s.
 */
class RampSegmentTest {
  /**
   * Stub implementation of {@link RampSegment}.
   */
  private static class RampSegmentImplementation extends RampSegment {
    RampSegmentImplementation(SegmentType segmentType) {
      super(segmentType);
    }

    @Override
    protected void initRampConnector(List<AttributeAccessor<?>> rampAttributesList) {
    }
  }

  private static final SegmentType TEST_SEGMENT_TYPE = SegmentType.ENTRANCE;

  RampSegment testSegment;

  @BeforeEach
  void beforeEach() {
    this.testSegment = new RampSegmentImplementation(TEST_SEGMENT_TYPE);
  }

  /**
   * Tests whether the attribute accessors have the correct attribute types and are arranged in
   * the correct order.
   */
  @Test
  void testGetAttributeAccessors() {
    SortedBox<AttributeAccessor<?>> attributeAccessors = testSegment.getAttributeAccessors();

    assertEquals(10, attributeAccessors.getSize());

    assertSame(AttributeType.NAME, attributeAccessors.get(0).getAttributeType());
    assertSame(AttributeType.COMMENT, attributeAccessors.get(1).getAttributeType());
    assertSame(AttributeType.LENGTH, attributeAccessors.get(2).getAttributeType());
    assertSame(AttributeType.SLOPE, attributeAccessors.get(3).getAttributeType());
    assertSame(AttributeType.LANE_COUNT, attributeAccessors.get(4).getAttributeType());
    assertSame(AttributeType.CONURBATION, attributeAccessors.get(5).getAttributeType());
    assertSame(AttributeType.MAX_SPEED, attributeAccessors.get(6).getAttributeType());
    assertSame(AttributeType.LANE_COUNT_RAMP, attributeAccessors.get(7).getAttributeType());
    assertSame(AttributeType.JUNCTION, attributeAccessors.get(8).getAttributeType());
    assertSame(AttributeType.MAX_SPEED_RAMP, attributeAccessors.get(9).getAttributeType());
  }

  @Test
  void testLaneCountRampAttribute() {
    assertAccessorCorrectness(
        AttributeType.LANE_COUNT_RAMP,
        this.testSegment::getLaneCountRamp,
        this.testSegment::setLaneCountRamp,
        3
    );
  }

  @Test
  void testMaxSpeedRampAttribute() {
    assertAccessorCorrectness(
        AttributeType.MAX_SPEED_RAMP,
        this.testSegment::getMaxSpeedRamp,
        this.testSegment::setMaxSpeedRamp,
        SpeedLimit.T90
    );
  }

  @Test
  void testJunctionNameAttribute() {
    assertAccessorCorrectness(
        AttributeType.JUNCTION,
        this.testSegment::getJunctionName,
        this.testSegment::setJunctionName,
        "central junction"
    );
  }

  private <T> void assertAccessorCorrectness(AttributeType attribute, Supplier<T> getter,
                                             Consumer<T> setter, T testValue) {
    AccessorUtility.testAccessorCorrectness(
        this.testSegment,
        attribute,
        getter,
        setter,
        testValue
    );
  }

}
