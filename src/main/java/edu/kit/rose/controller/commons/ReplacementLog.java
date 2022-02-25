package edu.kit.rose.controller.commons;

import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
   *
   * @param oldElement the element that was replaced by {@code newElement}, may not be {@code null}.
   * @param newElement the element that replaces {@code oldElement}, may not be {@code null}.
   * @throws IllegalArgumentException if the old and new element are not instances of the same
   *     class.
   */
  public <T extends Element> void replaceElement(T oldElement, T newElement) {
    Objects.requireNonNull(oldElement, "old element may not be null");
    Objects.requireNonNull(newElement, "new element may not be null");
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
   * Finds the current versions of the given elements, considering all known replacements.
   */
  public <T extends Element> Set<T> getCurrentVersions(Set<T> elements) {
    Objects.requireNonNull(elements);

    return elements.stream().map(this::getCurrentVersion).collect(Collectors.toSet());
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
   * Finds the current version of the given connector, considering all known replacement.
   */
  public <T extends Connector> T getCurrentConnectorVersion(T connector) {
    Segment oldContainer = findSegmentWithConnector(connector);
    if (oldContainer == null) {
      return connector;
    } else {
      Segment newContainer = getCurrentVersion(oldContainer);
      return findMatchingConnector(newContainer, connector);
    }
  }

  private Segment findSegmentWithConnector(Connector connector) {
    return this.replacements.keySet().stream()
        .filter(element -> !element.isContainer())
        .map(element -> (Segment) element)
        .filter(segment -> segment.getConnectors().contains(connector))
        .findAny()
        .orElse(null);
  }

  /**
   * Finds the connector in the given {@code newSegment} that is the equivalent of the given
   * {@code connector} in some other segment.
   *
   * @param newSegment the segment to find a connector in.
   * @param connector the connector whose equivalent we are looking for.
   * @param <T> the Java type of the current connector.
   * @return the matching connector.
   * @throws java.util.NoSuchElementException if there is no matching connector in the {@code
   * newSegment}.
   */
  @SuppressWarnings("unchecked")
  private <T extends Connector> T findMatchingConnector(Segment newSegment, T connector) {
    return (T) newSegment.getConnectors().stream()
        .filter(newConnector -> newConnector.getType() == connector.getType())
        .findAny()
        .orElseThrow();
  }

  /**
   * Finds the accessor in a given element that matches the type of the given accessor.
   *
   * @throws java.util.NoSuchElementException if the given element does not contain a matching
   *     accessor.
   */
  @SuppressWarnings("unchecked")
  private <T> AttributeAccessor<T> findMatchingAccessor(Element element,
                                                        AttributeAccessor<T> oldAccessor) {
    return (AttributeAccessor<T>) element.getAttributeAccessors().stream()
        .filter(newAccessor -> newAccessor.getAttributeType() == oldAccessor.getAttributeType())
        .findAny()
        .orElseThrow();
  }
}
