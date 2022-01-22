package edu.kit.rose.infrastructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * Unit Test for the RoseBox Class.
 */
public class RoseSortedBoxTest {
  static int numberOfElements = 10;
  static List<Integer> testArrayList;

  static SortedBox<Integer> sortedBox;

  @BeforeAll
  static void setup() {
    testArrayList = new ArrayList<>();
    for (int i = 0; i < numberOfElements; i++) {
      testArrayList.add(i);
    }

    sortedBox = new SimpleSortedBox<>(testArrayList);
  }

  @Test
  public void testGetSize() {
    Assertions.assertEquals(numberOfElements, sortedBox.getSize());
  }

  @Test
  public void testIterator() {
    Iterator<Integer> iterator = sortedBox.iterator();

    for (int i = 0; i < numberOfElements; i++) {
      Assertions.assertEquals(i, iterator.next());
    }
  }

  @Test
  public void testGet() {
    for (int i = 0; i < numberOfElements; i++) {
      Assertions.assertEquals(i, sortedBox.get(i));
    }

  }

  @Test
  public void testContains() {
    testArrayList.forEach(i -> Assertions.assertTrue(sortedBox.contains(i)));
    Assertions.assertFalse(sortedBox.contains(numberOfElements));
  }

}