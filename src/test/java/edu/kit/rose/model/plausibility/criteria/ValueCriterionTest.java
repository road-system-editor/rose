package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ValueCriterionTest {
  private ValueCriterion criterion1;
  private ValueCriterion criterion2;
  private ViolationManager violationManager;

  @BeforeEach
  public void setUp() {
    this.violationManager = new ViolationManager();
    this.criterion1 = new ValueCriterion(violationManager, AttributeType.LENGTH,
            ValueCriterion.LENGTH_RANGE);
    this.criterion2 = new ValueCriterion(violationManager, AttributeType.SLOPE,
            ValueCriterion.SLOPE_RANGE);
  }

  @Test
  void testGetAttributeType() {
    Assertions.assertEquals(AttributeType.LENGTH, criterion1.getAttributeType());
  }

  @Test
  void testGetAndSetName() {
    criterion1.setName("test");

    Assertions.assertEquals("test", criterion1.getName());
  }

  @Test
  void testGetAndAddSegmentTypes() {
    SetObserver<SegmentType, PlausibilityCriterion> observer = Mockito.mock(SetObserver.class);
    criterion1.addSubscriber(observer);
    criterion1.addSegmentType(SegmentType.BASE);

    Assertions.assertEquals(1, criterion1.getSegmentTypes().getSize());
    Assertions.assertTrue(criterion1.getSegmentTypes().contains(SegmentType.BASE));
    Mockito.verify(observer, Mockito.times(1)).notifyAddition(SegmentType.BASE);
  }

  @Test
  void testGetType() {
    Assertions.assertEquals(PlausibilityCriterionType.VALUE, criterion1.getType());
  }

  @Test
  void removeSegmentType() {
    SetObserver<SegmentType, PlausibilityCriterion> observer = Mockito.mock(SetObserver.class);
    criterion1.addSubscriber(observer);
    criterion1.addSegmentType(SegmentType.BASE);
    criterion1.addSegmentType(SegmentType.EXIT);

    Assertions.assertEquals(2, criterion1.getSegmentTypes().getSize());

    criterion1.removeSegmentType(SegmentType.EXIT);

    Assertions.assertEquals(1, criterion1.getSegmentTypes().getSize());
    Assertions.assertTrue(criterion1.getSegmentTypes().contains(SegmentType.BASE));
    Assertions.assertFalse(criterion1.getSegmentTypes().contains(SegmentType.EXIT));
    Mockito.verify(observer, Mockito.times(1)).notifyRemoval(SegmentType.EXIT);
  }

  @Test
  void testNotifyChange() {
    Base segment = new Base();
    criterion1.addSegmentType(SegmentType.BASE);
    segment.setLength(5001);
    criterion1.notifyChange(segment);
    Assertions.assertEquals(1, violationManager.getViolations().getSize());
    segment.setLength(4999);
    criterion1.notifyChange(segment);
    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testNotifyAddition() {
    Base segment = new Base();
    criterion2.addSegmentType(SegmentType.BASE);

    segment.setSlope(11.0);

    criterion2.notifyAddition(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }

  @Test
  void testNotifyRemoval() {
    Base segment = new Base();
    criterion1.addSegmentType(SegmentType.BASE);

    segment.setLength(5001);

    criterion1.notifyChange(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    criterion1.notifyRemoval(segment);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testGetThis() {
    Assertions.assertSame(criterion1, criterion1.getThis());
  }

  @Test
  void testThrowsException() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> new ValueCriterion(violationManager, AttributeType.NAME,
                    ValueCriterion.SLOPE_RANGE));
  }
}