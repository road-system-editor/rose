package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * An Operator describing the logical OR function for two booleans.
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
public class OrValidationStrategy<T> extends ValidationStrategy<T> {
  @Override
  public boolean validate(Object first, Object second) {
    return false;
  }

  @Override
  public boolean validate(T first, T second, double legalDiscrepancy) {
    return false;
  }
}
