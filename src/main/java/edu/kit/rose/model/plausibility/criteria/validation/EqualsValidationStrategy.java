package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes an equals operator for different Objects. Uses the java.util.equals function.
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
class EqualsValidationStrategy<T> extends ValidationStrategy<T> {

  /**
   * Standard Constructor.
   */
  public EqualsValidationStrategy() {
    super(ValidationType.EQUALS);
  }

  @Override
  boolean validate(Object first, Object second) {
    return first.equals(second);
  }

  @Override
  boolean validate(T first, T second, double legalDiscrepancy) {
    return first.equals(second);
  }
}