package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes an equals operator for different Objects. Uses the java.util.equals function.
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
public class EqualsValidationStrategy<T> extends ValidationStrategy<T> {
  @Override
  public boolean validate(Object first, Object second) {
    return false;
  }

  @Override
  public boolean validate(T first, T second, double legalDiscrepancy) {
    return false;
  }
}