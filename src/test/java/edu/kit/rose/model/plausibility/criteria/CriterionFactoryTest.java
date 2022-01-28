package edu.kit.rose.model.plausibility.criteria;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
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
import org.mockito.Mock;

/**
 * Unit test for {@link CriterionFactory}.
 */
class CriterionFactoryTest {
  private static final boolean SUBSCRIBED = true;
  private static final boolean NOT_SUBSCRIBED = false;
  @Mock
  private RoadSystem roadSystem;
  @Mock
  private Element element;
  private CriterionFactory factory;
  private boolean subscribed;

  @BeforeEach
  public void setUp() {
    this.subscribed = NOT_SUBSCRIBED;
    this.roadSystem = mock(RoadSystem.class);
    this.element = mock(Element.class);
    doAnswer(e -> this.subscribed = SUBSCRIBED)
            .when(this.element).addSubscriber(any());
    when(this.element.isContainer()).thenReturn(false);
    when(this.roadSystem.getElements()).thenReturn(new RoseSortedBox<>(List.of(this.element)));
    this.factory = new CriterionFactory(this.roadSystem, mock(ViolationManager.class));
  }

  @Test
  void createCompletenessCriterionTest() {
    PlausibilityCriterion criteria = this.factory.createCompletenessCriterion();

    Assertions.assertNotNull(criteria);
    Assertions.assertEquals(criteria.getType(), (PlausibilityCriterionType.COMPLETENESS));
    Assertions.assertTrue(this.subscribed);
  }

  @Test
  void createValueCriterionTest() {
    PlausibilityCriterion criteria = this.factory.createValueCriterion();

    Assertions.assertNotNull(criteria);
    Assertions.assertEquals(criteria.getType(), (PlausibilityCriterionType.VALUE));
    Assertions.assertTrue(this.subscribed);
  }

  @Test
  void createCompatibilityCriterionTest() {
    PlausibilityCriterion criteria = this.factory.createCompatibilityCriterion();

    Assertions.assertNotNull(criteria);
    Assertions.assertEquals(criteria.getType(), (PlausibilityCriterionType.COMPATIBILITY));
    Assertions.assertTrue(this.subscribed);
  }
}