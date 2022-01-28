package edu.kit.rose.model.model.plausibility.violation;

import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit tests for the {@link edu.kit.rose.model.plausibility.violation.Violation} record.
 */
public class ViolationTest {
  private PlausibilityCriterion violatedCriterion;
  private Collection<Segment> offendingSegments;

  @BeforeEach
  public void beforeEach() {
    this.violatedCriterion = Mockito.mock(PlausibilityCriterion.class);
    this.offendingSegments = new ArrayList<>();
  }

  @Test
  public void testConstructorNull() {
    Assertions.assertThrows(NullPointerException.class,
        () -> new Violation(null, null));
    Assertions.assertThrows(NullPointerException.class,
        () -> new Violation(this.violatedCriterion, null));
    Assertions.assertThrows(NullPointerException.class,
        () -> new Violation(null, this.offendingSegments));
  }

  @Test
  public void testOffendingSegmentsImmutable() {
    var violation = new Violation(this.violatedCriterion, this.offendingSegments);
    var segment = Mockito.mock(Segment.class);

    Assertions.assertThrows(UnsupportedOperationException.class,
        () -> violation.offendingSegments().add(segment));
  }
}
