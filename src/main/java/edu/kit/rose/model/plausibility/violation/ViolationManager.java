package edu.kit.rose.model.plausibility.violation;

import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;


/**
 * A ViolationManager provides currently active Violations, violations can be added and removed
 * (from inside this package). Violations held by this ViolationManager are mutually distinct.
 */
public class ViolationManager extends RoseSetObservable<Violation, ViolationManager>
    implements Iterable<Violation> {

  private final List<Violation> violations;
  private final MultiValuedMap<PlausibilityCriterion, Violation> criterionViolationMap;

  /**
   * Constructor.
   */
  public ViolationManager() {
    violations = new ArrayList<>();
    criterionViolationMap = new HashSetValuedHashMap<>();
  }


  /**
   * Adds a given {@link Violation} to the ViolationManager.
   *
   * @param violation The {@link Violation} to add.
   */
  void addViolation(Violation violation) {
    violations.add(violation);
    criterionViolationMap.put(violation.violatedCriterion(), violation);
    notifySubscribers();
  }

  /**
   * Removes a given {@link Violation} from the ones held by the violationManager.
   *
   * @param violation The {@link Violation} to remove.
   */
  void removeViolation(Violation violation) {
    violations.remove(violation);
    criterionViolationMap.removeMapping(violation.violatedCriterion(), violation);
    notifySubscribers();
  }

  /**
   * Returns the {@link Violation} of a given {@link PlausibilityCriterion} that
   * is caused by the given {@link Segment}s.
   *
   * @param criterion The {@link PlausibilityCriterion} that the searched
   *        {@link Violation} offends against.
   * @param offendingSegments The {@link Segment}s that cause the {@link Violation}
   * @return the violation agoinst the given Criterion by the given Segments.
   */
  Violation getViolation(PlausibilityCriterion criterion, Collection<Segment> offendingSegments) {
    Collection<Violation> violationsAgainstCriterion = criterionViolationMap.get(criterion);
    List<Violation> matches =
        violationsAgainstCriterion.stream().filter((violation ->
            violation.offendingSegments().equals(offendingSegments))).toList();
    assert (matches.size() <= 1);
    if (matches.size() == 1) {
      return matches.get(0);
    }
    return null;
  }


  /**
   * Returns all {@link Violation}s currently held by the ViolationManager.
   *
   * @return A {@link SortedBox} containing all {@link Violation}s.
   */
  public SortedBox<Violation> getViolations() {
    return new RoseSortedBox<>(violations);
  }

  @Override
  public Iterator<Violation> iterator() {
    return Collections.unmodifiableCollection(violations).iterator();
  }

  @Override
  public ViolationManager getThis() {
    return this;
  }
}
