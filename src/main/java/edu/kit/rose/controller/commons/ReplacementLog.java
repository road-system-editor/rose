package edu.kit.rose.controller.commons;

import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Element;
import java.util.HashMap;
import java.util.Map;

/**
 * The replacement log keeps track of new versions of {@link Element}s that are created through
 * undoing and redoing {@link edu.kit.rose.controller.command.ChangeCommand}s.
 */
public class ReplacementLog {
  private final Map<Element, Element> replacements;

  /**
   * Creates a new and empty replacement log.
   */
  public ReplacementLog() {
    this.replacements = new HashMap<>();
  }

  /**
   * Registers an element replacement.
   * The old and new element must be of the same class.
   */
  public <T extends Element> void replaceElement(T oldElement, T newElement) {
    if (oldElement.getClass() != newElement.getClass()) {
      throw new IllegalArgumentException("replacement must have elements of the same class!");
    }
    if (oldElement != newElement) {
      this.replacements.put(oldElement, newElement);
    }
  }

  /**
   * Finds the current version of the given element, considering all known replacements.
   */
  // the cast is allowed since all replacements have strictly matching types
  @SuppressWarnings("unchecked")
  public <T extends Element> T getCurrentVersion(T element) {
    var current = element;
    while (replacements.containsKey(current)) {
      current = (T) replacements.get(current);
    }
    return current;
  }

  /**
   * Finds the current version of the given attribute accessor, considering all known replacements.
   */
  public <T> AttributeAccessor<T> getCurrentAccessorVersion(AttributeAccessor<T> accessor) {
    Element oldContainer = findElementContaining(accessor);
    if (oldContainer == null) {
      return accessor;
    } else {
      Element newContainer = getCurrentVersion(oldContainer);
      return findMatchingAccessor(newContainer, accessor);
    }
  }

  private <T> Element findElementContaining(AttributeAccessor<T> accessor) {
    return this.replacements.keySet().stream()
        .filter(element -> element.getAttributeAccessors().contains(accessor))
        .findAny()
        .orElse(null);
  }

  /**
   * Finds the accessor in a given element that matches the type of the given accessor.
   *
   * @throws RuntimeException if the given element does not contain a matching accessor.
   */
  @SuppressWarnings("unchecked")
  private <T> AttributeAccessor<T> findMatchingAccessor(Element element,
                                                        AttributeAccessor<T> oldAccessor) {
    for (var newAccessor : element.getAttributeAccessors()) {
      if (newAccessor.getAttributeType() == oldAccessor.getAttributeType()) {
        return (AttributeAccessor<T>) newAccessor;
      }
    }

    throw new RuntimeException("target element does not contain a matching accessor");
  }
}
