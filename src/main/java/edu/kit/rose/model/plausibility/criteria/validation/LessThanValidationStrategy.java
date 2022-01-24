package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes a LessThan Operator for numerical values. "<"
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
class LessThanValidationStrategy<T> extends ValidationStrategy<T> {

  /**
   * Standard Constructor.
   */
  public LessThanValidationStrategy() {
    super(ValidationType.LESS_THAN);
  }

  @Override
  boolean validate(Object first, Object second) {
    return false;
  }

  @Override
  boolean validate(T first, T second, double legalDiscrepancy) {
    return false;
  }
}
