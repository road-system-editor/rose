package edu.kit.rose.model.plausibility.criteria.validation;

import edu.kit.rose.model.roadsystem.DataType;
import java.util.Collection;

/**
 * An enum that describes different kinds of {@link ValidationStrategy}s and the {@link DataType}s
 * they are compatible with.
 */
public enum OperatorType {

  DEFAULT(false, DataType.INTEGER, DataType.FRACTIONAL, DataType.STRING, DataType.BOOLEAN),
  LESS_THAN(true, DataType.INTEGER, DataType.FRACTIONAL),
  EQUALS(false, DataType.INTEGER, DataType.FRACTIONAL, DataType.BOOLEAN, DataType.BOOLEAN),
  NOT_EQUALS(false, DataType.INTEGER, DataType.FRACTIONAL, DataType.BOOLEAN, DataType.BOOLEAN),
  OR(false, DataType.BOOLEAN),
  NOR(false, DataType.BOOLEAN);

  boolean hasDiscrepancy;
  DataType[] compatibleDataTypes;

  OperatorType(boolean hasDiscrepancy, DataType... types) {
  }

  /**
   * Provides all {@link DataType}s that are compatible with this OperatorType.
   *
   * @return All {@link DataType}s that are compatible with this OperatorType.
   */
  Collection<DataType> getCompatible() {
    return null;
  }

  /**
   * Provides a boolean describing if the OperatorType can be used with a discrepancy.
   *
   * @return true if the OperatorType can be used with a discrepancy.
   */
  public boolean hasDiscrepancy() {
    return hasDiscrepancy;
  }
}
