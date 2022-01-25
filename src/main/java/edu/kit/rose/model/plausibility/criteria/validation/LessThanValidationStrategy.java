package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes a LessThan Operator for numerical values. "<"
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
class LessThanValidationStrategy<T extends Number> extends ValidationStrategy<T> {

  /**
   * Standard Constructor.
   */
  public LessThanValidationStrategy() {
    super(ValidationType.LESS_THAN);
  }

  @Override
  public boolean validate(T first, T second) {
    int result = Double.compare(first.doubleValue(), second.doubleValue());
    return result == 0;
  }

  @Override
  public boolean validate(T first, T second, double legalDiscrepancy) {

    double discrepancy;
    if (Double.compare(first.doubleValue(), second.doubleValue()) > 0) {
      discrepancy = first.doubleValue() - second.doubleValue();
    } else {
      discrepancy = second.doubleValue() - first.doubleValue();
    }
    discrepancy = Math.abs(discrepancy);
    return discrepancy < legalDiscrepancy;
  }
}
