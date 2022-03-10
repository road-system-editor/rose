package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes an equals validation strategy for different Objects. Uses the java.util.equals
 * function.
 *
 * @param <T> The Type that this ValidationStrategy is applied to.
 */
public class EqualsValidationStrategy<T> extends ValidationStrategy<T> {

  /**
   * Standard Constructor.
   */
  public EqualsValidationStrategy() {
    super(ValidationType.EQUALS);
  }

  @Override
  public boolean validate(Object first, Object second) {
    return first.equals(second);
  }

  @Override
  public boolean validate(T first, T second, double legalDiscrepancy) {
    return this.validate(first, second);
  }
}