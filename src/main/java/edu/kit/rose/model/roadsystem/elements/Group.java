package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.RoseUnitObservable;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A container tha holds multiple {@link Element}s.
 * Might hold other groups.
 * (see: Pflichtenheft: "Gruppe")
 */
public class Group extends RoseUnitObservable<Element> implements Element, Iterable<Element> {

  private final List<Element> elements = new ArrayList<>();
  private String name;
  private String comment;

  @Override
  public Iterator<Element> iterator() {
    return elements.iterator();
  }

  /**
   * Adds a given {@link Element} to the Group.
   *
   * @param element The {@link Element} that shall be added to the Group.
   */
  public void addElement(Element element) {
    if (element != null && !elements.contains(element)) {
      elements.add(element);
    }
  }

  /**
   * Removes a given {@link Element} from the Group.
   *
   * @param element The {@link Element} that shall be removed from the Group.
   */
  public void removeElement(Element element) {
    elements.remove(element);
  }

  /**
   * Returns a {@link Box} of all {@link Element}s in the Group.
   *
   * @return a {@link Box} of all {@link Element}s in the Group.
   */
  public SortedBox<Element> getElements() {
    return new RoseSortedBox<>(elements);
  }

  /**
   * Returns a boolean describing if a given {@link Element} is in this Group.
   *
   * @param element The {@link Element} to look for.
   * @return True if the given element is in the Group.
   */
  public boolean contains(Element element) {
    return elements.contains(element);
  }

  @Override
  public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
    return new RoseSortedBox<>(
        List.of(
            new AttributeAccessor<>(
                AttributeType.NAME,
                () -> this.name,
                name -> this.name = name),
            new AttributeAccessor<>(
                AttributeType.COMMENT,
                () -> this.comment,
                comment -> this.comment = comment)
        ));
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isContainer() {
    return true;
  }

  @Override
  public Element getThis() {
    return this;
  }
}
