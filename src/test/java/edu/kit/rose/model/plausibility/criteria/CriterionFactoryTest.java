package edu.kit.rose.model.plausibility.criteria;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Element;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CriterionFactory}.
 */
class CriterionFactoryTest {
  private CriterionFactory factory;

  @BeforeEach
  public void setUp() {
    RoadSystem roadSystem = mock(RoadSystem.class);
    Element element = new Base();
    when(roadSystem.getElements()).thenReturn(new RoseSortedBox<>(List.of(element)));
    when(roadSystem.getConnections(any())).thenReturn(new RoseBox<>());
    this.factory = new CriterionFactory();
    this.factory.setRoadSystem(roadSystem);
    this.factory.setViolationManager(mock(ViolationManager.class));
  }

  @Test
  void testCreateCompletenessCriterion() {
    PlausibilityCriterion criteria = this.factory.createCompletenessCriterion();

    assertNotNull(criteria);
    assertSame(PlausibilityCriterionType.COMPLETENESS, criteria.getType());
  }

  @Test
  void testCreateValueCriteria() {
    List<ValueCriterion> criterion = this.factory.createValueCriteria();

    for (ValueCriterion criteria : criterion) {
      assertNotNull(criteria);
      assertSame(PlausibilityCriterionType.VALUE, criteria.getType());
    }
  }

  @Test
  void testCreateCompatibilityCriterion() {
    PlausibilityCriterion criterion = this.factory.createCompatibilityCriterion();

    assertNotNull(criterion);
    assertSame(PlausibilityCriterionType.COMPATIBILITY, criterion.getType());
  }

  @Test
  void testCreateConnectorCriterion() {
    ConnectorCriterion criterion = this.factory.createConnectorCriterion();
    assertNotNull(criterion);
    assertSame(PlausibilityCriterionType.CONNECTOR, criterion.getType());
  }
}