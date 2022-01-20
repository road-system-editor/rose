package edu.kit.rose.model.roadsystem.attributes;

import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;

/**
 * An Accessor for an Attribute that has an {@link AttributeType}.
 * Allows access to an attribute of an object without having to consult the object itself.
 * This makes it possible to grant dynamic access to Attributes of an object.
 *
 * @param <T> the Class of the Attribute that this Accessor is used for.
 */
public class AttributeAccessor<T> implements UnitObservable<AttributeAccessor<T>> {

  private AttributeType attributeType;

  /**
   * Provides the {@link AttributeType} of the underlying attribute.
   *
   * @return the {@link AttributeType} of the underlying attribute.
   */
  public AttributeType getAttributeType() {
    return null;
  }

  /**
   * Provides the name of the underlying attribute.
   *
   * @return the name of the underlying attribute.
   */
  String getName() {
    return attributeType.getName();
  }

  /**
   * Provides the value of the Attribute.
   *
   * @return T the value of the Attribute.
   */
  public T getValue() {
    return null;
  }

  /**
   * Sets the value of the Attribute to the given value.
   *
   * @param value the value to set the Attribute to.
   */
  public void setValue(T value) {

  }

  @Override
  public void removeSubscriber(UnitObserver<AttributeAccessor<T>> observer) {

  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public void addSubscriber(UnitObserver<AttributeAccessor<T>> observer) {

  }
}
