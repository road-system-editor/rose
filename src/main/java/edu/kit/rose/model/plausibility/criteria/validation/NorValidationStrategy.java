package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * An Operator describing the logical NOR function for two booleans.
 */
public class NorValidationStrategy extends ValidationStrategy<Boolean> {

  /**
   * Standard Constructor.
   */
  public NorValidationStrategy() {
    super(ValidationType.NOR);
  }

  @Override
  public boolean validate(Boolean first, Boolean second) {
    return !Boolean.logicalOr(first, second);
  }

  @Override
  public boolean validate(Boolean first, Boolean second, double legalDiscrepancy) {
    return this.validate(first, second);
  }

}
