package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.HighwaySegment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.util.MockingUtility;
import edu.kit.rose.util.RoadSystemUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit Tests for {@link CompatibilityCriterion}.
 */
class CompatibilityCriterionTest {
  private CompatibilityCriterion criterion;
  private RoadSystem roadSystem;
  private ViolationManager violationManager;

  @BeforeEach
  void setUp() {
    CriteriaManager criteriaManager = MockingUtility.mockCriteriaManager();
    this.roadSystem = new GraphRoadSystem(criteriaManager,
            Mockito.mock(TimeSliceSetting.class));
    this.violationManager = new ViolationManager();
    this.criterion = new CompatibilityCriterion(null, this.violationManager);
    this.criterion.setRoadSystem(this.roadSystem);
  }

  @Test
  void testGetAndSetAttributeType() {
    criterion.setAttributeType(AttributeType.NAME);

    Assertions.assertEquals(AttributeType.NAME, this.criterion.getAttributeType());
  }


  @Test
  void testGetAndSetValidationType() {
    criterion.setValidationType(ValidationType.EQUALS);

    Assertions.assertEquals(ValidationType.EQUALS, this.criterion.getValidationType());
  }

  @Test
  void testGetCompatibleValidationTypes() {
    criterion.setAttributeType(AttributeType.NAME);
    SortedBox<ValidationType> box = criterion.getCompatibleValidationTypes();

    Assertions.assertTrue(box.contains(ValidationType.EQUALS));
    Assertions.assertTrue(box.contains(ValidationType.NOT_EQUALS));
    Assertions.assertFalse(box.contains(ValidationType.OR));
    Assertions.assertFalse(box.contains(ValidationType.NOR));
    Assertions.assertFalse(box.contains(ValidationType.LESS_THAN));
  }

  @Test
  void testGetAndSetLegalDiscrepancy() {
    criterion.setLegalDiscrepancy(0);

    Assertions.assertEquals(0, criterion.getLegalDiscrepancy());
  }

  @Test
  void testGetAndSetName() {
    criterion.setName("name");

    Assertions.assertEquals("name", criterion.getName());
  }

  @Test
  void testGetSegmentTypes() {
    criterion.addSegmentType(SegmentType.BASE);

    Assertions.assertEquals(1, criterion.getSegmentTypes().getSize());
    Assertions.assertTrue(criterion.getSegmentTypes().contains(SegmentType.BASE));
  }

  @Test
  void testGetType() {
    Assertions.assertEquals(PlausibilityCriterionType.COMPATIBILITY, criterion.getType());
  }

  @Test
  void testRemoveSegmentType() {
    criterion.addSegmentType(SegmentType.BASE);
    criterion.addSegmentType(SegmentType.EXIT);
    criterion.removeSegmentType(SegmentType.EXIT);

    Assertions.assertEquals(1, criterion.getSegmentTypes().getSize());
    Assertions.assertTrue(criterion.getSegmentTypes().contains(SegmentType.BASE));
    Assertions.assertFalse(criterion.getSegmentTypes().contains(SegmentType.EXIT));
  }

  @Test
  void testNotifyChange() {
    HighwaySegment segment1 = RoadSystemUtility.createDefaultBase(this.roadSystem);
    HighwaySegment segment2 = RoadSystemUtility.createDefaultBase(this.roadSystem);

    roadSystem.connectConnectors(segment1.getEntry(),
            segment2.getExit());

    segment1.setName("str");
    segment2.setName("str");

    this.criterion.setValidationType(ValidationType.EQUALS);
    this.criterion.setAttributeType(AttributeType.NAME);
    this.criterion.addSegmentType(SegmentType.BASE);

    Assertions.assertEquals(0, this.violationManager.getViolations().getSize());

    segment1.setLength(3);
    segment2.setLength(1);
    this.criterion.setLegalDiscrepancy(1);
    this.criterion.setAttributeType(AttributeType.LENGTH);
    this.criterion.setValidationType(ValidationType.LESS_THAN);


    Assertions.assertEquals(1, this.violationManager.getViolations().getSize());

    segment1.setLength(1);
    this.criterion.notifyChange(segment1);

    // the violation is now resolved
    Assertions.assertEquals(0, this.violationManager.getViolations().getSize());
  }

  @Test
  void testNotifyRemoval() {
    HighwaySegment segment1 = RoadSystemUtility.createDefaultBase(this.roadSystem);
    HighwaySegment segment2 = RoadSystemUtility.createDefaultBase(this.roadSystem);

    roadSystem.connectConnectors(segment1.getEntry(),
            segment2.getExit());

    segment1.setSlope(3.0);
    segment2.setSlope(3.0);
    this.criterion.setAttributeType(AttributeType.SLOPE);
    this.criterion.setValidationType(ValidationType.NOT_EQUALS);
    this.criterion.addSegmentType(SegmentType.BASE);

    this.roadSystem.removeElement(segment1);
    this.criterion.notifyChange(segment1);

    // all violations removed after the segment is removed
    Assertions.assertEquals(0, this.violationManager.getViolations().getSize());
  }

  @Test
  void testNotifyAddition() {
    this.criterion.setValidationType(ValidationType.NOR);
    this.criterion.setAttributeType(AttributeType.CONURBATION);
    this.criterion.addSegmentType(SegmentType.BASE);
    HighwaySegment segment1 = RoadSystemUtility.createDefaultBase(this.roadSystem);
    HighwaySegment segment2 = RoadSystemUtility.createDefaultBase(this.roadSystem);
    roadSystem.connectConnectors(segment1.getExit(),
            segment2.getEntry());
    segment1.setConurbation(true);
    segment2.setConurbation(true);
    //this.criterion.notifyAddition(segment1);
    this.criterion.notifyChange(segment1);

    Assertions.assertEquals(1, this.violationManager.getViolations().getSize());
  }

  @Test
  void testSetViolationManager() {
    ViolationManager violationManager2 = new ViolationManager();
    HighwaySegment segment1 = (HighwaySegment) roadSystem.createSegment(SegmentType.BASE);
    HighwaySegment segment2 = (HighwaySegment) roadSystem.createSegment(SegmentType.BASE);

    roadSystem.connectConnectors(segment1.getConnectors().iterator().next(),
            segment2.getConnectors().iterator().next());

    this.criterion.setViolationManager(violationManager2);

    segment1.setConurbation(false);
    segment2.setConurbation(false);

    this.criterion.setAttributeType(AttributeType.CONURBATION);
    this.criterion.addSegmentType(SegmentType.BASE);
    this.criterion.setValidationType(ValidationType.OR);
    Assertions.assertEquals(1, violationManager2.getViolations().getSize());
  }

  @Test
  void testThrowsException() {
    this.criterion = new CompatibilityCriterion(null, this.violationManager);

    Base someBase = new Base();
    Assertions.assertThrows(IllegalStateException.class,
            () -> this.criterion.notifyChange(someBase));
  }

  @Test
  void testIgnoresAttributesWithoutValues() {
    HighwaySegment segment1 = (HighwaySegment) this.roadSystem.createSegment(SegmentType.BASE);
    HighwaySegment segment2 = (HighwaySegment) this.roadSystem.createSegment(SegmentType.BASE);

    roadSystem.connectConnectors(segment1.getEntry(),
        segment2.getExit());

    segment1.setName(null);
    segment2.setName("str");

    this.criterion.setValidationType(ValidationType.EQUALS);
    this.criterion.setAttributeType(AttributeType.NAME);
    this.criterion.addSegmentType(SegmentType.BASE);

    // there should be no violation since the name of segment 1 is unconfigured
    Assertions.assertEquals(0, this.violationManager.getViolations().getSize());
  }
}