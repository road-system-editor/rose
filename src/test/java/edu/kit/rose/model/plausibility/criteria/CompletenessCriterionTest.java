package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CompletenessCriterionTest {
  private CompletenessCriterion criterion;
  private ViolationManager violationManager;

  @BeforeEach
  public void setUp() {
    this.violationManager = new ViolationManager();
    this.criterion = new CompletenessCriterion(this.violationManager);
  }

  @Test
  void testGetAndSetName() {
    criterion.setName("name");

    Assertions.assertEquals("name", criterion.getName());
  }


  @Test
  void testGetAndAddSegmentTypes() {
    criterion.addSegmentType(SegmentType.EXIT);

    Assertions.assertTrue(criterion.getSegmentTypes().contains(SegmentType.EXIT));
  }

  @Test
  void testGetType() {
    Assertions.assertEquals(PlausibilityCriterionType.COMPLETENESS, criterion.getType());
  }

  @Test
  void testRemoveSegmentType() {
    criterion.addSegmentType(SegmentType.BASE);
    criterion.addSegmentType(SegmentType.EXIT);
    criterion.removeSegmentType(SegmentType.BASE);

    Assertions.assertTrue(criterion.getSegmentTypes().contains(SegmentType.EXIT));
    Assertions.assertFalse(criterion.getSegmentTypes().contains(SegmentType.BASE));
  }

  @Test
  void testNotifyChange() {
    Segment segment = new Base();

    for (AttributeAccessor<?> accessor : segment.getAttributeAccessors()) {
      if (accessor.getAttributeType().equals(AttributeType.NAME)) {
        accessor.setValue(null);
      }
    }

    criterion.addSegmentType(SegmentType.BASE);
    criterion.notifyChange(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    for (AttributeAccessor<?> accessor : segment.getAttributeAccessors()) {
      if (accessor.getAttributeType().equals(AttributeType.NAME)) {
        AttributeAccessor<String> auxAccessor = (AttributeAccessor<String>) accessor;
        auxAccessor.setValue("test");
      }
    }
    criterion.notifyChange(segment);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testNotifyAddition() {
    Segment segment = new Base();

    for (AttributeAccessor<?> accessor : segment.getAttributeAccessors()) {
      if (accessor.getAttributeType().equals(AttributeType.NAME)) {
        accessor.setValue(null);
      }
    }

    criterion.addSegmentType(SegmentType.BASE);
    criterion.notifyAddition(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }

  @Test
  void testNotifyRemoval() {
    Segment segment = new Base();

    for (AttributeAccessor<?> accessor : segment.getAttributeAccessors()) {
      if (accessor.getAttributeType().equals(AttributeType.NAME)) {
        accessor.setValue(null);
      }
    }

    criterion.addSegmentType(SegmentType.BASE);
    criterion.notifyChange(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    criterion.notifyRemoval(segment);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testGetThis() {
    Assertions.assertSame(criterion, criterion.getThis());
  }

  @Test
  void testNotifiesSubscribers() {
    SetObserver<SegmentType, PlausibilityCriterion> observer = Mockito.mock(SetObserver.class);
    criterion.addSubscriber(observer);
    criterion.addSegmentType(SegmentType.BASE);
    Mockito.verify(observer, Mockito.times(1)).notifyAddition(SegmentType.BASE);
    criterion.removeSegmentType(SegmentType.BASE);
    Mockito.verify(observer, Mockito.times(1)).notifyRemoval(SegmentType.BASE);
  }
}