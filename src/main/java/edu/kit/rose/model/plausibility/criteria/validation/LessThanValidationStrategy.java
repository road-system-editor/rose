package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes a LessThan Operator for numerical values. "<"
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
class LessThanValidationStrategy<T extends Comparable<T>> extends ValidationStrategy<T> {

  /**
   * Standard Constructor.
   */
  public LessThanValidationStrategy() {
    super(ValidationType.LESS_THAN);
  }

  @Override
  boolean validate(T first, T second) {
    int result = first.compareTo(second);
    return result < 0;
  }

  @Override
  boolean validate(T first, T second, double legalDiscrepancy) {

    double discrepancy;
    if (first.compareTo(second) < 0) {
      discrepancy = (Double) first - (Double) second;
    } else {
      discrepancy = (Double) second - (Double) first;
    }
    return discrepancy < legalDiscrepancy;
  }
}
