package edu.kit.rose.model.plausibility.violation;

import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Collection;

/**
 * A Violation is an offense against a {@link PlausibilityCriterion} caused by one {@link Segment}
 * or a {@link Connection} of multiple {@link Segment}s.
 */
public class Violation {

  /**
   * Constructor.
   *
   * @param violatedCriterion the {@link PlausibilityCriterion} this SimpleViolation offends.
   * @param offendingSegments the {@link Segment}s that cause this SimpleViolation.
   */
  public Violation(PlausibilityCriterion violatedCriterion, Collection<Segment> offendingSegments) {

  }

  /**
   * Provides the violated Criterion.
   *
   * @return The violated {@link PlausibilityCriterion}.
   */
  public PlausibilityCriterion getViolatedCriterion() {
    return null;
  }

  /**
   * Provides the Segments that caused this violation.
   *
   * @return The {@link Segment}s that caused this Violation.
   */
  public Collection<Segment> offendingSegments() {
    return null;
  }
}
