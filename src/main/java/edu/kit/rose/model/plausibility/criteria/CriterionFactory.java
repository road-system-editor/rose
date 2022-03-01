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
    var criterion = new CompletenessCriterion((this.violationManager));
    subscribeAndCheckSegmentsIfPossible(criterion);
    return criterion;
  }

  /**
   * Creates new ValueCriteria.
   *
   * @return the list of new ValueCriteria
   */
  public List<ValueCriterion> createValueCriteria() {
    ArrayList<ValueCriterion> valueCriteria = new ArrayList<>();

    valueCriteria.add(new ValueCriterion(this.violationManager, AttributeType.LANE_COUNT,
        ValueCriterion.LANE_COUNT_RANGE));

    valueCriteria.add(new ValueCriterion(this.violationManager, AttributeType.LANE_COUNT_RAMP,
        ValueCriterion.LANE_COUNT_RAMP_RANGE));

    valueCriteria.add(new ValueCriterion(this.violationManager, AttributeType.LENGTH,
        ValueCriterion.LENGTH_RANGE));

    valueCriteria.add(new ValueCriterion(this.violationManager, AttributeType.SLOPE,
        ValueCriterion.SLOPE_RANGE));

    for (var criterion : valueCriteria) {
      subscribeAndCheckSegmentsIfPossible(criterion);
    }

    return valueCriteria;
  }

  public List<CompatibilityCriterion> createCompatibilityCriteria() {
    ArrayList<CompatibilityCriterion> compatibilityCriteria = new ArrayList<>();
    compatibilityCriteria.add(new CompatibilityCriterion(roadSystem, violationManager,
        AttributeType.))

  }

  /**
   * Creates a new CompatibilityCriterion.
   *
   * @return the new CompatibilityCriterion
   */
  public CompatibilityCriterion createCompatibilityCriterion() {
    var criterion = new CompatibilityCriterion(this.roadSystem, this.violationManager);
    subscribeAndCheckSegmentsIfPossible(criterion);
    return criterion;
  }

  private void subscribeAndCheckSegmentsIfPossible(PlausibilityCriterion criterion) {
    if (this.roadSystem != null) {
      for (Element element : this.roadSystem.getElements()) {
        if (!element.isContainer()) {
          element.addSubscriber(criterion);
          criterion.notifyChange(element);
        }
      }
    }
  }
}
