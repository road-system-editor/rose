package edu.kit.rose.util;

import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;

/**
 * Utility methods that assist with verifying the correctness of {@link AttributeAccessor}s.
 */
public final class AccessorUtility {
  /**
   * Finds the attribute accessor for a given attribute {@code type} in a given {@code element}.
   *
   * @return the attribute accessor of the given type or {@code null} if it does not exist.
   * @throws ClassCastException if the attribute accessor does not have the matching java type.
   */
  @SuppressWarnings("unchecked")
  public static <T> AttributeAccessor<T> findAccessorOfType(Element element,
                                                            AttributeType type) {
    for (var accessor : element.getAttributeAccessors()) {
      if (accessor.getAttributeType() == type) {
        return (AttributeAccessor<T>) accessor;
      }
    }
    return null;
  }
}
