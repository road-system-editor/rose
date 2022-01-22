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
  NAME("name", DataType.STRING, false),
  /**
   * The physical length of a road segment.
   */
  LENGTH("length", DataType.INTEGER, true),
  /**
   * The amount of lanes of a road segment.
   */
  LANE_COUNT("lane count", DataType.INTEGER, true),
  /**
   * The amount of lanes on the ramp of a highway entrance or exit segment.
   */
  LANE_COUNT_RAMP("lane count ramp", DataType.INTEGER, true),
  /**
   * The longitudinal slope of a road, as a percentage.
   */
  SLOPE("longitudinal slope", DataType.FRACTIONAL, true),
  /**
   * Describes whether a road segment is in an urban area.
   */
  CONURBATION("conurbation", DataType.BOOLEAN, true),
  /**
   * The speed limit on a road segment, in kilometers per hour.
   */
  MAX_SPEED("max speed", DataType.INTEGER, true),
  /**
   * The speed limit on the ramp of a highway entrance or exit segment, in kilometers per hour.
   */
  MAX_SPEED_RAMP("max speed ramp", DataType.INTEGER, true),
  /**
   * A note for an element by the user.
   */
  COMMENT("comment", DataType.STRING, false);

  private final String name;
  private final DataType dataType;
  private final boolean isBulkable;

  /**
   * Constructor.
   *
   * @param name       The name of the AttributeType.
   * @param dataType   The {@link DataType} of the Attribute Type.
   * @param isBulkable A boolean describing if the AttributeType can
   *                   be accessed for multiple {@link Element}s at once.
   */
  AttributeType(String name, DataType dataType, boolean isBulkable) {
    this.name = name;
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
   * Provides a String holding the name of the AttributeType.
   *
   * @return a String holding the name of the AttributeType.
   */
  public String getName() {
    return this.name;
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
