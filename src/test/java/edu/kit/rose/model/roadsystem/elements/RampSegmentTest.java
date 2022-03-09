package edu.kit.rose.model.roadsystem.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.util.AccessorUtility;
import java.util.List;
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

  SetObserver<Element, Element> mockObserver;
  RampSegment testSegment;

  @BeforeEach
  void beforeEach() {
    this.testSegment = new RampSegmentImplementation(TEST_SEGMENT_TYPE);

    this.mockObserver = createMockObserver();
    this.testSegment.addSubscriber(mockObserver);
  }

  @SuppressWarnings("unchecked") // this is how mocking generics in
  private SetObserver<Element, Element> createMockObserver() {
    return mock(SetObserver.class);
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
  void testJunctionNameAttribute() {
    AttributeAccessor<String> accessor = AccessorUtility.findAccessorOfType(
        this.testSegment, AttributeType.JUNCTION);
    assertNotNull(accessor);

    // default: junction name not configured
    assertNull(this.testSegment.getJunctionName());
    assertNull(accessor.getValue());
    verifyNoInteractions(mockObserver);

    // test normal setter
    var junctionName = "central junction";
    this.testSegment.setJunctionName(junctionName);
    assertEquals(junctionName, this.testSegment.getJunctionName());
    assertEquals(junctionName, accessor.getValue());
    verify(this.mockObserver, times(1)).notifyChange(this.testSegment);

    // test attribute accessor setter
    junctionName = "actually, it is another junction";
    accessor.setValue(junctionName);
    assertEquals(junctionName, this.testSegment.getJunctionName());
    assertEquals(junctionName, accessor.getValue());
    verify(this.mockObserver, times(2)).notifyChange(this.testSegment);
  }
}
