package edu.kit.rose.model.roadsystem.attributes;

import edu.kit.rose.model.roadsystem.DataType;
import edu.kit.rose.model.roadsystem.elements.Element;

/**
 * Different Types of Attributes. Used to determine the UI needed to configure an attribute.
 * Provides information about whether an attribute should be bulkable, that is if it should be
 * set to the same value as other attributes of the
 * same type.
 */
public enum AttributeType {
  /**
   * Every {@link Element} has a name as an attribute.
   */
  NAME(DataType.STRING, false),
  /**
   * The physical length of a road segment.
   */
  LENGTH(DataType.INTEGER, true),
  /**
   * The amount of lanes of a road segment.
   */
  LANE_COUNT(DataType.INTEGER, true),
  /**
   * The amount of lanes on the ramp of a highway entrance or exit segment.
   */
  LANE_COUNT_RAMP(DataType.INTEGER, true),
  /**
   * The longitudinal slope of a road, as a percentage.
   */
  SLOPE(DataType.FRACTIONAL, true),
  /**
   * Describes whether a road segment is in an urban area.
   */
  CONURBATION(DataType.BOOLEAN, true),
  /**
   * The speed limit on a road segment, in kilometers per hour.
   */
  MAX_SPEED(DataType.SPEED_LIMIT, true),
  /**
   * The speed limit on the ramp of a highway entrance or exit segment, in kilometers per hour.
   */
  MAX_SPEED_RAMP(DataType.SPEED_LIMIT, true),
  /**
   * A note for an element by the user.
   */
  COMMENT(DataType.STRING, false);

  private final DataType dataType;
  private final boolean isBulkable;

  /**
   * Constructor.
   *
   * @param dataType   The {@link DataType} of the Attribute Type.
   * @param isBulkable A boolean describing if the AttributeType can
   *                   be accessed for multiple {@link Element}s at once.
   */
  AttributeType(DataType dataType, boolean isBulkable) {
    this.dataType = dataType;
    this.isBulkable = isBulkable;
  }

  /**
   * Provides the {@link DataType} of the AttributeType.
   *
   * @return the {@link DataType} of the AttributeType.
   */
  public DataType getDataType() {
    return this.dataType;
  }

  /**
   * Provides a boolean describing if the AttributeType can be accessed
   * for multiple {@link Element}s at once.
   *
   * @return a boolean describing if the AttributeType can be accessed for multiple
   *        {@link Element}s at once.
   */
  public boolean isBulkable() {
    return this.isBulkable;
  }
}
