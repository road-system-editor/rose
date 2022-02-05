package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompletenessCriterionTest {
  private CompletenessCriterion criterion;
  private RoadSystem roadSystem;
  private ViolationManager violationManager;

  @BeforeEach
  public void setUp() {
    this.violationManager = new ViolationManager();
    this.criterion = new CompletenessCriterion(this.violationManager);
  }

  @Test
  void TestGetAndSetName() {
    criterion.setName("name");

    Assertions.assertEquals("name", criterion.getName());
  }


  @Test
  void TestGetAndAddSegmentTypes() {
    criterion.addSegmentType(SegmentType.EXIT);

    Assertions.assertTrue(criterion.getSegmentTypes().contains(SegmentType.EXIT));
  }

  @Test
  void TestGetType() {
    Assertions.assertEquals(PlausibilityCriterionType.COMPLETENESS, criterion.getType());
  }

  @Test
  void TestRemoveSegmentType() {
    criterion.addSegmentType(SegmentType.BASE);
    criterion.addSegmentType(SegmentType.EXIT);
    criterion.removeSegmentType(SegmentType.BASE);

    Assertions.assertTrue(criterion.getSegmentTypes().contains(SegmentType.EXIT));
    Assertions.assertFalse(criterion.getSegmentTypes().contains(SegmentType.BASE));
  }

  @Test
  void notifyChange() {
    Segment segment = new Base();

    criterion.addSegmentType(SegmentType.BASE);
    criterion.notifyChange(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    for (AttributeAccessor<?> accessor : segment.getAttributeAccessors()) {
      AttributeAccessor<Object> auxAccessor = (AttributeAccessor<Object>) accessor;
      auxAccessor.setValue(new Object());
    }
    criterion.notifyChange(segment);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void getThis() {
  }

  @Test
  void notifyAddition() {
    Segment segment = new Base();

    criterion.addSegmentType(SegmentType.BASE);
    criterion.notifyAddition(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());
  }

  @Test
  void notifyRemoval() {
    Segment segment = new Base();

    criterion.addSegmentType(SegmentType.BASE);
    criterion.notifyChange(segment);

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    criterion.notifyRemoval(segment);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }
}