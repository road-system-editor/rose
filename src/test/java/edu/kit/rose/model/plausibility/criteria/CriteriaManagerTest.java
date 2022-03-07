package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.HighwaySegment;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CriteriaManagerTest {
  private CriteriaManager criteriaManager;

  @BeforeEach
  public void setUp() {
    this.criteriaManager = new CriteriaManager();
    this.criteriaManager.setViolationManager(new ViolationManager());
    RoadSystem roadSystem = new GraphRoadSystem(criteriaManager,
        Mockito.mock(TimeSliceSetting.class));
    this.criteriaManager.setRoadSystem(roadSystem);
  }


  @Test
  void testGetCriteria() {
    Assertions.assertTrue(criteriaManager.getCriteria().getSize() > 0);
  }

  @Test
  void testGetCriteriaOfType() {
    for (PlausibilityCriterion criterion : criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.VALUE)) {
      Assertions.assertEquals(PlausibilityCriterionType.VALUE, criterion.getType());
    }
  }

  @Test
  void testCreateCompatibilityCriterion() {
    int found = 0;
    criteriaManager.createCompatibilityCriterion();

    for (PlausibilityCriterion criterion : criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.COMPATIBILITY)) {
      if (criterion.getType().equals(PlausibilityCriterionType.COMPATIBILITY)) {
        found++;
      }
    }

    Assertions.assertEquals(1, found);
  }

  @Test
  void testRemoveCriterion() {
    int found = 0;

    criteriaManager.createCompatibilityCriterion();
    PlausibilityCriterion auxCriterion = criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.COMPATIBILITY).get(0);
    criteriaManager.removeCriterion(auxCriterion);

    for (PlausibilityCriterion criterion : criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.VALUE)) {
      if (criterion.getType().equals(PlausibilityCriterionType.COMPATIBILITY)) {
        found++;
      }
    }

    Assertions.assertEquals(0, found);
  }

  @Test
  void testRemoveAllCriteria() {
    criteriaManager.removeAllCriteria();
    Assertions.assertEquals(0, criteriaManager.getCriteria().getSize());
  }

  @Test
  void testRemoveAllCriteriaOfType() {
    criteriaManager.removeAllCriteriaOfType(PlausibilityCriterionType.VALUE);
    Assertions.assertEquals(0, criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.VALUE).getSize());
  }


  @Test
  void testNotifyChange() {
    SetObserver<PlausibilityCriterion, CriteriaManager> observer = Mockito
            .mock(SetObserver.class);
    criteriaManager.addSubscriber(observer);
    criteriaManager.notifyChange(null);
    Mockito.verify(observer, Mockito.times(1)).notifyChange(Mockito.any());
  }

  /**
   * Tests if the set method subscribes the criteria to segments.
   * If that's the case than a violation should be created.
   */
  @Test
  void testSetRoadSystem() {
    CriteriaManager criteriaManager1 = new CriteriaManager();
    GraphRoadSystem roadSystem =
            new GraphRoadSystem(criteriaManager1, Mockito.mock(TimeSliceSetting.class));
    criteriaManager1.setRoadSystem(roadSystem);
    HighwaySegment segment1 = (HighwaySegment) roadSystem.createSegment(SegmentType.BASE);
    HighwaySegment segment2 = (HighwaySegment) roadSystem.createSegment(SegmentType.BASE);
    roadSystem.connectConnectors(segment1.getConnectors().iterator().next(),
            segment2.getConnectors().iterator().next());
    segment1.setLength(3);
    segment2.setLength(1);
    ViolationManager violationManager = new ViolationManager();
    criteriaManager1.setViolationManager(violationManager);
    CompatibilityCriterion criterion = criteriaManager1.createCompatibilityCriterion();
    criterion.setLegalDiscrepancy(1);
    criterion.setAttributeType(AttributeType.LENGTH);
    criterion.setOperatorType(ValidationType.LESS_THAN);
    criterion.addSegmentType(SegmentType.BASE);
    criteriaManager1.setRoadSystem(roadSystem);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }
}