package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes a NotEquals Operator for different Objects. It uses the java.util.equals function.
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
public class NotEqualsValidationStrategy<T> extends ValidationStrategy<T> {
  @Override
  public boolean validate(Object first, Object second) {
    return false;
  }

  @Override
  public boolean validate(T first, T second, double legalDiscrepancy) {
    return false;
  }
}
