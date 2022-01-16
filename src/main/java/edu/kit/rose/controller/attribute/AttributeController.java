package edu.kit.rose.controller.attribute;

import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;


/**
 * Provides methods for setting the values of attribute accessors.
 *
 * @author ROSE Team
 */
public interface AttributeController {

  /**
   * Sets the value of a given accessor to another given value.
   *
   * @param accessor accessor whose value gets set
   * @param value    value to apply to the accessor
   * @param <T>      generic type of the accessor parameter and the value parameter
   */
  <T> void setAttribute(AttributeAccessor<T> accessor, T value);

  /**
   * Adds an attribute type to the list of attribute types, that are displayed on street segments.
   *
   * @param attributeType the attribute type to add
   */
  void addShownAttributeType(AttributeType attributeType);

  /**
   * Removes an attribute type from the list of attribute types, that are displayed on street segments.
   *
   * @param attributeType the attribute type to remove
   */
  void removeShownAttributeType(AttributeType attributeType);
}
