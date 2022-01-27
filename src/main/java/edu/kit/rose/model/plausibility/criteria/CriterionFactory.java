package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Element;


/**
 * A Factory for different kinds of PlausibilityCriteria.
 */
class CriterionFactory {
  private final RoadSystem roadSystem;
  private final ViolationManager violationManager;

  /**
   * Constructor.
   *
   * @param roadSystem The Roadsystem needed for CompatibilityCriterion.
   */
  public CriterionFactory(RoadSystem roadSystem, ViolationManager violationManager) {
    this.roadSystem = roadSystem;
    this.violationManager = violationManager;
  }

  /**
   * Creates a new CompletenessCriterion.
   *
   * @return the new CompletenessCriterion
   */
  public CompletenessCriterion createCompletenessCriterion() {
    CompletenessCriterion criterion = new CompletenessCriterion(this.violationManager);
    subscribeCriterionToSegments(criterion, this.roadSystem.getElements());
    return criterion;
  }

  /**
   * Creates a new ValueCriterion.
   *
   * @return the new ValueCriterion
   */
  public ValueCriterion createValueCriterion() {
    ValueCriterion criterion = new ValueCriterion(this.violationManager);
    subscribeCriterionToSegments(criterion, this.roadSystem.getElements());
    return criterion;
  }

  /**
   * Creates a new CompatibilityCriterion.
   *
   * @return the new CompatibilityCriterion
   */
  public CompatibilityCriterion createCompatibilityCriterion() {
    CompatibilityCriterion criterion =
            new CompatibilityCriterion(this.roadSystem, this.violationManager);
    subscribeCriterionToSegments(criterion, this.roadSystem.getElements());
    return criterion;
  }

  private void subscribeCriterionToSegments(PlausibilityCriterion criterion,
                                            Box<Element> elements) {
    if (elements != null) {
      for (Element element : elements) {
        if (!element.isContainer()) {
          element.addSubscriber(criterion);
        }
      }
    }
  }
}
