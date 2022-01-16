package edu.kit.rose.model.plausibility.criteria;

/**
 * Different types of {@link PlausibilityCriterion}s. Used to determine the concrete type of given
 * {@link PlausibilityCriterion}. Allows quick access to all possible Criterion-types.
 */
public enum PlausibilityCriterionType {
  COMPLETENESS,
  VALUE,
  COMPATIBILITY
}
