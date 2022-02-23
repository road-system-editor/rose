package edu.kit.rose.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for the {@link Movement} Class.
 */
public class MovementTest {
  private static final int MOVEMENT_X = 7;
  private static final int MOVEMENT_Y = 9;

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

  @Test
  void testEqualsWithUnequalObjects() {
    //noinspection ConstantConditions,SimplifiableAssertion <-- that's what we're testing
    assertFalse(movement.equals(null));

    //noinspection SimplifiableAssertion,EqualsBetweenInconvertibleTypes
    assertFalse(movement.equals(new Position(MOVEMENT_X, MOVEMENT_Y)));

    assertNotEquals(new Movement(MOVEMENT_X + 1, MOVEMENT_Y), movement);
    assertNotEquals(new Movement(MOVEMENT_X, MOVEMENT_Y + 1), movement);
    assertNotEquals(new Movement(MOVEMENT_X + 1, MOVEMENT_Y + 1), movement);
  }
}
