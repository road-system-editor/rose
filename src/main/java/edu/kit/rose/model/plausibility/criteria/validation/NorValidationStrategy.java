package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * An Operator describing the logical NOR function for two booleans.
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
class NorValidationStrategy<T> extends ValidationStrategy<T> {

  /**
   * Standard Constructor.
   */
  public NorValidationStrategy() {
    super(ValidationType.NOR);
  }

  @Override
  boolean validate(Object first, Object second) {
    Boolean one = false;
    Boolean two = false;

    try {
      one = (Boolean) first;
      two = (Boolean) second;
    } catch (ClassCastException classCastException) {
      classCastException.printStackTrace();
    }

    return !(one || two);
  }

  @Override
  boolean validate(T first, T second, double legalDiscrepancy) {
    return validate(first, second);
  }
}
