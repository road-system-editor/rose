package edu.kit.rose.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

  /**
   * Tests if an {@code element} has an {@link AttributeAccessor} for the given {@code attribute}
   * type and if that accessors getter and setter work similar to the given {@code getter} and
   * {@code setter} by setting and resetting the attribute with the given {@code testValue}.
   *
   * @param <T> the java type of the attribute to test.
   */
  public static <T> void testAccessorCorrectness(Element element, AttributeType attribute,
                                                 Supplier<T> getter, Consumer<T> setter,
                                                 T testValue) {
    AttributeAccessor<T> accessor = findAccessorOfType(element, attribute);
    assertNotNull(accessor);

    SetObserver<Element, Element> mockObserver = createMockObserver();
    element.addSubscriber(mockObserver);

    // default: attribute is not configured
    assertNull(getter.get());
    assertNull(accessor.getValue());
    verifyNoInteractions(mockObserver);

    // set using normal setter
    setter.accept(testValue);
    assertSame(testValue, getter.get());
    assertSame(testValue, accessor.getValue());
    verify(mockObserver, times(1)).notifyChange(element);

    // reset using normal setter
    setter.accept(null);
    assertNull(getter.get());
    assertNull(accessor.getValue());
    verify(mockObserver, times(2)).notifyChange(element);

    // set using accessor
    accessor.setValue(testValue);
    assertSame(testValue, getter.get());
    assertSame(testValue, accessor.getValue());
    verify(mockObserver, times(3)).notifyChange(element);

    // reset using accessor
    accessor.setValue(null);
    assertNull(getter.get());
    assertNull(accessor.getValue());
    verify(mockObserver, times(4)).notifyChange(element);
  }

  @SuppressWarnings("unchecked") // this is how mocking generics in
  private static SetObserver<Element, Element> createMockObserver() {
    return mock(SetObserver.class);
  }
}
