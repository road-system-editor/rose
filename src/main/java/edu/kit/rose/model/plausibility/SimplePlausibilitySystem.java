package edu.kit.rose.model.plausibility;

import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;

/**
 * A Standard Implementation of the {@link PlausibilitySystem} Interface.
 */
public class SimplePlausibilitySystem implements PlausibilitySystem {

  /**
   * Constructor.
   *
   * @param roadSystem      The {@link RoadSystem} that this PlausibilitySystem is supposed to check.
   * @param criteriaManager The CriteriaManager holding the Criteria that are to be checked against.
   */
  public SimplePlausibilitySystem(CriteriaManager criteriaManager, RoadSystem roadSystem) {

  }

  @Override
  public ViolationManager getViolationManager() {
    return null;
  }

  @Override
  public CriteriaManager getCriteriaManager() {
    return null;
  }

  @Override
  public void checkAll() {

  }
}
