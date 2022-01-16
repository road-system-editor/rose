package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes a LessThan Operator for numerical values. "<"
 *
 * @param <T>
 */
class LessThan_ValidationStrategy<T> extends ValidationStrategy<T> {
  @Override
  boolean validate(Object first, Object second) {
    return false;
  }

  @Override
  boolean validate(T first, T second, double legalDiscrepancy) {
    return false;
  }
}
