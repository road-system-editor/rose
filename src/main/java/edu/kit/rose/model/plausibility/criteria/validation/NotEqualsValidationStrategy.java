package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes a NotEquals Operator for different Objects. It uses the java.util.equals function.
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
class NotEqualsValidationStrategy<T> extends ValidationStrategy<T> {

  /**
   * Standard Constructor.
   */
  public NotEqualsValidationStrategy() {
    super(ValidationType.NOT_EQUALS);
  }

  @Override
  boolean validate(Object first, Object second) {
    return !first.equals(second);
  }

  @Override
  boolean validate(T first, T second, double legalDiscrepancy) {
    return validate(first, second);
  }
}
