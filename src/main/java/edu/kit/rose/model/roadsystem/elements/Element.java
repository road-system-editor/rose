package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.SetObservable;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;

/**
 * Represents an Element (see Pflichtenheft: "Element").
 * Holds a set of {@link AttributeAccessor}s.
 * Implements the Composite in the Composition Pattern with {@link Segment}s as leaf
 * and {@link Group} as container.
 */
public interface Element extends SetObservable<Element, Element> {

  /**
   * Returns {@link AttributeAccessor}s that thus allow for access to attributes of this element.
   *
   * @return the {@link AttributeAccessor}s for attributes of this Element.
   */
  SortedBox<AttributeAccessor<?>> getAttributeAccessors();

  /**
   * Provides the name of this Element.
   *
   * @return the name of this Element.
   */
  String getName();

  /**
   * Sets the name of this Element to the given {@code name}.
   */
  void setName(String name);

  /**
   * Returns the user comment on this Element.
   */
  String getComment();

  /**
   * Sets the user comment of this Element to the given {@code comment}.
   */
  void setComment(String comment);

  /**
   * Gives a boolean describing if this is a {@link Group} (contains other Elements) or a
   * {@link Segment}.
   *
   * @return True if this Element can contain other Elements. False if it can not.
   */
  boolean isContainer();
}
