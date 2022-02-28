package edu.kit.rose.model.roadsystem.attributes;

import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AttributeAccessor}.
 */
public class AttributeAccessorTest {
  private static final AttributeType TYPE = AttributeType.COMMENT;

  private int targetValue;
  private final Consumer<Integer> setter = newValue -> targetValue = newValue;
  private final Supplier<Integer> getter = () -> targetValue;

  private AttributeAccessor<Integer> accessor;

  @BeforeEach
  public void beforeEach() {
    targetValue = 0;
    accessor = new AttributeAccessor<>(TYPE, getter, setter);
  }

  /**
   * Tests whether the constructor throws {@link NullPointerException}s if any of the given
   * values is {@code null}.
   */
  @Test
  public void testConstructor() {
    Assertions.assertThrows(NullPointerException.class,
        () -> new AttributeAccessor<>(null, getter, setter));

    Assertions.assertThrows(NullPointerException.class,
        () -> new AttributeAccessor<>(TYPE, null, setter));

    Assertions.assertThrows(NullPointerException.class,
        () -> new AttributeAccessor<>(TYPE, getter, null));
  }

  /**
   * Tests whether the attribute accessor returns the correct attribute type and name.
   */
  @Test
  public void testAttributeType() {
    Assertions.assertEquals(TYPE, accessor.getAttributeType());
  }

  /**
   * Tests whether the getter gets the correct value.
   */
  @Test
  public void testGetter() {
    targetValue = 1;
    Assertions.assertEquals(targetValue, accessor.getValue());

    targetValue = -1;
    Assertions.assertEquals(targetValue, accessor.getValue());
  }

  /**
   * Tests whether the setter sets the correct value.
   */
  @Test
  public void testSetter() {
    accessor.setValue(1);
    Assertions.assertEquals(1, targetValue);

    accessor.setValue(-1);
    Assertions.assertEquals(-1, targetValue);
  }

  @Test
  public void testGetThis() {
    Assertions.assertSame(accessor, accessor.getThis());
  }
}
