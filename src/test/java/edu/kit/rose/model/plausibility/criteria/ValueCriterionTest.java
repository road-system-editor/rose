package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValueCriterionTest {
  private ValueCriterion criterion;
  private ViolationManager violationManager;

  @BeforeEach
  public void setUp() {
    this.violationManager = new ViolationManager();
    this.criterion = new ValueCriterion(violationManager, AttributeType.LENGTH,
            ValueCriterion.LENGTH_RANGE);
  }

  @Test
  void testGetAttributeType() {
    Assertions.assertEquals(AttributeType.LENGTH, criterion.getAttributeType());
  }

  @Test
  void testGetAndSetName() {
    criterion.setName("test");

    Assertions.assertEquals("test", criterion.getName());
  }

  @Test
  void testGetAndAddSegmentTypes() {
    criterion.addSegmentType(SegmentType.BASE);

    Assertions.assertEquals(1, criterion.getSegmentTypes().getSize());
    Assertions.assertTrue(criterion.getSegmentTypes().contains(SegmentType.BASE));
  }

  @Test
  void testGetType() {
    Assertions.assertEquals(PlausibilityCriterionType.VALUE, criterion.getType());
  }

  @Test
  void removeSegmentType() {
    criterion.addSegmentType(SegmentType.BASE);
    criterion.addSegmentType(SegmentType.EXIT);

    Assertions.assertEquals(2, criterion.getSegmentTypes().getSize());

    criterion.removeSegmentType(SegmentType.EXIT);

    Assertions.assertEquals(1, criterion.getSegmentTypes().getSize());
    Assertions.assertTrue(criterion.getSegmentTypes().contains(SegmentType.BASE));
    Assertions.assertFalse(criterion.getSegmentTypes().contains(SegmentType.EXIT));
  }

  @Test
  void testNotifyChange() {
    Base segment = new Base();
    criterion.addSegmentType(SegmentType.BASE);

    segment.setLength(5001);

    criterion.notifyChange(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    segment.setLength(4999);

    criterion.notifyChange(segment);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testNotifyAddition() {
    Base segment = new Base();
    criterion.addSegmentType(SegmentType.BASE);

    segment.setLength(5001);

    criterion.notifyAddition(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    segment.setLength(4999);

    criterion.notifyAddition(segment);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testNotifyRemoval() {
    Base segment = new Base();
    criterion.addSegmentType(SegmentType.BASE);

    segment.setLength(5001);

    criterion.notifyChange(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    criterion.notifyRemoval(segment);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }
}