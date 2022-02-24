package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
  public void setUp() {
    this.roadSystem = new GraphRoadSystem(new CriteriaManager(),
            Mockito.mock(TimeSliceSetting.class));
    this.violationManager = new ViolationManager();
    this.criterion = new CompatibilityCriterion(roadSystem, this.violationManager);
  }

  @Test
  void testGetAndSetAttributeType() {
    criterion.setAttributeType(AttributeType.NAME);

    Assertions.assertEquals(AttributeType.NAME, this.criterion.getAttributeType());
  }


  @Test
  void testGetAndSetOperatorType() {
    criterion.setOperatorType(ValidationType.EQUALS);

    Assertions.assertEquals(ValidationType.EQUALS, this.criterion.getOperatorType());
  }

  @Test
  void testGetCompatibleOperatorTypes() {
    criterion.setAttributeType(AttributeType.NAME);
    SortedBox<ValidationType> box = criterion.getCompatibleOperatorTypes();

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
    Segment segment1 = roadSystem.createSegment(SegmentType.BASE);
    Segment segment2 = roadSystem.createSegment(SegmentType.BASE);

    roadSystem.connectConnectors(segment1.getConnectors().iterator().next(),
            segment2.getConnectors().iterator().next());

    SortedBox<AttributeAccessor<?>> accessors1 = segment1.getAttributeAccessors();
    SortedBox<AttributeAccessor<?>> accessors2 = segment2.getAttributeAccessors();

    this.setValueToAccessor(accessors1, "str", AttributeType.NAME);
    this.setValueToAccessor(accessors2, "str", AttributeType.NAME);
    this.criterion.setOperatorType(ValidationType.EQUALS);
    this.criterion.setAttributeType(AttributeType.NAME);
    this.criterion.addSegmentType(SegmentType.BASE);
    this.criterion.notifyChange(segment1);

    Assertions.assertEquals(0, this.violationManager.getViolations().getSize());

    this.setValueToAccessor(accessors1, 3, AttributeType.LENGTH);
    this.setValueToAccessor(accessors2, 1, AttributeType.LENGTH);
    this.criterion.setLegalDiscrepancy(1);
    this.criterion.setAttributeType(AttributeType.LENGTH);
    this.criterion.setOperatorType(ValidationType.LESS_THAN);
    this.criterion.notifyChange(segment1);


    Assertions.assertEquals(1, this.violationManager.getViolations().getSize());

    this.setValueToAccessor(accessors1, 1, AttributeType.LENGTH);
    this.criterion.notifyChange(segment1);

    Assertions.assertEquals(0, this.violationManager.getViolations().getSize());
  }

  private <T> void setValueToAccessor(SortedBox<AttributeAccessor<?>> attributeAccessor, T value,
                                      AttributeType type) {
    for (AttributeAccessor<?> accessor : attributeAccessor) {
      if (accessor.getAttributeType().equals(type)) {
        AttributeAccessor<T> auxAccessor = (AttributeAccessor<T>) accessor;
        auxAccessor.setValue(value);
      }
    }
  }

  @Test
  void testNotifyRemoval() {
    Segment segment1 = roadSystem.createSegment(SegmentType.BASE);
    Segment segment2 = roadSystem.createSegment(SegmentType.BASE);

    roadSystem.connectConnectors(segment1.getConnectors().iterator().next(),
            segment2.getConnectors().iterator().next());

    SortedBox<AttributeAccessor<?>> accessors1 = segment1.getAttributeAccessors();
    SortedBox<AttributeAccessor<?>> accessors2 = segment2.getAttributeAccessors();


    this.setValueToAccessor(accessors1, 3, AttributeType.LENGTH);
    this.setValueToAccessor(accessors2, 1, AttributeType.LENGTH);
    this.criterion.setLegalDiscrepancy(1);
    this.criterion.setAttributeType(AttributeType.LENGTH);
    this.criterion.setOperatorType(ValidationType.LESS_THAN);
    this.criterion.addSegmentType(SegmentType.BASE);
    this.criterion.notifyChange(segment1);
    this.criterion.notifyRemoval(segment1);

    Assertions.assertEquals(0, this.violationManager.getViolations().getSize());
  }
}