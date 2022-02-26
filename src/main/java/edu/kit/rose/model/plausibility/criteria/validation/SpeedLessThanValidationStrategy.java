package edu.kit.rose.model.plausibility.criteria.validation;

import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;

/**
 * Describes a LessThan Operator for SpeedLimits. "<"
 *
 */
public class SpeedLessThanValidationStrategy extends ValidationStrategy<SpeedLimit> {

  /**
   * Standard Constructor.
   */
  public SpeedLessThanValidationStrategy() {
    super(ValidationType.LESS_THAN);
  }

  @Override
  public boolean validate(SpeedLimit first, SpeedLimit second) {
    return first.equals(second);
  }

  @Override
  public boolean validate(SpeedLimit first, SpeedLimit second, double legalDiscrepancy) {
    if (first.getValue() == 0 || second.getValue() == 0) {
      return true;
    }
    int discrepancy = Math.abs(first.getValue() - second.getValue());
    return discrepancy < legalDiscrepancy;
  }
}