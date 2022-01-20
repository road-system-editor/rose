package edu.kit.rose.model.plausibility.violation;

import edu.kit.rose.infrastructure.SetObservable;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Collection;
import java.util.Iterator;

/**
 * A ViolationManager provides currently active Violations, violations can be added and removed
 * (from inside this package). Violations held by this ViolationManager are mutually distinct.
 */
public class ViolationManager
    implements SetObservable<Violation, ViolationManager>, Iterable<Violation> {

  /**
   * Adds a given {@link Violation} to the ViolationManager.
   *
   * @param violation The {@link Violation} to add.
   */
  void addViolation(Violation violation) {

  }

  /**
   * Removes a given {@link Violation} from the ones held by the violationManager.
   *
   * @param violation The {@link Violation} to remove.
   */
  void removeViolation(Violation violation) {

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
    return null;
  }

  @Override
  public void addSubscriber(SetObserver<Violation, ViolationManager> observer) {

  }

  @Override
  public void removeSubscriber(SetObserver<Violation, ViolationManager> observer) {

  }

  /**
   * Returns all {@link Violation}s currentl held by the ViolationManager.
   *
   * @return A {@link SortedBox} containing all {@link Violation}s.
   */
  public SortedBox<Violation> getViolations() {
    return null;
  }

  @Override
  public Iterator<Violation> iterator() {
    return null;
  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public ViolationManager getThis() {
    return this;
  }
}
