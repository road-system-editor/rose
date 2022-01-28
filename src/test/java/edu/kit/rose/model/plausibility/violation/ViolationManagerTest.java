package edu.kit.rose.model.plausibility.violation;

import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for the ViolationManager.
 */
public class ViolationManagerTest {
  private PlausibilityCriterion compatibilityCriterion;
  private PlausibilityCriterion valueCriterion;
  private Collection<Segment> twoOffendingSegments;
  private Collection<Segment> emptyOffendingSegments;
  private ViolationManager violationManager;
  private Violation compatibilityViolation;
  private Violation valueViolation;

  /**
   * Initialize the Class Attributes.
   */
  @BeforeEach
  public void beforeEach() {
    this.compatibilityCriterion = Mockito.mock(PlausibilityCriterion.class);
    Mockito.when(compatibilityCriterion.getType())
        .thenReturn(PlausibilityCriterionType.COMPATIBILITY);
    this.valueCriterion = Mockito.mock(PlausibilityCriterion.class);
    Mockito.when(valueCriterion.getType()).thenReturn(PlausibilityCriterionType.VALUE);

    this.emptyOffendingSegments = new ArrayList<>();
    this.twoOffendingSegments = List.of(new Entrance(), new Base());

    this.compatibilityViolation = new Violation(compatibilityCriterion, twoOffendingSegments);
    this.valueViolation = new Violation(valueCriterion, twoOffendingSegments);

    this.violationManager = new ViolationManager();
  }

  @Test
  public void testAddAndGetViolation() {
    Assertions.assertEquals(0, violationManager.getViolations().getSize());
    violationManager.addViolation(compatibilityViolation);
    SortedBox<Violation> violations = violationManager.getViolations();
    Assertions.assertEquals(1, violations.getSize());
    Assertions.assertEquals(PlausibilityCriterionType.COMPATIBILITY,
        violations.get(0).violatedCriterion().getType());
    violationManager.addViolation(valueViolation);
    violations = violationManager.getViolations();
    Assertions.assertEquals(2, violations.getSize());
    Assertions.assertTrue(violations.contains(compatibilityViolation));
    Assertions.assertTrue(violations.contains(valueViolation));
  }

  @Test
  public void testRemoveViolation() {
    violationManager.addViolation(compatibilityViolation);
    violationManager.addViolation(valueViolation);
    violationManager.removeViolation(compatibilityViolation);
    SortedBox<Violation> violations = violationManager.getViolations();
    Assertions.assertEquals(1, violations.getSize());
    Assertions.assertFalse(violations.contains(compatibilityViolation));
    Assertions.assertTrue(violations.contains(valueViolation));
  }

  @Test
  public void testGetViolation() {
    Violation violationsNoSegments = new Violation(compatibilityCriterion, emptyOffendingSegments);
    violationManager.addViolation(violationsNoSegments);
    violationManager.addViolation(compatibilityViolation);
    Violation violation = violationManager.getViolation(compatibilityCriterion,
        emptyOffendingSegments);
    Assertions.assertEquals(violationsNoSegments, violation);
    Assertions.assertNotEquals(compatibilityViolation, violation);
    violation = violationManager.getViolation(compatibilityCriterion,
        twoOffendingSegments);
    Assertions.assertEquals(compatibilityViolation, violation);
    Assertions.assertNotEquals(violationsNoSegments, violation);
  }

  @Test
  public void testGetViolationNoResult() {
    Violation violationsNoSegments = new Violation(compatibilityCriterion, emptyOffendingSegments);
    violationManager.addViolation(violationsNoSegments);
    violationManager.addViolation(valueViolation);

    Assertions.assertNull(violationManager.getViolation(compatibilityCriterion,
        twoOffendingSegments));
    Assertions.assertNull(violationManager.getViolation(valueCriterion,
        emptyOffendingSegments));
  }



  @Test
  public void testIteratorImmutable() {
    violationManager.addViolation(compatibilityViolation);
    violationManager.addViolation(valueViolation);
    Iterator<Violation> iterator = violationManager.iterator();
    Assertions.assertTrue(iterator.hasNext());
    Assertions.assertThrows(UnsupportedOperationException.class, iterator::remove);
  }

  @Test
  public void testGetThis() {
    Assertions.assertEquals(violationManager, violationManager.getThis());
  }


}
