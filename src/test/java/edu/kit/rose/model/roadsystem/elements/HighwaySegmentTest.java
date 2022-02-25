package edu.kit.rose.model.roadsystem.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HighwaySegment}.
 */
public class HighwaySegmentTest {
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
}
