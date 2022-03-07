package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.ConnectorType;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for the ConnectorCriterion Class.
 */
public class ConnectorCriterionTest {
  private RoadSystem roadSystem;
  private ViolationManager violationManager;

  /**
   * setup.
   */
  @BeforeEach
  public void setUp() {
    CriteriaManager criteriaManager = new CriteriaManager();
    this.roadSystem = new GraphRoadSystem(criteriaManager,
        Mockito.mock(TimeSliceSetting.class));
    this.violationManager = new ViolationManager();
    criteriaManager.setRoadSystem(roadSystem);
    criteriaManager.setViolationManager(violationManager);
  }

  @Test
  void entryViolationTest() {
    Segment segment1 = roadSystem.createSegment(SegmentType.ENTRANCE);
    Segment segment2 = roadSystem.createSegment(SegmentType.ENTRANCE);

    Connector exitConnector =
        segment1.getConnectors().stream()
            .filter(connector -> connector.getType() == ConnectorType.EXIT)
            .findAny().orElseThrow();
    Connector exitConnector2 =
        segment2.getConnectors().stream()
            .filter(connector -> connector.getType() == ConnectorType.EXIT)
            .findAny().orElseThrow();

    roadSystem.connectConnectors(exitConnector, exitConnector2);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }

  @Test
  void baseViolationTest() {
    Segment segment1 = roadSystem.createSegment(SegmentType.BASE);
    Segment segment2 = roadSystem.createSegment(SegmentType.BASE);

    Connector exitConnector =
        segment1.getConnectors().stream()
            .filter(connector -> connector.getType() == ConnectorType.ENTRY)
            .findAny().orElseThrow();
    Connector exitConnector2 =
        segment2.getConnectors().stream()
            .filter(connector -> connector.getType() == ConnectorType.ENTRY)
            .findAny().orElseThrow();

    roadSystem.connectConnectors(exitConnector, exitConnector2);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }

  @Test
  void baseRampViolationTest() {
    Segment segment1 = roadSystem.createSegment(SegmentType.BASE);
    Segment segment2 = roadSystem.createSegment(SegmentType.ENTRANCE);

    Connector exitConnector =
        segment1.getConnectors().stream()
            .filter(connector -> connector.getType() == ConnectorType.ENTRY)
            .findAny().orElseThrow();
    Connector exitConnector2 =
        segment2.getConnectors().stream()
            .filter(connector -> connector.getType() == ConnectorType.RAMP_ENTRY)
            .findAny().orElseThrow();

    roadSystem.connectConnectors(exitConnector, exitConnector2);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }

  @Test
  void baseRampNoViolationTest() {
    Segment segment1 = roadSystem.createSegment(SegmentType.BASE);
    Segment segment2 = roadSystem.createSegment(SegmentType.ENTRANCE);

    Connector exitConnector =
        segment1.getConnectors().stream()
            .filter(connector -> connector.getType() == ConnectorType.EXIT)
            .findAny().orElseThrow();
    Connector exitConnector2 =
        segment2.getConnectors().stream()
            .filter(connector -> connector.getType() == ConnectorType.RAMP_ENTRY)
            .findAny().orElseThrow();

    roadSystem.connectConnectors(exitConnector, exitConnector2);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }
}
