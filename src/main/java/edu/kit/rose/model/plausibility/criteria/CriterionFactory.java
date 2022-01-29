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
    subscribeCriterionToSegments(criterion);
    return criterion;
  }

  /**
   * Creates a new ValueCriterion.
   *
   * @return the new ValueCriterion
   */
  public List<ValueCriterion> createValueCriterion() {
    ArrayList<ValueCriterion> valueCriterion = new ArrayList<>();

    ValueCriterion criterion = new ValueCriterion(this.violationManager, AttributeType.LANE_COUNT,
            ValueCriterion.LANE_COUNT_RANGE);
    valueCriterion.add(criterion);
    subscribeCriterionToSegments(criterion);

    criterion = new ValueCriterion(this.violationManager, AttributeType.LANE_COUNT_RAMP,
            ValueCriterion.LANE_COUNT_RAMP_RANGE);
    valueCriterion.add(criterion);
    subscribeCriterionToSegments(criterion);

    criterion = new ValueCriterion(this.violationManager, AttributeType.LENGTH,
            ValueCriterion.LENGTH_RANGE);
    valueCriterion.add(criterion);
    subscribeCriterionToSegments(criterion);

    criterion = new ValueCriterion(this.violationManager, AttributeType.MAX_SPEED,
            ValueCriterion.MAX_SPEED_RANGE);
    valueCriterion.add(criterion);
    subscribeCriterionToSegments(criterion);

    criterion = new ValueCriterion(this.violationManager, AttributeType.MAX_SPEED_RAMP,
            ValueCriterion.MAX_SPEED_RAMP_RANGE);
    valueCriterion.add(criterion);
    subscribeCriterionToSegments(criterion);

    criterion = new ValueCriterion(this.violationManager, AttributeType.SLOPE,
            ValueCriterion.SLOPE_RANGE);
    valueCriterion.add(criterion);
    subscribeCriterionToSegments(criterion);

    return valueCriterion;
  }

  /**
   * Creates a new CompatibilityCriterion.
   *
   * @return the new CompatibilityCriterion
   */
  public CompatibilityCriterion createCompatibilityCriterion() {
    CompatibilityCriterion criterion =
            new CompatibilityCriterion(this.roadSystem, this.violationManager);
    subscribeCriterionToSegments(criterion);
    return criterion;
  }

  private void subscribeCriterionToSegments(PlausibilityCriterion criterion) {
    for (Element element : this.roadSystem.getElements()) {
      element.addSubscriber(criterion);
    }
  }
}
