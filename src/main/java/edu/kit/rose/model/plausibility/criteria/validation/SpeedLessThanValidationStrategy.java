package edu.kit.rose.model.plausibility.criteria.validation;

import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import java.util.Objects;

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
    return Objects.equals(first, second);
  }

  @Override
  public boolean validate(SpeedLimit first, SpeedLimit second, double legalDiscrepancy) {
    //Ignore if the values have not been configured by the user (null).
    //That case is handled by CompletenessCriterion.
    if (first == null || second == null
        || first.getValue() == 0 || second.getValue() == 0) {
      return true;
    }
    int discrepancy = Math.abs(first.getValue() - second.getValue());
    return discrepancy < legalDiscrepancy;
  }
}