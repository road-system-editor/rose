package edu.kit.rose.model.roadsystem.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;
import edu.kit.rose.util.AccessorUtility;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HighwaySegment}.
 */
class HighwaySegmentTest {
  private static class HighwaySegmentImplementation extends HighwaySegment {
    HighwaySegmentImplementation(SegmentType segmentType, String name, Connector testConnector) {
      super(segmentType, name);
      this.connectors.add(Objects.requireNonNull(testConnector));
    }

    @Override
    protected void initConnectors(List<AttributeAccessor<?>> entryAttributesList,
                                  List<AttributeAccessor<?>> exitAttributesList) {
    }
  }

  private static final SegmentType TEST_SEGMENT_TYPE = SegmentType.ENTRANCE;
  private static final String TEST_SEGMENT_NAME = "test segment name";

  private Connector testConnector;
  private HighwaySegment testSegment;

  @BeforeEach
  void beforeEach() {
    this.testConnector = new Connector(ConnectorType.EXIT, new Position(1, 0), List.of());
    this.testSegment = new HighwaySegmentImplementation(
        TEST_SEGMENT_TYPE, TEST_SEGMENT_NAME, this.testConnector);
  }

  @Test
  void testConstructor() {
    var segment1 = new HighwaySegmentImplementation(
        SegmentType.BASE, "some base segment", testConnector);
    assertSame(SegmentType.BASE, segment1.getSegmentType());
    assertEquals("some base segment", segment1.getName());

    var segment2 = new HighwaySegmentImplementation(
        SegmentType.EXIT, "an exit segment", testConnector);
    assertSame(SegmentType.EXIT, segment2.getSegmentType());
    assertEquals("an exit segment", segment2.getName());
  }

  @Test
  void testGetAbsoluteConnectorPositionWithInvalidConnector() {

    assertThrows(IllegalArgumentException.class,
        () -> testSegment.getAbsoluteConnectorPosition(null));

    var invalidConnector = mock(Connector.class);
    assertThrows(IllegalArgumentException.class,
        () -> testSegment.getAbsoluteConnectorPosition(invalidConnector));
  }

  @Test
  void testGetMeasurementsReturnsBox() {
    assertNotNull(testSegment.getMeasurements());

  }

  @Disabled("measurements will not be implemented as part of this PSE")
  @Test
  void testGetMeasurementsContainsExpectedMeasurements() {
    Box<Measurement<?>> measurements = testSegment.getMeasurements();
    assertNotNull(findMeasurementOfType(measurements, MeasurementType.HEAVY_TRAFFIC_PROPORTION));
    assertNotNull(findMeasurementOfType(measurements, MeasurementType.DEMAND));
    assertNotNull(findMeasurementOfType(measurements, MeasurementType.CAPACITY_FACTOR));
  }

  @SuppressWarnings("unchecked") // measurements are expected to have the correct java type
  private static <T> Measurement<T> findMeasurementOfType(Box<Measurement<?>> measurements,
                                                          MeasurementType type) {
    return (Measurement<T>) measurements.stream()
        .filter(measurement -> measurement.getMeasurementType() == type)
        .findAny()
        .orElse(null);
  }

  @Test
  void testCompareToHighwaySegment() {
    HighwaySegmentImplementation other = new HighwaySegmentImplementation(
        SegmentType.BASE, "irrelevant name", mock(Connector.class));

    assertTrue(this.testSegment.compareTo(other) < 0);
    assertTrue(other.compareTo(this.testSegment) > 0);
  }

  @Test
  void testCompareToNonHighwaySegment() {
    Segment other = mock(Segment.class);
    // indicate that "other" is newer than "testSegment"
    when(other.compareTo(this.testSegment)).thenReturn(1);

    assumeTrue(other.compareTo(this.testSegment) > 0);
    assertTrue(this.testSegment.compareTo(other) < 0);
  }

  /**
   * Tests whether the attribute accessors have the correct attribute types and are arranged in
   * the correct order.
   */
  @Test
  void testGetAttributeAccessors() {
    SortedBox<AttributeAccessor<?>> attributeAccessors = this.testSegment.getAttributeAccessors();

    assertEquals(7, attributeAccessors.getSize());

    assertSame(AttributeType.NAME, attributeAccessors.get(0).getAttributeType());
    assertSame(AttributeType.COMMENT, attributeAccessors.get(1).getAttributeType());
    assertSame(AttributeType.LENGTH, attributeAccessors.get(2).getAttributeType());
    assertSame(AttributeType.SLOPE, attributeAccessors.get(3).getAttributeType());
    assertSame(AttributeType.LANE_COUNT, attributeAccessors.get(4).getAttributeType());
    assertSame(AttributeType.CONURBATION, attributeAccessors.get(5).getAttributeType());
    assertSame(AttributeType.MAX_SPEED, attributeAccessors.get(6).getAttributeType()
    );
  }

  @Test
  void testNameAttribute() {
    this.testSegment.setName(null);

    testAccessorCorrectness(
        AttributeType.NAME,
        this.testSegment::getName,
        this.testSegment::setName,
        "highway segment name"
    );
  }

  @Test
  void testCommentAttribute() {
    testAccessorCorrectness(
        AttributeType.COMMENT,
        this.testSegment::getComment,
        this.testSegment::setComment,
        "highly informative and insightful comment"
    );
  }

  @Test
  void testLengthAttribute() {
    testAccessorCorrectness(
        AttributeType.LENGTH,
        this.testSegment::getLength,
        this.testSegment::setLength,
        489
    );
  }

  @Test
  void testSlopeAttribute() {
    testAccessorCorrectness(
        AttributeType.SLOPE,
        this.testSegment::getSlope,
        this.testSegment::setSlope,
        12.34
    );
  }

  @Test
  void testLaneCountAttribute() {
    testAccessorCorrectness(
        AttributeType.LANE_COUNT,
        this.testSegment::getLaneCount,
        this.testSegment::setLaneCount,
        2
    );
  }

  @Test
  void testConurbationAttribute() {
    testAccessorCorrectness(
        AttributeType.CONURBATION,
        this.testSegment::getConurbation,
        this.testSegment::setConurbation,
        false
    );
  }

  @Test
  void testMaxSpeedAttribute() {
    testAccessorCorrectness(
        AttributeType.MAX_SPEED,
        this.testSegment::getMaxSpeed,
        this.testSegment::setMaxSpeed,
        SpeedLimit.TUNNEL
    );
  }

  private <T> void testAccessorCorrectness(AttributeType attribute, Supplier<T> getter,
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
