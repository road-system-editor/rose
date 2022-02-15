package edu.kit.rose.view.panel.problem;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.language.RoseLocalizedTextProvider;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.panel.violation.MessageFactory;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link MessageFactory} class.
 */
public class MessageFactoryTest {
  private Segment segment1;
  private Segment segment2;
  private MessageFactory messageFactory;

  @BeforeEach
  void beforeEach() {
    segment1 = mock(Segment.class);
    when(segment1.getName()).thenReturn("__SEGMENT_1_NAME__");

    segment2 = mock(Segment.class);
    when(segment2.getName()).thenReturn("__SEGMENT_2_NAME__");

    messageFactory = new MessageFactory(new RoseLocalizedTextProvider());
  }

  @Test
  void testValueCriterionViolation() {
    PlausibilityCriterion valueCriterion = mock(PlausibilityCriterion.class);
    when(valueCriterion.getType()).thenReturn(PlausibilityCriterionType.VALUE);
    var violation = new Violation(valueCriterion, List.of(segment1));

    Assertions.assertTrue(messageFactory.generateShortDescription(violation)
        .contains(segment1.getName()));

    Assertions.assertTrue(messageFactory.generateDetailedDescription(violation)
        .contains(segment1.getName()));
  }

  @Test
  void testCompletenessCriterionViolation() {
    PlausibilityCriterion completenessCriterion = mock(PlausibilityCriterion.class);
    when(completenessCriterion.getType()).thenReturn(PlausibilityCriterionType.COMPLETENESS);
    var violation = new Violation(completenessCriterion, List.of(segment1));

    Assertions.assertTrue(messageFactory.generateShortDescription(violation)
        .contains(segment1.getName()));

    Assertions.assertTrue(messageFactory.generateDetailedDescription(violation)
        .contains(segment1.getName()));
  }

  @Test
  void testCompatibilityCriterionViolation() {
    PlausibilityCriterion compatibilityCriterion = mock(PlausibilityCriterion.class);
    when(compatibilityCriterion.getType()).thenReturn(PlausibilityCriterionType.COMPATIBILITY);
    var violation = new Violation(compatibilityCriterion, List.of(segment1, segment2));

    String shortDesc = messageFactory.generateShortDescription(violation);
    Assertions.assertTrue(shortDesc.contains(segment1.getName()));
    Assertions.assertTrue(shortDesc.contains(segment2.getName()));

    String detailedDesc = messageFactory.generateDetailedDescription(violation);
    Assertions.assertTrue(detailedDesc.contains(segment1.getName()));
    Assertions.assertTrue(detailedDesc.contains(segment2.getName()));
  }
}
