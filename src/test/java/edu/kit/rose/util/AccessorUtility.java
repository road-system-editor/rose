package edu.kit.rose.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

  /**
   * Asserts that two {@link Element}s have the same types of {@link AttributeAccessor}s and that
   * all of them have an equal value in their pendant.
   */
  public static void assertEqualAccessors(Element expected, Element actual) {
    assertEquals(expected.getAttributeAccessors().getSize(),
        actual.getAttributeAccessors().getSize());
    for (var accessor1 : expected.getAttributeAccessors()) {
      var accessor2 = findAccessorOfType(actual, accessor1.getAttributeType());
      assertNotNull(accessor2);
      assertEquals(accessor1.getValue(), accessor2.getValue());
    }
  }
}
