package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.SetObservable;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.plausibility.violation.ViolationManager;

/**
 * A CriteriaManager holds {@link PlausibilityCriterion}s .
 * It provides functions to create and remove Criteria as well as getters for them.
 * Provided with a {@link edu.kit.rose.model.roadsystem.RoadSystem} it will set up created
 * Criteria to observe the appropriate {@link edu.kit.rose.model.roadsystem.elements.Segment}s.
 */
public class CriteriaManager implements SetObservable<PlausibilityCriterion, CriteriaManager>,
    UnitObserver<PlausibilityCriterion> {

  @Override
  public void addSubscriber(SetObserver<PlausibilityCriterion, CriteriaManager> setObserver) {

  }

  @Override
  public void removeSubscriber(SetObserver<PlausibilityCriterion, CriteriaManager> setObserver) {

  }

  /**
   * Sets the {@link ViolationManager} that will receive the
   * {@link edu.kit.rose.model.plausibility.violation.Violation}s
   * of {@link PlausibilityCriterion} in this CriteriaManager.
   * Sets the {@link ViolationManager} that will receive the
   * {@link edu.kit.rose.model.plausibility.violation.Violation}s of
   * {@link PlausibilityCriterion} in this CriteriaManager.
   *
   * @param violationManager the violationManager this CriteriaManager is supposed to use.
   */
  public void setViolationManager(ViolationManager violationManager) {

  }

  /**
   * Provides a {@link SortedBox} containing all {@link PlausibilityCriterion}
   * that this CriteriaManager contains.
   *
   * @return a {@link SortedBox} containing all {@link PlausibilityCriterion}
   *        that this CriteriaManager contains.
   */
  public SortedBox<PlausibilityCriterion> getCriteria() {
    return null;
  }

  /**
   * Gives a {@link SortedBox} containing all {@link PlausibilityCriterion} of
   * the given {@link PlausibilityCriterionType} that this CriteriaManager contains.
   *
   * @param type The Criteria Type to look for.
   * @return a {@link SortedBox} containing all {@link PlausibilityCriterion} of the given type.
   */
  public SortedBox<PlausibilityCriterion> getCriteriaOfType(PlausibilityCriterionType type) {
    return null;
  }

  /**
   * Creates a new {@link PlausibilityCriterion} with the given {@link PlausibilityCriterionType}.
   *
   * @param type The Type of the new {@link PlausibilityCriterion}.
   */
  public void createCriterionOfType(PlausibilityCriterionType type) {
  }

  /**
   * Removes a given {@link PlausibilityCriterion} from this CriterionManager.
   *
   * @param criteria the Criterion to remove.
   */
  public void removeCriterion(PlausibilityCriterion criteria) {

  }

  /**
   * Removes all {@link PlausibilityCriterion} from this CriterionManager.
   */
  public void removeAllCriteria() {

  }

  /**
   * Removes all {@link PlausibilityCriterion} of the given {@link PlausibilityCriterionType}
   * from this CriterionManager.
   *
   * @param type the type of {@link PlausibilityCriterion} to remove.
   */
  public void removeAllCriteriaOfType(PlausibilityCriterionType type) {

  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public void notifyChange(PlausibilityCriterion unit) {

  }
}
