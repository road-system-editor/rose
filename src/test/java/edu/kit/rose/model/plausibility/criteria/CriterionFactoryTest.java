package edu.kit.rose.model.plausibility.criteria;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Element;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link CriterionFactory}.
 */
class CriterionFactoryTest {
  private CriterionFactory factory;

  @BeforeEach
  public void setUp() {
    RoadSystem roadSystem = mock(RoadSystem.class);
    Element element = mock(Element.class);
    when(element.isContainer()).thenReturn(false);
    when(roadSystem.getElements()).thenReturn(new RoseSortedBox<>(List.of(element)));
    this.factory = new CriterionFactory();
    this.factory.setRoadSystem(roadSystem);
    this.factory.setViolationManager(mock(ViolationManager.class));
  }

  @Test
  void createCompletenessCriterionTest() {
    PlausibilityCriterion criteria = this.factory.createCompletenessCriterion();

    Assertions.assertNotNull(criteria);
    Assertions.assertEquals(criteria.getType(), (PlausibilityCriterionType.COMPLETENESS));
  }

  @Test
  void createValueCriterionTest() {
    List<ValueCriterion> criterion = this.factory.createValueCriteria();

    for (ValueCriterion criteria : criterion) {
      Assertions.assertNotNull(criteria);
      Assertions.assertEquals(criteria.getType(), (PlausibilityCriterionType.VALUE));
    }
  }

  @Test
  void createCompatibilityCriterionTest() {
    PlausibilityCriterion criteria = this.factory.createCompatibilityCriterion();

    Assertions.assertNotNull(criteria);
    Assertions.assertEquals(criteria.getType(), (PlausibilityCriterionType.COMPATIBILITY));
  }
}