package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * A ValidationStrategy models a simple logical evaluation between two values of type T.
 * It can also hold a double that holds a discrepancy  for numerical Values.
 * Then it will subtract the given values and check, if the absolute difference is smaller than
 * the legalDiscrepancy.
 *
 * @param <T> The Type of value this Operator uses.
 */
public abstract class ValidationStrategy<T> {

  /**
   * Validates if the two given values are a legal combination in sense of the ValidationStrategy.
   *
   * @param first  The first value to check with the other.
   * @param second The second value to check with the other.
   * @return True if the given values are a legal combination in sense of the ValidationStrategy.
   */
  public abstract boolean validate(T first, T second);

  /**
   * Validates if the difference between the two given values are smaller than the given
   * legalDiscrepancy.
   * If this Validation Strategy does not support a Discrepancy, the third parameter is ignored.
   *
   * @param first            The first value to check with the other.
   * @param second           The second value to check with the other.
   * @param legalDiscrepancy if this Strategy supports a legalDiscrepancy it will use this one.
   * @return True if the given values are a legal combination in sense of the Strategy and the
   *        legalDiscrepancy.
   */
  public abstract boolean validate(T first, T second, double legalDiscrepancy);


}
