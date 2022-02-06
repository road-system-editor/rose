package edu.kit.rose.model.plausibility.criteria.validation;

import edu.kit.rose.model.roadsystem.DataType;
import java.util.Collection;
import java.util.List;

/**
 * An enum that describes different kinds of {@link ValidationStrategy}s and the {@link DataType}s
 * they are compatible with.
 */
public enum ValidationType {

  DEFAULT(false, DataType.INTEGER, DataType.FRACTIONAL, DataType.STRING, DataType.BOOLEAN),
  LESS_THAN(true, DataType.INTEGER, DataType.FRACTIONAL),
  EQUALS(false, DataType.INTEGER, DataType.FRACTIONAL, DataType.STRING, DataType.BOOLEAN),
  NOT_EQUALS(false, DataType.INTEGER, DataType.FRACTIONAL, DataType.STRING, DataType.BOOLEAN),
  OR(false, DataType.BOOLEAN),
  NOR(false, DataType.BOOLEAN);

  private final boolean hasDiscrepancy;
  private final DataType[] compatibleDataTypes;

  ValidationType(boolean hasDiscrepancy, DataType... types) {
    this.hasDiscrepancy = hasDiscrepancy;
    this.compatibleDataTypes = types;
  }

  /**
   * Provides all {@link DataType}s that are compatible with this ValidationType.
   *
   * @return All {@link DataType}s that are compatible with this ValidationType.
   */
  public Collection<DataType> getCompatible() {
    return List.of(this.compatibleDataTypes);
  }

  /**
   * Provides a boolean describing if the ValidationType can be used with a discrepancy.
   *
   * @return true if the ValidationType can be used with a discrepancy.
   */
  public boolean hasDiscrepancy() {
    return hasDiscrepancy;
  }
}
