package edu.kit.rose.model.plausibility.violation;

import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * A Violation is an offense against a {@link PlausibilityCriterion} caused by one {@link Segment}
 * or a {@link Connection} of multiple {@link Segment}s.
 *
 * @param violatedCriterion the criterion that this violation offends, may not be null.
 * @param offendingSegments the {@link Segment}s that cause this violation, may not be null.
 */
public record Violation(
    PlausibilityCriterion violatedCriterion,
    Collection<Segment> offendingSegments) {

  /**
   * Creates a new violation of the given criterion through the given segments.
   *
   * @param violatedCriterion the criterion that this violation offends.
   * @param offendingSegments the {@link Segment}s that cause this violation.
   */
  public Violation(PlausibilityCriterion violatedCriterion, Collection<Segment> offendingSegments) {
    this.violatedCriterion =
        Objects.requireNonNull(violatedCriterion, "violated criterion may not be null");
    this.offendingSegments = Collections.unmodifiableCollection(
        Objects.requireNonNull(offendingSegments, "offending segment may not be null"));
  }
}
