package edu.kit.rose.model.plausibility.criteria.validation;

/**
 * Describes an equals operator for different Objects. Uses the java.util.equals function
 * @param <T>
 */
class Equals_ValidationStrategy<T> extends ValidationStrategy<T> {
    @Override
    boolean validate(Object first, Object second) {
        return false;
    }

    @Override
    boolean validate(T first, T second, double legalDiscrepancy) {
        return false;
    }
}