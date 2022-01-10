package edu.kit.rose.model.roadsystem.attributes;

import edu.kit.rose.model.roadsystem.DataType;
import edu.kit.rose.model.roadsystem.elements.Element;

/**
 * Different Types of Attributes. Used to determine the UI needed to configure an attribute. Provides information about
 * whether an attribute should be bulkable, that is if it should be set to the same value as other attributes of the
 * same type.
 */
public enum AttributeType {
    NAME("name", DataType.STRING, false),
    LENGTH("length",DataType.INTEGER, true),
    LANE_COUNT("lane count",DataType.INTEGER, true),
    LANE_COUNT_RAMP("lane count ramp", DataType.INTEGER, true),
    SLOPE("longitudinal slope",DataType.FRACTIONAL, true),
    CONURBATION("conurbation",DataType.BOOLEAN, true),
    MAX_SPEED("max speed", DataType.INTEGER, true),
    MAX_SPEED_RAMP("max speed ramp", DataType.INTEGER, true),
    COMMENT("comment", DataType.STRING, false);

    private final String name;
    private final DataType dataType;
    private final boolean isBulkable;

    /**
     * Constructor
     * @param name The name of the AttributeType.
     * @param dataType The {@link DataType} of the Attribute Type.
     * @param isBulkable A boolean describing if the AttributeType can
     *                   be accessed for multiple {@link Element}s at once.
     */
    AttributeType(String name, DataType dataType, boolean isBulkable) {
        this.name = name;
        this.dataType = dataType;
        this.isBulkable = isBulkable;
    }

    /**
     *
     * @return the {@link DataType} of the AttributeType
     */
    DataType getDataType() {
        return this.dataType;
    }

    /**
     *
     * @return a String holding the name of the AttributeType
     */
    String getName() {
        return this.name;
    }

    /**
     *
     * @return a boolean describing if the AttributeType can be accessed
     *         for multiple {@link Element}s at once.
     */
    boolean isBulkable() {
        return this.isBulkable;
    }

}
