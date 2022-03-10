package edu.kit.rose.model.plausibility;

import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.util.MockingUtility;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link RosePlausibilitySystem}.
 */
class RosePlausibilitySystemTest {
  private CriteriaManager criteriaManager;
  private RoadSystem roadSystem;

  @BeforeEach
  void beforeEach() {
    this.criteriaManager = MockingUtility.mockCriteriaManager();
    this.roadSystem = Mockito.mock(RoadSystem.class);
  }

  /**
   * Checks whether the constructor throws {@link NullPointerException}s if a given argument is
   * {@code null}.
   */
  @Test
  void testConstructor() {
    Assertions.assertThrows(NullPointerException.class,
        () -> new RosePlausibilitySystem(null, null));
    Assertions.assertThrows(NullPointerException.class,
        () -> new RosePlausibilitySystem(this.criteriaManager, null));
    Assertions.assertThrows(NullPointerException.class,
        () -> new RosePlausibilitySystem(null, this.roadSystem));
    Assertions.assertDoesNotThrow(
        () -> new RosePlausibilitySystem(this.criteriaManager, this.roadSystem));
  }

  /**
   * Tests whether {@link RosePlausibilitySystem#getCriteriaManager()} returns the
   * {@link CriteriaManager} given in the constructor.
   */
  @Test
  void testGetCriteriaManager() {
    var plausibilitySystem = new RosePlausibilitySystem(this.criteriaManager, this.roadSystem);

    Assertions.assertSame(this.criteriaManager, plausibilitySystem.getCriteriaManager());
  }

  /**
   * Tests whether {@link RosePlausibilitySystem#getViolationManager()} returns an instance of
   * {@link edu.kit.rose.model.plausibility.violation.ViolationManager}.
   */
  @Test
  void testGetViolationManager() {
    var plausibilitySystem = new RosePlausibilitySystem(this.criteriaManager, this.roadSystem);

    Assertions.assertNotNull(plausibilitySystem.getViolationManager());
  }

  /**
   * Tests whether {@link RosePlausibilitySystem#checkAll()} checks each
   * {@link PlausibilityCriterion} of the {@link CriteriaManager} with each
   * {@link edu.kit.rose.model.roadsystem.elements.Element} from the {@link RoadSystem}.
   */
  @Test
  void testCheckAll() {
    var plausibilitySystem = new RosePlausibilitySystem(this.criteriaManager, this.roadSystem);

    var criteria = new RoseSortedBox<>(List.of(
        Mockito.mock(PlausibilityCriterion.class),
        Mockito.mock(PlausibilityCriterion.class)
    ));
    Mockito.when(this.criteriaManager.getCriteria()).thenReturn(criteria);

    var elements = new RoseBox<>(List.of(
        Mockito.mock(Segment.class),
        Mockito.mock(Group.class),
        Mockito.mock(Segment.class)
    ));
    Mockito.when(this.roadSystem.getElements()).thenReturn(elements);

    // actual test execution
    plausibilitySystem.checkAll();

    // plausibility criteria should each have been called with all elements
    for (var criterion : criteria) {
      Mockito.verify(criterion, Mockito.times(elements.getSize())).notifyChange(Mockito.any());
    }
  }
}
