package edu.kit.rose.controller.plausibility;

import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;


/**
 * Provides functionality to work with
 * plausibility criteria and to export and import
 * the plausibility criteria.
 */
public class RosePlausibilityController extends Controller implements PlausibilityController {

  /**
   * Creates a new {@link RosePlausibilityController}.
   *
   * @param storageLock           the coordinator for controller actions
   * @param navigator             the navigator for the controller
   * @param project               the model facade for project data
   * @param applicationDataSystem the model facade for application data
   */
  public RosePlausibilityController(StorageLock storageLock, Navigator navigator, Project project,
                                    ApplicationDataSystem applicationDataSystem) {
    super(storageLock, navigator);
  }

  @Override
  public void addCompatibilityCriterion(PlausibilityCriterionType type) {

  }

  @Override
  public void setCompatibilityCriterionName(CompatibilityCriterion criterion,
                                            String criterionName) {

  }

  @Override
  public void addSegmentTypeToCompatibilityCriterion(CompatibilityCriterion criterion,
                                                     SegmentType segmentType) {

  }

  @Override
  public void removeSegmentTypeToCompatibilityCriterion(CompatibilityCriterion criterion,
                                                        SegmentType segmentType) {

  }

  @Override
  public void setCompatibilityCriterionAttributeType(CompatibilityCriterion criterion,
                                                     AttributeType attributeType) {

  }

  @Override
  public void setCompatibilityCriterionOperatorType(CompatibilityCriterion criterion,
                                                    ValidationType validationType) {

  }

  @Override
  public void setCompatibilityCriterionLegalDiscrepancy(CompatibilityCriterion criterion,
                                                        double discrepancy) {

  }


  @Override
  public void deleteCompatibilityCriterion(CompatibilityCriterion criterion) {

  }

  @Override
  public void importCompatibilityCriteria() {

  }

  @Override
  public void exportCompatibilityCriteria() {

  }

  @Override
  public void jumpToCriterionViolation(Violation violation) {

  }

  @Override
  public void subscribeToPlausibilityIoAction(Runnable onBegin, Runnable onDone) {

  }

  @Override
  public void unsubscribeFromPlausibilityIoAction(Runnable onBegin, Runnable onDone) {

  }
}