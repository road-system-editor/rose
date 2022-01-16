package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes a NotEquals Operator for different Objects. It uses the java.util.equals function.
 *
 * @param <T>
 */
class NotEquals_ValidationStrategy<T> extends ValidationStrategy<T> {
  @Override
  boolean validate(Object first, Object second) {
    return false;
  }

  @Override
  boolean validate(T first, T second, double legalDiscrepancy) {
    return false;
  }
}
