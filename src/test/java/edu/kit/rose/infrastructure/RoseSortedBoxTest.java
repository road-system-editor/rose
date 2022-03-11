package edu.kit.rose.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit Test for the RoseBox Class.
 */
class RoseSortedBoxTest {
  static int numberOfElements = 10;
  static List<Integer> testArrayList;

  static SortedBox<Integer> sortedBox;

  @BeforeAll
  static void setup() {
    testArrayList = new ArrayList<>();
    for (int i = 0; i < numberOfElements; i++) {
      testArrayList.add(i);
    }

    sortedBox = new RoseSortedBox<>(testArrayList);
  }

  @Test
  void testGetSize() {
    Assertions.assertEquals(numberOfElements, sortedBox.getSize());
  }

  @Test
  void testIterator() {
    Iterator<Integer> iterator = sortedBox.iterator();

    for (int i = 0; i < numberOfElements; i++) {
      Assertions.assertEquals(i, iterator.next());
    }
  }

  @Test
  void testIteratorDoesNotSupportRemove() {
    Iterator<Integer> iterator = sortedBox.iterator();

    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }

  @Test
  void testGet() {
    for (int i = 0; i < numberOfElements; i++) {
      Assertions.assertEquals(i, sortedBox.get(i));
    }

  }

  @Test
  void testContains() {
    testArrayList.forEach(i -> Assertions.assertTrue(sortedBox.contains(i)));
    Assertions.assertFalse(sortedBox.contains(numberOfElements));
  }

  @Test
  void testStream() {
    List<Integer> expected = List.of(0, 1, 4, 9, 16, 25, 36, 49, 64, 81);
    List<Integer> actual = sortedBox.stream().map(x -> x * x).toList();
    assertEquals(expected, actual);
  }

}