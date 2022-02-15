package edu.kit.rose.model.plausibility.criteria.validation;

import edu.kit.rose.model.roadsystem.DataType;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the ValidationStrategy Class.
 */
public class ValidationTypeTest {

  private static ValidationStrategy<Boolean> norValidationStrategy;
  private static ValidationStrategy<Boolean> orValidationStrategy;
  private static ValidationStrategy<String> notEqualsValidationStrategy;
  private static ValidationStrategy<String> equalsValidationStrategy;
  private ValidationStrategy<Integer> integerLessThanValidationStrategy;
  private ValidationStrategy<Double> doubleLessThanValidationStrategy;

  /**
   * Initialize.
   */
  @BeforeEach
  public void initialize() {
    norValidationStrategy = new NorValidationStrategy();
    orValidationStrategy = new OrValidationStrategy();
    notEqualsValidationStrategy = new NotEqualsValidationStrategy<>();
    equalsValidationStrategy = new EqualsValidationStrategy<>();
    integerLessThanValidationStrategy = new LessThanValidationStrategy<>();
    doubleLessThanValidationStrategy = new LessThanValidationStrategy<>();
  }


  @Test
  public void testValueCriterionNor() {
    Collection<DataType> compatible =
        norValidationStrategy.getValidationType().getCompatible();
    Assertions.assertEquals(1, compatible.size());
    Assertions.assertTrue(compatible.contains(DataType.BOOLEAN));
  }

  @Test
  public void testValueCriterionOr() {
    Collection<DataType> compatible =
        orValidationStrategy.getValidationType().getCompatible();
    Assertions.assertEquals(1, compatible.size());
    Assertions.assertTrue(compatible.contains(DataType.BOOLEAN));
  }

  @Test
  public void testValueCriterionEquals() {
    Collection<DataType> compatible =
        equalsValidationStrategy.getValidationType().getCompatible();
    Assertions.assertEquals(5, compatible.size());
    Assertions.assertTrue(compatible.contains(DataType.BOOLEAN));
    Assertions.assertTrue(compatible.contains(DataType.STRING));
    Assertions.assertTrue(compatible.contains(DataType.INTEGER));
    Assertions.assertTrue(compatible.contains(DataType.FRACTIONAL));
    Assertions.assertTrue(compatible.contains(DataType.SPEED_LIMIT));
  }

  @Test
  public void testValueCriterionNotEquals() {
    Collection<DataType> compatible =
        notEqualsValidationStrategy.getValidationType().getCompatible();
    Assertions.assertEquals(5, compatible.size());
    Assertions.assertTrue(compatible.contains(DataType.BOOLEAN));
    Assertions.assertTrue(compatible.contains(DataType.STRING));
    Assertions.assertTrue(compatible.contains(DataType.INTEGER));
    Assertions.assertTrue(compatible.contains(DataType.FRACTIONAL));
    Assertions.assertTrue(compatible.contains(DataType.SPEED_LIMIT));
  }

  @Test
  public void testValueCriterionLessThan() {
    Collection<DataType> compatible =
        integerLessThanValidationStrategy.getValidationType().getCompatible();
    Assertions.assertTrue(compatible.contains(DataType.INTEGER));
    Assertions.assertTrue(compatible.contains(DataType.FRACTIONAL));

    compatible =
        doubleLessThanValidationStrategy.getValidationType().getCompatible();
    Assertions.assertTrue(compatible.contains(DataType.INTEGER));
    Assertions.assertTrue(compatible.contains(DataType.FRACTIONAL));
  }

  @Test
  public void testHasDiscrepancy() {
    Assertions.assertTrue(integerLessThanValidationStrategy.validationType.hasDiscrepancy());
    Assertions.assertTrue(doubleLessThanValidationStrategy.validationType.hasDiscrepancy());
    Assertions.assertFalse(orValidationStrategy.validationType.hasDiscrepancy());
    Assertions.assertFalse(norValidationStrategy.validationType.hasDiscrepancy());
    Assertions.assertFalse(equalsValidationStrategy.validationType.hasDiscrepancy());
    Assertions.assertFalse(notEqualsValidationStrategy.validationType.hasDiscrepancy());
  }
}
