package edu.kit.rose.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * Unit Test for the RoseBox Class.
 */
public class RoseBoxTest {
  static int numberOfElements = 10;
  static Collection<Integer> testArrayList;

  static Box<Integer> box;

  @BeforeAll
  static void setup() {
    testArrayList = new ArrayList<>();
    for (int i = 0; i < numberOfElements; i++) {
      testArrayList.add(i);
    }
    box = new RoseBox<>(testArrayList);
  }

  @Test
  public void testGetSize() {
    assertEquals(numberOfElements, box.getSize());
  }

  @Test
  public void testIterator() {

    Iterator<Integer> iterator = box.iterator();
    Collection<Integer> actualContent = new ArrayList<>();

    for (int i = 0; i < numberOfElements; i++) {
      actualContent.add(iterator.next());
    }
    for (int i = 0; i < numberOfElements; i++) {
      Assertions.assertTrue(actualContent.contains(i));
    }
  }

  @Test
  public void testContains() {
    testArrayList.forEach(i -> Assertions.assertTrue(box.contains(i)));
    Assertions.assertFalse(box.contains(numberOfElements));
  }

  @Test
  public void testStream() {
    List<Integer> expected = new ArrayList<>(List.of(0, 1, 4, 9, 16, 25, 36, 49, 64, 81));
    expected.removeAll(box.stream().map(x -> x * x).toList());
    assertTrue(expected.isEmpty());
  }
}
