package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;
import java.util.ArrayList;
import java.util.List;


/**
 * A Factory for different kinds of PlausibilityCriteria.
 */
class CriterionFactory {
  private ViolationManager violationManager;
  private RoadSystem roadSystem;

  /**
   * Sets the {@link RoadSystem} used by this CriterionFactory.
   * Will be inserted in the compatibilityCriteria created by this factory.
   *
   * @param roadSystem the roadSystem.
   */
  public void setRoadSystem(RoadSystem roadSystem) {
    this.roadSystem = roadSystem;
  }

  /**
   * Sets the {@link ViolationManager} used by this CriterionFactory.
   * Will be inserted in the criteria created by this factory.
   *
   * @param violationManager the violationManager.
   */
  public void setViolationManager(ViolationManager violationManager) {
    this.violationManager = violationManager;
  }

  /**
   * Creates a new CompletenessCriterion.
   *
   * @return the new CompletenessCriterion
   */
  public CompletenessCriterion createCompletenessCriterion() {
    CompletenessCriterion criterion = new CompletenessCriterion((this.violationManager));
    if (this.roadSystem != null) {
      this.roadSystem.getElements().forEach(element -> element.addSubscriber(criterion));
    }
    return criterion;
  }

  /**
   * Creates new ValueCriteria.
   *
   * @return the list of new ValueCriteria
   */
  public List<ValueCriterion> createValueCriteria() {
    ArrayList<ValueCriterion> valueCriteria = new ArrayList<>();

    ValueCriterion criterion1 = new ValueCriterion(this.violationManager, AttributeType.LANE_COUNT,
            ValueCriterion.LANE_COUNT_RANGE);
    valueCriteria.add(criterion1);

    ValueCriterion criterion2 = new ValueCriterion(this.violationManager,
        AttributeType.LANE_COUNT_RAMP,
            ValueCriterion.LANE_COUNT_RAMP_RANGE);
    valueCriteria.add(criterion2);

    ValueCriterion criterion3 = new ValueCriterion(this.violationManager, AttributeType.LENGTH,
            ValueCriterion.LENGTH_RANGE);
    valueCriteria.add(criterion3);

    ValueCriterion criterion4 = new ValueCriterion(this.violationManager, AttributeType.MAX_SPEED,
            ValueCriterion.MAX_SPEED_RANGE);
    valueCriteria.add(criterion4);

    ValueCriterion criterion5 = new ValueCriterion(this.violationManager,
        AttributeType.MAX_SPEED_RAMP,
            ValueCriterion.MAX_SPEED_RAMP_RANGE);
    valueCriteria.add(criterion5);

    ValueCriterion criterion6 = new ValueCriterion(this.violationManager, AttributeType.SLOPE,
            ValueCriterion.SLOPE_RANGE);
    valueCriteria.add(criterion6);

    if (this.roadSystem != null) {
      this.roadSystem.getElements().forEach(element -> element.addSubscriber(criterion1));
      this.roadSystem.getElements().forEach(element -> element.addSubscriber(criterion2));
      this.roadSystem.getElements().forEach(element -> element.addSubscriber(criterion3));
      this.roadSystem.getElements().forEach(element -> element.addSubscriber(criterion4));
      this.roadSystem.getElements().forEach(element -> element.addSubscriber(criterion5));
      this.roadSystem.getElements().forEach(element -> element.addSubscriber(criterion6));
    }

    return valueCriteria;
  }

  /**
   * Creates a new CompatibilityCriterion.
   *
   * @return the new CompatibilityCriterion
   */
  public CompatibilityCriterion createCompatibilityCriterion() {
    CompatibilityCriterion criterion =
        new CompatibilityCriterion(this.roadSystem, this.violationManager);
    if (this.roadSystem != null) {
      this.roadSystem.getElements().forEach(element -> {
        if (!element.isContainer()) {
          element.addSubscriber(criterion);
          element.notifySubscribers();
        }
      });

    }
    return criterion;
  }
}
