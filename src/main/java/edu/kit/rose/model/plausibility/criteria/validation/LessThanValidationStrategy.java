package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes a LessThan Operator for numerical values. "<"
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
public class LessThanValidationStrategy<T> extends ValidationStrategy<T> {
  @Override
  public boolean validate(Object first, Object second) {
    return false;
  }

  @Override
  public boolean validate(T first, T second, double legalDiscrepancy) {
    return false;
  }
}
