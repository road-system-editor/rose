package edu.kit.rose.model.plausibility.criteria.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Equals Validation Strategies.
 */
public class EqualsValidationTest {

  private static final int legalDiscrepancy = 10;
  private static ValidationStrategy<String> notEqualsValidationStrategy;
  private static ValidationStrategy<String> equalsValidationStrategy;

  /**
   * Initialize.
   */
  @BeforeEach
  public void initialize() {
    notEqualsValidationStrategy = new NotEqualsValidationStrategy<>();
    equalsValidationStrategy = new EqualsValidationStrategy<>();
  }

  @Test
  public void testEqualsValidationStrategy() {
    String first = "first";
    String second = "second";
    Assertions.assertTrue(equalsValidationStrategy.validate(first, first));
    Assertions.assertTrue(equalsValidationStrategy.validate(second, second));
    Assertions.assertFalse(equalsValidationStrategy.validate(first, second));
    Assertions.assertFalse(equalsValidationStrategy.validate(second, first));

    Assertions.assertTrue(equalsValidationStrategy.validate(first, first, legalDiscrepancy));
    Assertions.assertTrue(equalsValidationStrategy.validate(second, second, legalDiscrepancy));
    Assertions.assertFalse(equalsValidationStrategy.validate(first, second, legalDiscrepancy));
    Assertions.assertFalse(equalsValidationStrategy.validate(second, first, legalDiscrepancy));
  }


  @Test
  public void testNotEqualsValidationStrategy() {
    String first = "first";
    String second = "second";
    Assertions.assertFalse(notEqualsValidationStrategy.validate(first, first));
    Assertions.assertFalse(notEqualsValidationStrategy.validate(second, second));
    Assertions.assertTrue(notEqualsValidationStrategy.validate(first, second));
    Assertions.assertTrue(notEqualsValidationStrategy.validate(second, first));

    Assertions.assertFalse(notEqualsValidationStrategy.validate(first, first, legalDiscrepancy));
    Assertions.assertFalse(notEqualsValidationStrategy.validate(second, second, legalDiscrepancy));
    Assertions.assertTrue(notEqualsValidationStrategy.validate(first, second, legalDiscrepancy));
    Assertions.assertTrue(notEqualsValidationStrategy.validate(second, first, legalDiscrepancy));
  }

}