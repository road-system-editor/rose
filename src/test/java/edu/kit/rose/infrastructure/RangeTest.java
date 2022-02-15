package edu.kit.rose.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Range}.
 */
public class RangeTest {

  private Range<Double> testRange;

  @BeforeEach
  void setup() {
    this.testRange = new Range<>(69d, 420d);
  }

  @Test
  void testContains() {
    Assertions.assertTrue(testRange.contains(69d));
    Assertions.assertTrue(testRange.contains(420d));
    Assertions.assertTrue(testRange.contains(100d));
    Assertions.assertFalse(testRange.contains(68d));
    Assertions.assertFalse(testRange.contains(421d));
    Assertions.assertFalse(testRange.contains(0d));
    Assertions.assertFalse(testRange.contains(690d));
    Assertions.assertFalse(testRange.contains(-690d));
  }
}
