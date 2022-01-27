package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests the {@link Group} class.
 */
public class GroupTest {

  private Element element;

  private Segment segment;

  @BeforeEach
  private void setUp() {
    element = Mockito.mock(Element.class);
    segment = Mockito.mock(Segment.class);
  }

  @Test()
  public void testAddElement() {
    Group g = new Group();

    g.addElement(element);
    Assertions.assertEquals(element, g.getElements().get(0));
    Assertions.assertEquals(1, g.getElements().getSize());

    g.addElement(element);
    Assertions.assertEquals(element, g.getElements().get(0));
    Assertions.assertEquals(1, g.getElements().getSize());

    Assertions.assertThrows(IllegalArgumentException.class, () -> g.addElement(null));
  }

  @Test
  public void testRemoveElement() {
    Group group = new Group();

    group.addElement(element);
    Assertions.assertEquals(element, group.getElements().get(0));
    Assertions.assertEquals(1, group.getElements().getSize());

    group.removeElement(element);
    Assertions.assertEquals(0, group.getElements().getSize());

    group.removeElement(element);
    Assertions.assertEquals(0, group.getElements().getSize());
  }

  /* TODO: fix
  @Test
  public void testGetElements() {
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
   */

  @Test
  public void testContains() {
    Group group = new Group();
    Group groupToAdd = new Group();

    group.addElement(element);
    group.addElement(segment);
    group.addElement(groupToAdd);

    Assertions.assertTrue(group.contains(element));
    Assertions.assertTrue(group.contains(segment));
    Assertions.assertTrue(group.contains(groupToAdd));
  }

  @Test
  public void testIsContainer() {
    Group group = new Group();

    Assertions.assertTrue(group.isContainer());
  }

  @Test
  public void testGetThis() {
    Group group = new Group();

    Assertions.assertSame(group, group.getThis());
  }

  @Test
  public void testGetAttributeAccessors() {
    Group group = new Group();

    SortedBox<AttributeAccessor<?>> attributeAccessors = group.getAttributeAccessors();

    Assertions.assertTrue(hasAccessorOfAttributeType(attributeAccessors, AttributeType.NAME));
    Assertions.assertTrue(hasAccessorOfAttributeType(attributeAccessors, AttributeType.COMMENT));
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
}
