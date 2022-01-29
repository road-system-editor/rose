package edu.kit.rose.controller.plausibility;


import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.plausibility.criteria.validation.OperatorType;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;

/**
 * Provides functionality to work with
 * plausibility criteria and to export and import
 * the plausibility criteria.
 */
public interface PlausibilityController {

  /**
   * Adds a compatibility criterion to the plausibility system.
   */
  void addCompatibilityCriterion();


  /**
   * Set the name of a {@link CompatibilityCriterion}.
   *
   * @param criterion     compatibility criterion to set the name on
   * @param criterionName the new name
   */
  void setCompatibilityCriterionName(CompatibilityCriterion criterion, String criterionName);

  /**
   * Adds a {@link SegmentType} to a {@link CompatibilityCriterion}.
   *
   * @param criterion   the compatibility criterion
   * @param segmentType the segment type to add
   */
  void addSegmentTypeToCompatibilityCriterion(CompatibilityCriterion criterion,
                                              SegmentType segmentType);

  /**
   * Removes a {@link SegmentType} from a {@link CompatibilityCriterion}.
   *
   * @param criterion   the compatibility criterion
   * @param segmentType the segment type to remove
   */
  void removeSegmentTypeToCompatibilityCriterion(CompatibilityCriterion criterion,
                                                 SegmentType segmentType);

  /**
   * Sets the {@link AttributeType} of an {@link CompatibilityCriterion}.
   *
   * @param criterion     the criterion to set the attribute type
   * @param attributeType the new attribute type
   */
  void setCompatibilityCriterionAttributeType(CompatibilityCriterion criterion,
                                              AttributeType attributeType);

  /**
   * Sets the {@link OperatorType} of an {@link CompatibilityCriterion}.
   *
   * @param criterion    the criterion to set the operator type
   * @param operatorType the new operator type
   */
  void setCompatibilityCriterionOperatorType(CompatibilityCriterion criterion,
                                             OperatorType operatorType);

  /**
   * Sets the discrepancy of an {@link CompatibilityCriterion}.
   *
   * @param criterion   the criterion to set the discrepancy
   * @param discrepancy the new discrepancy of the criterion
   */
  void setCompatibilityCriterionLegalDiscrepancy(CompatibilityCriterion criterion,
                                                 double discrepancy);

  /**
   * Deletes the specified compatibility criterion
   * from the plausibility system.
   *
   * @param criterion the criterion to delete
   */
  void deleteCompatibilityCriterion(CompatibilityCriterion criterion);

  /**
   * Opens a file chooser and imports the compatibility criteria
   * from a file chosen by the user.
   */
  void importCompatibilityCriteria();

  /**
   * Opens a file chooser and exports the compatibility criteria
   * into a file chosen by the user.
   */
  void exportCompatibilityCriteria();

  /**
   * Changes the position of the background surface
   * to the segment that causes the given violation.
   *
   * @param violation the violation to jump to
   */
  void jumpToCriterionViolation(Violation violation);


  /**
   * Registers a runnable that gets called before the controller executes an import or
   * an export and a runnable that runs when it is done.
   *
   * @param onBegin called before the import or export
   * @param onDone  called after the import or export
   */
  void subscribeToPlausibilityIoAction(Runnable onBegin, Runnable onDone);

  /**
   * Unregisters a runnable that gets called before the controller executes an import or
   * an export and a runnable that runs when it is done.
   *
   * @param onBegin called before the import or export
   * @param onDone  called after the import or export
   */
  void unsubscribeFromPlausibilityIoAction(Runnable onBegin, Runnable onDone);
}

