package edu.kit.rose.model.plausibility.criteria.validation;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import edu.kit.rose.model.roadsystem.DataType;
import java.util.Collection;
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
  private static ValidationStrategy<String> notEqualsValidationStrategy;
  private static ValidationStrategy<String> equalsValidationStrategy;

  /**
   * Initialize.
   */
  @BeforeEach
  public void initialize() {
    norValidationStrategy = new NorValidationStrategy();
    orValidationStrategy = new OrValidationStrategy();
    notEqualsValidationStrategy = new NotEqualsValidationStrategy<>();
    equalsValidationStrategy = new EqualsValidationStrategy<>();
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
