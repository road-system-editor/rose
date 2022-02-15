package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SetObservable;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A CriteriaManager holds {@link PlausibilityCriterion}s .
 * It provides functions to create and remove Criteria as well as getters for them.
 * Provided with a {@link edu.kit.rose.model.roadsystem.RoadSystem} it will set up created
 * Criteria to observe the appropriate {@link edu.kit.rose.model.roadsystem.elements.Segment}s.
 */
public class CriteriaManager extends RoseSetObservable<PlausibilityCriterion, CriteriaManager>
        implements SetObservable<PlausibilityCriterion, CriteriaManager>,
        UnitObserver<PlausibilityCriterion> {

  private final ArrayList<PlausibilityCriterion> criteria;
  private ViolationManager violationManager;
  private CriterionFactory criterionFactory;
  private RoadSystem roadSystem;

  /**
   * Constructor.
   */
  public CriteriaManager() {
    this.criteria = new ArrayList<>();
    this.criterionFactory = new CriterionFactory();
    this.criteria.addAll(this.criterionFactory.createValueCriteria());
    this.criteria.add(this.criterionFactory.createCompletenessCriterion());
  }

  /**
   * Sets only once a roadSystem to this CriteriaManager.
   *
   * @param roadSystem the road system whose elements
   *     will be observed by created criteria
   */
  public void setRoadSystem(RoadSystem roadSystem) {
    this.roadSystem = roadSystem;
    this.criterionFactory.setRoadSystem(roadSystem);
    this.criteria.stream()
        .filter(c -> c.getType() == PlausibilityCriterionType.COMPATIBILITY)
        .map(c -> (CompatibilityCriterion) c)
        .forEach(c -> c.setRoadSystem(roadSystem));
    var elements = roadSystem.getElements();
    var segments = elements.stream()
        .filter(element -> !element.isContainer())
        .map(element -> (Segment) element)
        .toList();
    for (Segment segment : segments) {
      for (PlausibilityCriterion criterion : criteria) {
        segment.addSubscriber(criterion);
      }
      segment.notifySubscribers();
    }
  }

  /**
   * Sets the {@link ViolationManager} that will receive the
   * {@link edu.kit.rose.model.plausibility.violation.Violation}s
   * of {@link PlausibilityCriterion} in this CriteriaManager.
   *
   * @param violationManager the violationManager this CriteriaManager is supposed to use.
   */
  public void setViolationManager(ViolationManager violationManager) {
    this.violationManager = violationManager;
    this.criterionFactory.setViolationManager(this.violationManager);
    this.criteria.forEach(c -> c.setViolationManager(this.violationManager));
  }

  /**
   * Provides a {@link SortedBox} containing all {@link PlausibilityCriterion}
   * that this CriteriaManager contains.
   *
   * @return a {@link SortedBox} containing all {@link PlausibilityCriterion}
   *        that this CriteriaManager contains.
   */
  public SortedBox<PlausibilityCriterion> getCriteria() {
    return new RoseSortedBox<>(this.criteria);
  }

  /**
   * Gives a {@link SortedBox} containing all {@link PlausibilityCriterion} of
   * the given {@link PlausibilityCriterionType} that this CriteriaManager contains.
   *
   * @param type The Criteria Type to look for.
   * @return a {@link SortedBox} containing all {@link PlausibilityCriterion} of the given type.
   */
  public SortedBox<PlausibilityCriterion> getCriteriaOfType(PlausibilityCriterionType type) {
    ArrayList<PlausibilityCriterion> typeCriteria = new ArrayList<>();

    for (PlausibilityCriterion criteria : this.criteria) {
      if (criteria.getType().equals(type)) {
        typeCriteria.add(criteria);
      }
    }
    return new RoseSortedBox<>(typeCriteria);
  }

  /**
   * Creates a new {@link CompatibilityCriterion}.
   *
   */
  public CompatibilityCriterion createCompatibilityCriterion() {
    CompatibilityCriterion newCriteria = this.criterionFactory.createCompatibilityCriterion();

    this.criteria.add(newCriteria);
    notifyAdditionToSubscribers(newCriteria);
    return newCriteria;
  }

  /**
   * Removes a given {@link PlausibilityCriterion} from this CriterionManager.
   *
   * @param criterion the Criterion to remove.
   */
  public void removeCriterion(PlausibilityCriterion criterion) {
    this.criteria.remove(criterion);
    roadSystem.getElements().forEach(element -> element.removeSubscriber(criterion));
    notifyRemovalToSubscribers(criterion);
    var violationsToRemove = violationManager.getViolations().stream()
        .filter(violation -> violation.violatedCriterion() == criterion)
        .toList();
    violationsToRemove.forEach(violationManager::removeViolation);
  }

  /**
   * Removes all {@link PlausibilityCriterion} from this CriterionManager.
   */
  public void removeAllCriteria() {
    this.criteria.forEach(this::notifyRemovalToSubscribers);
    this.criteria.clear();
  }

  /**
   * Removes all {@link PlausibilityCriterion} of the given {@link PlausibilityCriterionType}
   * from this CriterionManager.
   *
   * @param type the type of {@link PlausibilityCriterion} to remove.
   */
  public void removeAllCriteriaOfType(PlausibilityCriterionType type) {
    ArrayList<PlausibilityCriterion> toRemove = new ArrayList<>();
    for (PlausibilityCriterion criteria : this.criteria) {
      if (criteria.getType() == type) {
        notifyRemovalToSubscribers(criteria);
        toRemove.add(criteria);
      }
    }
    this.criteria.removeAll(toRemove);
  }

  @Override
  public CriteriaManager getThis() {
    return this;
  }

  @Override
  public void notifyChange(PlausibilityCriterion unit) {
    this.notifySubscribers();
  }

  private void notifyAdditionToSubscribers(PlausibilityCriterion criterion) {
    subscribers.forEach(s -> s.notifyAddition(criterion));
  }

  private void notifyRemovalToSubscribers(PlausibilityCriterion criterion) {
    subscribers.forEach(s -> s.notifyRemoval(criterion));
  }
}
