package edu.kit.rose.model.plausibility.criteria.validation;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Validation Strategies.
 */
public class BooleanValidationTest {

  private static final int legalDiscrepancy = 10;
  private static ValidationStrategy<Boolean> norValidationStrategy;
  private static ValidationStrategy<Boolean> orValidationStrategy;

  /**
   * Initialize.
   */
  @BeforeEach
  public void initialize() {
    norValidationStrategy = new NorValidationStrategy();
    orValidationStrategy = new OrValidationStrategy();
  }

  @Test
  public void testNorValidationStrategy() {
    Assertions.assertFalse(norValidationStrategy.validate(TRUE, TRUE));
    Assertions.assertFalse(norValidationStrategy.validate(FALSE, TRUE));
    Assertions.assertFalse(norValidationStrategy.validate(TRUE, FALSE));
    Assertions.assertTrue(norValidationStrategy.validate(FALSE, FALSE));

    Assertions.assertFalse(norValidationStrategy.validate(TRUE, TRUE, legalDiscrepancy));
    Assertions.assertFalse(norValidationStrategy.validate(FALSE, TRUE, legalDiscrepancy));
    Assertions.assertFalse(norValidationStrategy.validate(TRUE, FALSE, legalDiscrepancy));
    Assertions.assertTrue(norValidationStrategy.validate(FALSE, FALSE, legalDiscrepancy));
  }

  @Test
  public void testOrValidationStrategy() {
    Assertions.assertTrue(orValidationStrategy.validate(TRUE, TRUE));
    Assertions.assertTrue(orValidationStrategy.validate(FALSE, TRUE));
    Assertions.assertTrue(orValidationStrategy.validate(TRUE, FALSE));
    Assertions.assertFalse(orValidationStrategy.validate(FALSE, FALSE));

    Assertions.assertTrue(orValidationStrategy.validate(TRUE, TRUE, legalDiscrepancy));
    Assertions.assertTrue(orValidationStrategy.validate(FALSE, TRUE, legalDiscrepancy));
    Assertions.assertTrue(orValidationStrategy.validate(TRUE, FALSE, legalDiscrepancy));
    Assertions.assertFalse(orValidationStrategy.validate(FALSE, FALSE, legalDiscrepancy));
  }
}
