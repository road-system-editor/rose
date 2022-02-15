package edu.kit.rose.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for the {@link Movement} Class.
 */
public class MovementTest {
  private static final int FirstTestInt = 7;
  private static final int SecondTestInt = 9;

  @Test
  public void testGetCoordinates() {
    Movement movement = new Movement(FirstTestInt, SecondTestInt);
    Assertions.assertEquals(FirstTestInt, movement.getX());
    Assertions.assertEquals(SecondTestInt, movement.getY());
  }

  @Test
  public void testStandardConstructor() {
    Movement movement = new Movement();
    Assertions.assertEquals(0, movement.getX());
    Assertions.assertEquals(0, movement.getY());
  }
}
