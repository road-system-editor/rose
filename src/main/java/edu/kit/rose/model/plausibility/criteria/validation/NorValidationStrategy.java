package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * An Operator describing the logical NOR function for two booleans.
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
class NorValidationStrategy<T> extends ValidationStrategy<T> {
  @Override
  boolean validate(Object first, Object second) {
    return false;
  }

  @Override
  boolean validate(T first, T second, double legalDiscrepancy) {
    return false;
  }
}
