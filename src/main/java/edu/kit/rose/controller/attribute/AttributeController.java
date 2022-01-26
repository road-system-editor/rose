package edu.kit.rose.controller.attribute;

import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;


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
   * Removes an attribute type from the list of attribute types,
   * that are displayed on street segments.
   *
   * @param attributeType the attribute type to remove
   */
  void removeShownAttributeType(AttributeType attributeType);

  /**
   * Returns a {@link SortedBox} of all shared {@link AttributeAccessor}s of the selected
   * {@link edu.kit.rose.model.roadsystem.elements.Segment}s
   * The AttributeAccessors will forward getters (if there are multiple different values for an
   * attribute, null is returned) and setters to the underlying accessors and
   * observer notifications will be forwarded from the underlying accessors.
   *
   * @return A {@link SortedBox} containing all shared {@link AttributeAccessor}s of the
   *        {@link edu.kit.rose.model.roadsystem.elements.Segment}s.
   */
  SortedBox<AttributeAccessor<?>> getBulkEditAccessors();
}
