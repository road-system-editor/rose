package edu.kit.rose.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for the {@link Movement} Class.
 */
class MovementTest {
  private static final double MOVEMENT_X = 7;
  private static final double MOVEMENT_Y = 9;

  private Movement movement;

  @BeforeEach
  void beforeEach() {
    this.movement = new Movement(MOVEMENT_X, MOVEMENT_Y);
  }

  @Test
  void testGetCoordinates() {
    assertEquals(MOVEMENT_X, this.movement.getX());
    assertEquals(MOVEMENT_Y, this.movement.getY());
  }

  @Test
  void testStandardConstructor() {
    Movement movement = new Movement();
    assertEquals(0, movement.getX());
    assertEquals(0, movement.getY());
  }

  @Test
  void testEquals() {
    var equalMovement = new Movement(MOVEMENT_X, MOVEMENT_Y);

    assertEquals(equalMovement, movement);
    assertEquals(equalMovement.hashCode(), movement.hashCode());
  }

  @SuppressWarnings({"ConstantConditions", "SimplifiableAssertion", "java:S5785"})
  @Test
  void testMovementUnequalToNull() {
    // we can't use assertNotEquals because that would not test the "equals" method for null
    assertFalse(this.movement.equals(null));
  }

  @SuppressWarnings({"EqualsBetweenInconvertibleTypes", "SimplifiableAssertion", "java:S5785"})
  @Test
  void testMovementUnequalToObjectOfOtherType() {
    // we can't use assertNotEquals it does not guarantee that equals is called on the movement
    assertFalse(this.movement.equals(new Position(MOVEMENT_X, MOVEMENT_Y)));
  }

  @Test
  void testMovementUnequalToOtherMovements() {
    assertNotEquals(new Movement(MOVEMENT_X + 1, MOVEMENT_Y), movement);
    assertNotEquals(new Movement(MOVEMENT_X, MOVEMENT_Y + 1), movement);
    assertNotEquals(new Movement(MOVEMENT_X + 1, MOVEMENT_Y + 1), movement);
  }

  @Test
  void testToString() {
    String movementString = this.movement.toString();
    assertTrue(movementString.contains(String.format("%f", MOVEMENT_X)));
    assertTrue(movementString.contains(String.format("%f", MOVEMENT_Y)));
  }
}
