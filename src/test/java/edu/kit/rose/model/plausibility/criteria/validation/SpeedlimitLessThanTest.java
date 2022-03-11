package edu.kit.rose.model.plausibility.criteria.validation;

import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link SpeedLessThanValidationStrategy} Class.
 */
class SpeedlimitLessThanTest {
  private ValidationStrategy<SpeedLimit> speedLimitStrategy;


  @BeforeEach
  void beforeEach() {
    speedLimitStrategy = new SpeedLessThanValidationStrategy();
  }

  @Test
  void testWithoutDiscrepancy() {
    Assertions.assertTrue(speedLimitStrategy.validate(SpeedLimit.T60, SpeedLimit.T60));
    Assertions.assertTrue(speedLimitStrategy.validate(SpeedLimit.SBA, SpeedLimit.SBA));
    Assertions.assertFalse(speedLimitStrategy.validate(SpeedLimit.SBA, SpeedLimit.T60));
    Assertions.assertFalse(speedLimitStrategy.validate(SpeedLimit.SBA, SpeedLimit.TUNNEL));
  }

  @Test
  void testWithDiscrepancy() {
    Assertions.assertTrue(speedLimitStrategy
        .validate(SpeedLimit.T60, SpeedLimit.T60, 30));
    Assertions.assertTrue(speedLimitStrategy
        .validate(SpeedLimit.T60, SpeedLimit.T80, 30));
    Assertions.assertFalse(speedLimitStrategy
        .validate(SpeedLimit.T60, SpeedLimit.T100, 30));

    //speedlimits that dont have a fixed value are ignored when a discrepancy is given
    Assertions.assertTrue(speedLimitStrategy
        .validate(SpeedLimit.T60, SpeedLimit.NONE, 30));
    Assertions.assertTrue(speedLimitStrategy
        .validate(SpeedLimit.T60, SpeedLimit.TUNNEL, 30));
    Assertions.assertTrue(speedLimitStrategy
        .validate(SpeedLimit.TUNNEL, SpeedLimit.NONE, 30));
  }
}
