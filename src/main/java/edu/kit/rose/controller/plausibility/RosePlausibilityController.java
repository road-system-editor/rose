package edu.kit.rose.controller.plausibility;

import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.plausibility.criteria.validation.OperatorType;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * Provides functionality to work with
 * plausibility criteria and to export and import
 * the plausibility criteria.
 */
public class RosePlausibilityController extends Controller implements PlausibilityController {
  private final StorageLock storageLock;
  private final Navigator navigator;
  private final Project project;
  private final ApplicationDataSystem applicationDataSystem;
  private final Set<Runnable> onBeginSubscribers;
  private final Set<Runnable> onDoneSubscribers;

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
    this.storageLock = storageLock;
    this.navigator = navigator;
    this.project = project;
    this.applicationDataSystem = applicationDataSystem;
    this.onBeginSubscribers = new HashSet<>();
    this.onDoneSubscribers = new HashSet<>();
  }

  @Override
  public void addCompatibilityCriterion(PlausibilityCriterionType type) {
    this.applicationDataSystem.getCriteriaManager().createCriterionOfType(type);
  }

  @Override
  public void setCompatibilityCriterionName(CompatibilityCriterion criterion,
                                            String criterionName) {
    criterion.setName(criterionName);

  }

  @Override
  public void addSegmentTypeToCompatibilityCriterion(CompatibilityCriterion criterion,
                                                     SegmentType segmentType) {
    criterion.addSegmentType(segmentType);
  }

  @Override
  public void removeSegmentTypeToCompatibilityCriterion(CompatibilityCriterion criterion,
                                                        SegmentType segmentType) {
    criterion.removeSegmentType(segmentType);
  }

  @Override
  public void setCompatibilityCriterionAttributeType(CompatibilityCriterion criterion,
                                                     AttributeType attributeType) {
    criterion.setAttributeType(attributeType);
  }

  @Override
  public void setCompatibilityCriterionOperatorType(CompatibilityCriterion criterion,
                                                    OperatorType operatorType) {
    criterion.setOperatorType(operatorType);
  }

  @Override
  public void setCompatibilityCriterionLegalDiscrepancy(CompatibilityCriterion criterion,
                                                        double discrepancy) {
    criterion.setLegalDiscrepancy(discrepancy);
  }


  @Override
  public void deleteCompatibilityCriterion(CompatibilityCriterion criterion) {
    this.applicationDataSystem.getCriteriaManager().removeCriterion(criterion);
  }

  @Override
  public void importCompatibilityCriteria() {
    if (this.storageLock.acquireStorageLock()) {
      this.onBeginSubscribers.forEach((Runnable::run));
      this.applicationDataSystem.importCriteriaFromFile(this.navigator.showFileDialog());
      this.onDoneSubscribers.forEach(Runnable::run);
      this.storageLock.releaseStorageLock();
    }
  }

  @Override
  public void exportCompatibilityCriteria() {
    if (this.storageLock.acquireStorageLock()) {
      this.storageLock.acquireStorageLock();
      this.onBeginSubscribers.forEach((Runnable::run));
      this.applicationDataSystem.exportCriteriaToFile(this.navigator.showFileDialog());
      this.onDoneSubscribers.forEach(Runnable::run);
      this.storageLock.releaseStorageLock();
    }
  }

  @Override
  public void jumpToCriterionViolation(Violation violation) {
    Collection<Segment> offendingSegments = violation.offendingSegments();
    double sumX = 0;
    double sumY = 0;
    for (Segment segment : offendingSegments) {
      sumX += segment.getCenter().getX();
      sumY += segment.getCenter().getY();
    }
    sumX = sumX / offendingSegments.size();
    sumY = sumY / offendingSegments.size();
    this.project.getZoomSetting()
            .setCenterOfView(new Position((int) Math.round(sumX), (int) Math.round(sumY)));
  }

  @Override
  public void subscribeToPlausibilityIoAction(Runnable onBegin, Runnable onDone) {
    this.onBeginSubscribers.add(onBegin);
    this.onDoneSubscribers.add(onDone);
  }

  @Override
  public void unsubscribeFromPlausibilityIoAction(Runnable onBegin, Runnable onDone) {
    this.onBeginSubscribers.remove(onBegin);
    this.onDoneSubscribers.remove(onDone);
  }
}