package edu.kit.rose.model.plausibility.criteria.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the Or and NOR Validation Strategies.
 */
public class LessThanValidationTest {
  private static final Integer smallInteger = 10;
  private static final Integer largeInteger = 99999999;
  private static final Double smallDouble = 10.0;
  private static final Double largeDouble = 99999999.0;
  private ValidationStrategy<Integer> integerLessThanValidationaStrategy;
  private ValidationStrategy<Double> doubleLessThanValidationaStrategy;

  @BeforeEach
  public void initialize() {
    integerLessThanValidationaStrategy = new LessThanValidationStrategy<>();
    doubleLessThanValidationaStrategy = new LessThanValidationStrategy<>();
  }

  @Test
  public void testWithoutDiscrepancy() {
    Assertions.assertFalse(integerLessThanValidationaStrategy.validate(smallInteger, largeInteger));
    Assertions.assertFalse(doubleLessThanValidationaStrategy.validate(smallDouble, largeDouble));
    Assertions.assertTrue(integerLessThanValidationaStrategy.validate(smallInteger, smallInteger));
    Assertions.assertTrue(doubleLessThanValidationaStrategy.validate(smallDouble, smallDouble));
  }

  /**
   * Same test as above (that used small and large) just switched small and large.
   */
  @Test
  public void testWithoutDiscrepancyNewOrder() {
    Assertions.assertFalse(integerLessThanValidationaStrategy.validate(largeInteger, smallInteger));
    Assertions.assertFalse(doubleLessThanValidationaStrategy.validate(largeDouble, smallDouble));
  }

  @Test
  public void testDiscrepancy() {
    Assertions.assertTrue(integerLessThanValidationaStrategy.validate(smallInteger, largeInteger,
        largeDouble));
    Assertions.assertTrue(doubleLessThanValidationaStrategy.validate(smallDouble, largeDouble,
        largeDouble));
    Assertions.assertFalse(integerLessThanValidationaStrategy.validate(smallInteger, largeInteger,
        smallDouble));
    Assertions.assertFalse(doubleLessThanValidationaStrategy.validate(smallDouble, largeDouble,
        smallDouble));
    Assertions.assertTrue(doubleLessThanValidationaStrategy.validate(largeDouble, largeDouble,
        largeDouble));
    Assertions.assertTrue(doubleLessThanValidationaStrategy.validate(largeDouble, largeDouble,
        smallDouble));
  }

  /**
   * Same test as above (that used small and large) just switched small and large.
   */
  @Test
  public void testDiscrepancyNewOrder() {
    Assertions.assertTrue(integerLessThanValidationaStrategy.validate(largeInteger, smallInteger,
        largeDouble));
    Assertions.assertTrue(doubleLessThanValidationaStrategy.validate(largeDouble, smallDouble,
        largeDouble));
    Assertions.assertFalse(integerLessThanValidationaStrategy.validate(largeInteger, smallInteger,
        smallDouble));
    Assertions.assertFalse(doubleLessThanValidationaStrategy.validate(largeDouble, smallDouble,
        smallDouble));
  }
}
