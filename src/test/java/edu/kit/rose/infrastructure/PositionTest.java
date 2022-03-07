package edu.kit.rose.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for the {@link Position} class.
 */
class PositionTest {
  private static final double POSITION_X = 7;
  private static final double POSITION_Y = 9;

  private Position position;

  @BeforeEach
  void beforeEach() {
    this.position = new Position(POSITION_X, POSITION_Y);
  }

  @Test
  void testGetCoordinates() {
    assertEquals(POSITION_X, position.getX());
    assertEquals(POSITION_Y, position.getY());
  }

  @Test
  void testStandardConstructor() {
    Position position = new Position();
    assertEquals(0, position.getX());
    assertEquals(0, position.getY());
  }

  @Test
  void testSetCoordinates() {
    var newX = 1234;
    position.setX(newX);
    assertEquals(new Position(newX, POSITION_Y), position);

    var newY = 5677;
    position.setY(newY);
    assertEquals(new Position(newX, newY), position);
  }

  @Test
  void testDistanceTo() {
    Position position1 = new Position(2, 2);
    Position position2 = new Position(5, 6);
    assertEquals(5, position1.distanceTo(position2), 0.001);
  }

  @Test
  void testToString() {
    assertTrue(position.toString().contains(String.format("%f", POSITION_X)));
    assertTrue(position.toString().contains(String.format("%f", POSITION_Y)));
  }

  @Test
  void testEquals() {
    var equalPosition = new Position(POSITION_X, POSITION_Y);

    assertEquals(equalPosition, this.position);
    assertEquals(equalPosition.hashCode(), this.position.hashCode());
  }


  @SuppressWarnings({"ConstantConditions", "SimplifiableAssertion", "java:S5785"})
  @Test
  void testPositionUnequalToNull() {
    // we can't use assertNotEquals because that would not test the "equals" method for null
    assertFalse(this.position.equals(null));
  }

  @SuppressWarnings({"EqualsBetweenInconvertibleTypes", "SimplifiableAssertion", "java:S5785"})
  @Test
  void testPositionUnequalToObjectOfOtherType() {
    // we can't use assertNotEquals it does not guarantee that equals is called on the position
    assertFalse(this.position.equals(new Movement(POSITION_X, POSITION_Y)));
  }

  @Test
  void testPositionUnequalToOtherPositions() {
    assertNotEquals(new Position(POSITION_X + 1, POSITION_Y), this.position);
    assertNotEquals(new Position(POSITION_X, POSITION_Y + 1), this.position);
    assertNotEquals(new Position(POSITION_X + 1, POSITION_Y + 1), this.position);
  }
}
