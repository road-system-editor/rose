package edu.kit.rose.model.plausibility.violation;

import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.elements.Segment;
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

  private final MultiValuedMap<PlausibilityCriterion, Violation> criterionViolationMap;

  /**
   * Constructor.
   */
  public ViolationManager() {
    criterionViolationMap = new HashSetValuedHashMap<>();
  }


  /**
   * Adds a given {@link Violation} to the ViolationManager.
   *
   * @param violation The {@link Violation} to add.
   */
  public void addViolation(Violation violation) {
    criterionViolationMap.put(violation.violatedCriterion(), violation);
    getSubscriberIterator().forEachRemaining(sub -> sub.notifyAddition(violation));
  }

  /**
   * Removes a given {@link Violation} from the ones held by the violationManager.
   *
   * @param violation The {@link Violation} to remove.
   */
  public void removeViolation(Violation violation) {
    criterionViolationMap.removeMapping(violation.violatedCriterion(), violation);
    getSubscriberIterator().forEachRemaining(sub -> sub.notifyRemoval(violation));
  }

  /**
   * Returns the {@link Violation} of a given {@link PlausibilityCriterion} that
   * is caused by the given {@link Segment}s.
   *
   * @param criterion The {@link PlausibilityCriterion} that the searched
   *        {@link Violation} offends against.
   * @param offendingSegments The {@link Segment}s that cause the {@link Violation}
   * @return the violation against the given Criterion by the given Segments. Or null if the
   *        Violation is not in the ViolationManager.
   */
  Violation getViolation(PlausibilityCriterion criterion, Collection<Segment> offendingSegments) {
    Collection<Violation> violationsAgainstCriterion = criterionViolationMap.get(criterion);
    List<Violation> matches =
        violationsAgainstCriterion.stream().filter((violation ->
            violation.offendingSegments().containsAll(offendingSegments)
        && offendingSegments.containsAll(violation.offendingSegments()))).toList();

    if (matches.size() > 1) {
      throw new IllegalStateException("Multiple entries for the same criterion and segments");
    } else if (matches.size() == 1) {
      return matches.get(0);
    } else {
      return null;
    }
  }

  /**
   * Returns all {@link Violation}s currently held by the ViolationManager.
   *
   * @return A {@link SortedBox} containing all {@link Violation}s.
   */
  public SortedBox<Violation> getViolations() {
    return new RoseSortedBox<>(criterionViolationMap.values().toArray(new Violation[0]));
  }

  @Override
  public Iterator<Violation> iterator() {
    return Collections.unmodifiableCollection(criterionViolationMap.values()).iterator();
  }

  @Override
  public ViolationManager getThis() {
    return this;
  }
}
