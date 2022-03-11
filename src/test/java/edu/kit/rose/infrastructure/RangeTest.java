package edu.kit.rose.infrastructure;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Range}.
 */
class RangeTest {

  private Range<Double> testRange;

  @BeforeEach
  void setup() {
    this.testRange = new Range<>(69d, 420d);
  }

  @Test
  void testConstructorNull() {
    assertThrows(NullPointerException.class, () -> new Range<Integer>(null, null));
  }

  @Test
  void testConstructorWithReversedEndPoints() {
    var range = new Range<>(420d, 69d);
    assertTrue(range.contains(120d));
  }

  @Test
  void testContains() {
    assertTrue(testRange.contains(69d));
    assertTrue(testRange.contains(420d));
    assertTrue(testRange.contains(100d));
    Assertions.assertFalse(testRange.contains(68d));
    Assertions.assertFalse(testRange.contains(421d));
    Assertions.assertFalse(testRange.contains(0d));
    Assertions.assertFalse(testRange.contains(690d));
    Assertions.assertFalse(testRange.contains(-690d));
  }
}
