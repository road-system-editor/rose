package edu.kit.rose.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Unit Test for the RoseBox Class.
 */
public class RoseSortedBoxTest {
    static int numberOfElements = 10;
    static List<Integer> testArrayList;

    @BeforeAll
    static void setup() {
        testArrayList = new ArrayList<>();
        for (int i = 0; i < numberOfElements; i++) {
            testArrayList.add(i);
        }
    }

    @Test
    public void TestGetSize() {
        SortedBox<Integer> sortedBox = new SimpleSortedBox<>(testArrayList);
        Assertions.assertEquals(numberOfElements, sortedBox.getSize());
    }

    @Test
    public void TestIterator() {

        SortedBox<Integer> sortedBox = new SimpleSortedBox<>(testArrayList);
        Iterator<Integer> iterator = sortedBox.iterator();

        for(int i = 0; i < numberOfElements; i++) {
            Assertions.assertEquals(i, iterator.next());
        }
    }

    @Test
    public void TestGet() {
        SortedBox<Integer> sortedBox = new SimpleSortedBox<>(testArrayList);
        for(int i = 0; i < numberOfElements; i++) {
            Assertions.assertEquals(i, sortedBox.get(i));
        }

    }

}