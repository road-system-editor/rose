package edu.kit.rose.model.plausibility.criteria.validation;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Validation Strategies.
 */
public class ValidationTest {
  private static ValidationStrategy<Boolean> norValidationStrategy;
  private static ValidationStrategy<Boolean> orValidationStrategy;
  private static ValidationStrategy<String> notEqualsValidationStrategy;
  private static ValidationStrategy<String> equalsValidationStrategy;

  /**
   * Initialize.
   */
  @BeforeEach
  public static void initialize() {
    norValidationStrategy = new NorValidationStrategy<>();
    orValidationStrategy = new OrValidationStrategy<>();
    notEqualsValidationStrategy = new NotEqualsValidationStrategy<>();
    equalsValidationStrategy = new EqualsValidationStrategy<>();
  }

  @Test
  public void testNorValidationStrategy() {
    Assertions.assertFalse(norValidationStrategy.validate(TRUE, TRUE));
    Assertions.assertFalse(norValidationStrategy.validate(FALSE, TRUE));
    Assertions.assertFalse(norValidationStrategy.validate(TRUE, FALSE));
    Assertions.assertTrue(norValidationStrategy.validate(FALSE, FALSE));
  }

  @Test
  public void testOrValidationStrategy() {
    Assertions.assertTrue(orValidationStrategy.validate(TRUE, TRUE));
    Assertions.assertTrue(orValidationStrategy.validate(FALSE, TRUE));
    Assertions.assertTrue(orValidationStrategy.validate(TRUE, FALSE));
    Assertions.assertFalse(orValidationStrategy.validate(FALSE, FALSE));
  }

  @Test
  public void testEqualsValidationStrategy() {
    String first = "first";
    String second = "second";
    Assertions.assertTrue(equalsValidationStrategy.validate(first, first));
    Assertions.assertTrue(equalsValidationStrategy.validate(second, second));
    Assertions.assertFalse(equalsValidationStrategy.validate(first, second));
    Assertions.assertFalse(equalsValidationStrategy.validate(second, first));
  }
}
