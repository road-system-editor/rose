package edu.kit.rose.model.plausibility;

import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.violation.ViolationManager;

/**
 * A Plausibility System consists of an {@link ViolationManager} and an {@link CriteriaManager}.
 * The {@link CriteriaManager} holds {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion}
 * that will be applied to a {@link edu.kit.rose.model.roadsystem.RoadSystem}. Possible violations will be
 * added to the {@link edu.kit.rose.model.plausibility.violation.ViolationManager}.
 */
public interface PlausibilitySystem {

    /**
     *
     * @return the ViolationManager this PlausibilitySystem uses.
     */
    ViolationManager getViolationManager();

    /**
     *
     * @return the CriteriaManager this PlausibilitySystem uses.
     */
    CriteriaManager getCriteriaManager();

    /**
     * Checks all {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion} in the {@link CriteriaManager}.
     * This is only supposed to be used when importing a new {@link edu.kit.rose.model.roadsystem.RoadSystem} or a
     * new set of {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion}.
     */
    void checkAll();
}
