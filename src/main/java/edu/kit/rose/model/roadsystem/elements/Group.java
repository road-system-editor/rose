package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.Iterator;
import java.util.Set;

/**
 * A container tha holds multiple {@link Element}s.
 * Might hold other groups.
 * (see: Pflichtenheft: "Gruppe")
 */
public class Group implements Element, Iterable<Element> {

  public Group(Set<Element> elements) {
    //TODO: implement
  }

  @Override
  public Iterator<Element> iterator() {
    return null;
  }

  /**
   * Adds a given {@link Element} to the Group.
   *
   * @param element The {@link Element} that shall be added to the Group.
   */
  public void addElement(Element element) {

  }

  /**
   * Removes a given {@link Element} from the Group.
   *
   * @param element The {@link Element} that shall be removed from the Group.
   */
  public void removeElement(Element element) {

  }

  /**
   * Returns a {@link Box} of all {@link Element}s in the Group.
   *
   * @return a {@link Box} of all {@link Element}s in the Group.
   */
  public SortedBox<Element> getElements() {
    return null;
  }

  /**
   * Returns a boolean describing if a given {@link Element} is in this Group.
   *
   * @param element The {@link Element} to look for.
   * @return True if the given element is in the Group.
   */
  public boolean contains(Element element) {
    return false;
  }

  @Override
  public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public boolean isContainer() {
    return false;
  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public Element getThis() {
    return this;
  }

  @Override
  public void addSubscriber(UnitObserver<Element> observer) {

  }

  @Override
  public void removeSubscriber(UnitObserver<Element> observer) {

  }
}
