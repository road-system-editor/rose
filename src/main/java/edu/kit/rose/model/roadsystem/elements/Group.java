package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A container tha holds multiple {@link Element}s.
 * Might hold other groups.
 * (see: Pflichtenheft: "Gruppe")
 */
public class Group
    extends RoseSetObservable<Element, Element>
    implements Element, Iterable<Element> {

  private final List<Element> elements;
  private final SortedBox<AttributeAccessor<?>> accessors;
  private String name;
  private final AttributeAccessor<String> nameAccessor;
  private String comment;
  private final AttributeAccessor<String> commentAccessor;

  /**
   * Creates a new instance of the {@link Group} class.
   */
  public Group() {
    this(new LinkedList<>());
  }

  /**
   * Creates a new instance of the {@link Group} class.
   */
  public Group(List<Element> elements) {
    this.elements = elements;

    this.nameAccessor = new AttributeAccessor<>(AttributeType.NAME, this::getName, this::setName);
    this.commentAccessor = new AttributeAccessor<>(
        AttributeType.COMMENT, this::getComment, this::setComment);

    this.accessors = new RoseSortedBox<>(List.of(nameAccessor, commentAccessor));
  }

  @Override
  public Iterator<Element> iterator() {
    return getElements().iterator();
  }

  /**
   * Adds a given {@link Element} to the Group.
   *
   * @param element The {@link Element} that shall be added to the Group.
   */
  public void addElement(Element element) throws IllegalArgumentException {
    if (element == null) {
      throw new IllegalArgumentException(
          "The parameter element may not be null on Group.addElement");
    }

    if (!elements.contains(element)) {
      elements.add(element);

      subscribers.forEach(subscriber -> subscriber.notifyAddition(element));
    }
  }

  /**
   * Removes a given {@link Element} from the Group.
   *
   * @param element The {@link Element} that shall be removed from the Group.
   */
  public void removeElement(Element element) {
    if (elements.remove(element)) {
      subscribers.forEach(subscriber -> subscriber.notifyRemoval(element));
    }
  }

  /**
   * Returns a {@link Box} of all {@link Element}s in the Group.
   *
   * @return a {@link Box} of all {@link Element}s in the Group.
   */
  public SortedBox<Element> getElements() {
    return new RoseSortedBox<>(new ArrayList<>(elements));
  }

  /**
   * Returns a boolean describing if a given {@link Element} is in this Group.
   *
   * @param element The {@link Element} to look for.
   * @return True if the given element is in the Group.
   */
  public boolean contains(Element element) {
    return getElements().contains(element);
  }

  @Override
  public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
    return accessors;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;

    this.nameAccessor.notifySubscribers();
    this.notifySubscribers();
  }

  @Override
  public String getComment() {
    return this.comment;
  }

  @Override
  public void setComment(String comment) {
    this.comment = comment;

    this.commentAccessor.notifySubscribers();
    this.notifySubscribers();
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
