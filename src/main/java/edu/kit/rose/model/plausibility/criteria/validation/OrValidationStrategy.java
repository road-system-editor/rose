package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * An Operator describing the logical OR function for two booleans.
 */
public class OrValidationStrategy extends ValidationStrategy<Boolean> {

  /**
   * Standard Constructor.
   */
  public OrValidationStrategy() {
    super(ValidationType.OR);
  }

  @Override
  public boolean validate(Boolean first, Boolean second) {
    return Boolean.logicalOr(first, second);
  }

  @Override
  public boolean validate(Boolean first, Boolean second, double legalDiscrepancy) {
    return this.validate(first, second);
  }
}
