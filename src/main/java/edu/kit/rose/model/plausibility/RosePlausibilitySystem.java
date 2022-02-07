package edu.kit.rose.model.plausibility;

import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import java.util.List;
import java.util.Objects;

/**
 * A Standard Implementation of the {@link PlausibilitySystem} Interface.
 */
public class RosePlausibilitySystem implements PlausibilitySystem {
  private final CriteriaManager criteriaManager;
  private final ViolationManager violationManager;
  private final RoadSystem roadSystem;

  /**
   * Creates a new plausibility system for ROSE.
   *
   * @param roadSystem The {@link RoadSystem} that this PlausibilitySystem is supposed to check, may
   *                   not be {@code null}.
   * @param criteriaManager The CriteriaManager holding the Criteria that are to be checked against,
   *                        may not be {@code null}.
   */
  public RosePlausibilitySystem(CriteriaManager criteriaManager, RoadSystem roadSystem) {
    this.criteriaManager = Objects.requireNonNull(criteriaManager);
    this.roadSystem = Objects.requireNonNull(roadSystem);
    this.violationManager = new ViolationManager();
  }

  @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance") //TODO: Remove
  @Override
  public ViolationManager getViolationManager() {
    //TODO: Remove this, this is for testing
    final CompatibilityCriterion firstCriterion = new CompatibilityCriterion(roadSystem,
        violationManager);
    final CompatibilityCriterion secondCriterion = new CompatibilityCriterion(roadSystem,
        violationManager);
    final CompatibilityCriterion thirdCriterion = new CompatibilityCriterion(roadSystem,
        violationManager);
    final CompatibilityCriterion fourthCriterion = new CompatibilityCriterion(roadSystem,
        violationManager);
    final CompatibilityCriterion fifthCriterion = new CompatibilityCriterion(roadSystem,
        violationManager);
    final CompatibilityCriterion sixtCriterion = new CompatibilityCriterion(roadSystem,
        violationManager);

    firstCriterion.setName("firstCriterion");
    secondCriterion.setName("secondCriterion");
    thirdCriterion.setName("thirdCriterion");
    fourthCriterion.setName("fourthssssssssssCriterion");
    fifthCriterion.setName("fifthCriterion");
    sixtCriterion.setName("sixtCriterion");
    violationManager.addViolation(new Violation(firstCriterion, List.of(new Base(), new Base())));
    violationManager.addViolation(new Violation(secondCriterion, List.of(new Base(), new Base())));
    violationManager.addViolation(new Violation(thirdCriterion, List.of(new Base(), new Base())));
    violationManager.addViolation(new Violation(fourthCriterion, List.of(new Base(), new Base())));
    violationManager.addViolation(new Violation(fifthCriterion, List.of(new Base(), new Base())));
    violationManager.addViolation(new Violation(sixtCriterion, List.of(new Base(), new Base())));
    //TODO: End of Test
    return this.violationManager;
  }

  @Override
  public CriteriaManager getCriteriaManager() {
    return this.criteriaManager;
  }

  @Override
  public void checkAll() {
    for (var criterion : criteriaManager.getCriteria()) {
      for (var element : roadSystem.getElements()) {
        criterion.notifyChange(element);
      }
    }
  }
}
