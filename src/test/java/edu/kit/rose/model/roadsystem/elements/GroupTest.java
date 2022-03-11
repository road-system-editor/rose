package edu.kit.rose.model.roadsystem.elements;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.util.Iterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Group} class.
 */
class GroupTest {
  private Element element;
  private Segment segment;

  @BeforeEach
  void setUp() {
    element = mock(Element.class);
    segment = mock(Segment.class);
  }

  @Test()
  void testAddElement() {
    Group g = new Group();

    g.addElement(element);
    Assertions.assertSame(element, g.getElements().get(0));
    Assertions.assertEquals(1, g.getElements().getSize());

    g.addElement(element);
    Assertions.assertSame(element, g.getElements().get(0));
    Assertions.assertEquals(1, g.getElements().getSize());

    Assertions.assertThrows(IllegalArgumentException.class, () -> g.addElement(null));
  }

  @Test
  void testRemoveElement() {
    Group group = new Group();

    group.addElement(element);
    Assertions.assertEquals(element, group.getElements().get(0));
    Assertions.assertEquals(1, group.getElements().getSize());

    group.removeElement(element);
    Assertions.assertEquals(0, group.getElements().getSize());

    group.removeElement(element);
    Assertions.assertEquals(0, group.getElements().getSize());
  }

  @Test
  void testGetElements() {
    Group group = new Group();
    Group groupToAdd = new Group();

    group.addElement(element);
    group.addElement(segment);
    group.addElement(groupToAdd);

    Assertions.assertEquals(3, group.getElements().getSize());
    Assertions.assertEquals(element, group.getElements().get(0));
    Assertions.assertEquals(segment, group.getElements().get(1));
    Assertions.assertEquals(groupToAdd, group.getElements().get(2));
  }

  @Test
  void testContains() {
    Group group = new Group();
    Group groupToAdd = new Group();

    group.addElement(element);
    group.addElement(segment);
    group.addElement(groupToAdd);

    assertTrue(group.contains(element));
    assertTrue(group.contains(segment));
    assertTrue(group.contains(groupToAdd));
  }

  @Test
  void testIsContainer() {
    Group group = new Group();

    assertTrue(group.isContainer());
  }

  @Test
  void testGetThis() {
    Group group = new Group();

    Assertions.assertSame(group, group.getThis());
  }

  @Test
  void testGetAttributeAccessors() {
    Group group = new Group();

    SortedBox<AttributeAccessor<?>> attributeAccessors = group.getAttributeAccessors();

    assertTrue(hasAccessorOfAttributeType(attributeAccessors, AttributeType.NAME));
    assertTrue(hasAccessorOfAttributeType(attributeAccessors, AttributeType.COMMENT));
  }

  private boolean hasAccessorOfAttributeType(
      Iterable<AttributeAccessor<?>> accessors, AttributeType attributeType) {
    for (AttributeAccessor<?> accessor : accessors) {
      if (accessor.getAttributeType() == attributeType) {
        return true;
      }
    }

    return false;
  }

  @Test
  void testIterator() {
    var group = new Group();
    group.addElement(segment);
    group.addElement(element);

    Iterator<Element> iterator = group.iterator();
    assertSame(segment, iterator.next());
    assertThrows(UnsupportedOperationException.class, iterator::remove);
    assertSame(element, iterator.next());
    assertThrows(UnsupportedOperationException.class, iterator::remove);
    assertFalse(iterator.hasNext());
  }

  @Test
  void testNotification() {
    var group = new Group();
    var subscriber = mockSubscriber();
    group.addSubscriber(subscriber);

    group.addElement(segment);
    verify(subscriber, times(1)).notifyAddition(segment);

    group.removeElement(segment);
    verify(subscriber, times(1)).notifyRemoval(segment);

    group.setName("descriptive name");
    verify(subscriber, times(1)).notifyChange(group);

    group.setComment("insightful comment");
    verify(subscriber, times(2)).notifyChange(group);
  }

  @SuppressWarnings("unchecked")
  private SetObserver<Element, Element> mockSubscriber() {
    return mock(SetObserver.class);
  }
}
