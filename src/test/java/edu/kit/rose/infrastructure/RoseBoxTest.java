package edu.kit.rose.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Unit Test for the RoseBox Class.
 */
public class RoseBoxTest {
    static int numberOfElements = 10;
    static Collection<Integer> testArrayList;

    @BeforeAll
    static void setup() {
        testArrayList = new ArrayList<>();
        for (int i = 0; i < numberOfElements; i++) {
            testArrayList.add(i);
        }
    }
    @Test
    public void TestGetSize() {
        Box<Integer> box = new SimpleBox<>(testArrayList);
        Assertions.assertEquals(numberOfElements, box.getSize());
    }

    @Test
    public void TestIterator() {

        Box<Integer> box = new SimpleBox<>(testArrayList);
        Iterator<Integer> iterator = box.iterator();
        Collection<Integer> actualContent = new ArrayList<>();

        for(int i = 0; i < numberOfElements; i++) {
            actualContent.add(iterator.next());
        }
        for(int i = 0; i < numberOfElements; i++) {
            Assertions.assertTrue(actualContent.contains(i));
        }
    }
}
