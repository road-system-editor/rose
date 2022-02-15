package edu.kit.rose.model.roadsystem.attributes;

import edu.kit.rose.infrastructure.RoseUnitObservable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An Accessor for an Attribute that has an {@link AttributeType}.
 * Allows access to an attribute of an object without having to consult the object itself.
 * This makes it possible to grant dynamic access to Attributes of an object.
 *
 * @param <T> the Class of the Attribute that this Accessor is used for.
 */
public class AttributeAccessor<T> extends RoseUnitObservable<AttributeAccessor<T>> {
  private final AttributeType type;
  private final Supplier<T> getter;
  private final Consumer<T> setter;

  /**
   * Creates a new attribute accessor for an attribute of the given type with the given bindings.
   *
   * @param type the type of attribute that this accessor refers to, may not be {@code null}.
   * @param getter a getter for the value of the referenced attribute, may not be {@code null}.
   * @param setter a setter for the value of the referenced attribute, may not be {@code null}.
   */
  public AttributeAccessor(AttributeType type, Supplier<T> getter, Consumer<T> setter) {
    this.type = Objects.requireNonNull(type);
    this.getter = Objects.requireNonNull(getter);
    this.setter = Objects.requireNonNull(setter);
  }

  /**
   * Provides the {@link AttributeType} of the underlying attribute.
   *
   * @return the {@link AttributeType} of the underlying attribute.
   */
  public AttributeType getAttributeType() {
    return this.type;
  }

  /**
   * Provides the value of the Attribute.
   *
   * @return T the value of the Attribute.
   */
  public T getValue() {
    return getter.get();
  }

  /**
   * Sets the value of the Attribute to the given value.
   *
   * @param value the value to set the Attribute to.
   */
  public void setValue(T value) {
    setter.accept(value);
  }

  @Override
  public AttributeAccessor<T> getThis() {
    return this;
  }
}
