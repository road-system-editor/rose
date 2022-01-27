package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SetObservable;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
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

  private ViolationManager violationManager;
  private final ArrayList<PlausibilityCriterion> criterion;
  private final CriterionFactory criterionFactory;

  /**
   * Constructor.
   *
   * @param roadSystem        the road system whose elements
   *                          will be observed by created criteria
   * @param violationManager  the violation manager that will receive the
   *                          {@link edu.kit.rose.model.plausibility.violation.Violation}s
   *                          of {@link PlausibilityCriterion} in this CriteriaManager.
   */
  public CriteriaManager(RoadSystem roadSystem, ViolationManager violationManager) {
    this.criterion = new ArrayList<>();
    this.criterionFactory = new CriterionFactory(roadSystem, violationManager);
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
  }

  /**
   * Provides a {@link SortedBox} containing all {@link PlausibilityCriterion}
   * that this CriteriaManager contains.
   *
   * @return a {@link SortedBox} containing all {@link PlausibilityCriterion}
   *        that this CriteriaManager contains.
   */
  public SortedBox<PlausibilityCriterion> getCriteria() {
    return new RoseSortedBox<>(this.criterion);
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

    for (PlausibilityCriterion criteria : this.criterion) {
      if (criteria.getType().equals(type)) {
        typeCriteria.add(criteria);
      }
    }
    return new RoseSortedBox<>(typeCriteria);
  }

  /**
   * Creates a new {@link PlausibilityCriterion} with the given {@link PlausibilityCriterionType}.
   *
   * @param type The Type of the new {@link PlausibilityCriterion}.
   */
  public void createCriterionOfType(PlausibilityCriterionType type) {
    PlausibilityCriterion newCriteria;
    switch (type) {
      case VALUE:         newCriteria = this.criterionFactory.createValueCriterion();
                          break;
      case COMPLETENESS:  newCriteria = this.criterionFactory.createCompletenessCriterion();
                          break;
      case COMPATIBILITY: newCriteria = this.criterionFactory.createCompatibilityCriterion();
                          break;
      default:            throw new IllegalArgumentException("no such plausibility "
             + "criterion type found");
    }

    this.criterion.add(newCriteria);
    notifyAdditionToSubscribers(getSubscriberIterator(), newCriteria);
  }

  /**
   * Removes a given {@link PlausibilityCriterion} from this CriterionManager.
   *
   * @param criteria the Criterion to remove.
   */
  public void removeCriterion(PlausibilityCriterion criteria) {
    this.criterion.remove(criteria);
    notifyRemovalToSubscribers(getSubscriberIterator(), criteria);
  }

  /**
   * Removes all {@link PlausibilityCriterion} from this CriterionManager.
   */
  public void removeAllCriteria() {
    this.criterion.forEach(e -> notifyRemovalToSubscribers(getSubscriberIterator(), e));
    this.criterion.clear();
  }

  /**
   * Removes all {@link PlausibilityCriterion} of the given {@link PlausibilityCriterionType}
   * from this CriterionManager.
   *
   * @param type the type of {@link PlausibilityCriterion} to remove.
   */
  public void removeAllCriteriaOfType(PlausibilityCriterionType type) {
    for (PlausibilityCriterion criteria : this.criterion) {
      if (criteria.getType() == type) {
        notifyRemovalToSubscribers(getSubscriberIterator(), criteria);
        this.criterion.remove(criteria);
      }
    }
  }

  @Override
  public CriteriaManager getThis() {
    return this;
  }

  @Override
  public void notifyChange(PlausibilityCriterion unit) {
    this.notifySubscribers();
  }

  private void notifyAdditionToSubscribers(Iterator<SetObserver<PlausibilityCriterion,
          CriteriaManager>> iterator, PlausibilityCriterion criterion) {
    while (iterator.hasNext()) {
      iterator.next().notifyAddition(criterion);
    }
  }

  private void notifyRemovalToSubscribers(Iterator<SetObserver<PlausibilityCriterion,
          CriteriaManager>> iterator, PlausibilityCriterion criterion) {
    while (iterator.hasNext()) {
      iterator.next().notifyRemoval(criterion);
    }
  }
}
