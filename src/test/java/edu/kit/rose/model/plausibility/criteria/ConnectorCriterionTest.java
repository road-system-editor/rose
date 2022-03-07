package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit tests for the {@link ConnectorCriterion} Class.
 */
class ConnectorCriterionTest {
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
    var segment1 = (Entrance) roadSystem.createSegment(SegmentType.ENTRANCE);
    var segment2 = (Entrance) roadSystem.createSegment(SegmentType.ENTRANCE);

    Connector connector1 = segment1.getExit();
    Connector connector2 = segment2.getExit();

    roadSystem.connectConnectors(connector1, connector2);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }

  @Test
  void baseViolationTest() {
    var segment1 = (Base) roadSystem.createSegment(SegmentType.BASE);
    var segment2 = (Base) roadSystem.createSegment(SegmentType.BASE);

    Connector connector1 = segment1.getEntry();
    Connector connector2 = segment2.getEntry();

    roadSystem.connectConnectors(connector1, connector2);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }

  @Test
  void baseRampViolationTest() {
    var segment1 = (Base) roadSystem.createSegment(SegmentType.BASE);
    var segment2 = (Entrance) roadSystem.createSegment(SegmentType.ENTRANCE);

    Connector connector1 = segment1.getEntry();
    Connector connector2 = segment2.getRamp();

    roadSystem.connectConnectors(connector1, connector2);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }

  @Test
  void baseRampNoViolationTest() {
    var segment1 = (Base) roadSystem.createSegment(SegmentType.BASE);
    var segment2 = (Entrance) roadSystem.createSegment(SegmentType.ENTRANCE);

    Connector connector1 = segment1.getExit();
    Connector connector2 = segment2.getRamp();

    roadSystem.connectConnectors(connector1, connector2);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }
}
