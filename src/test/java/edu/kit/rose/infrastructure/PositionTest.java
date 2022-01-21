package edu.kit.rose.infrastructure;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for the {@link Position} Class.
 */
public class PositionTest {
  private static final int FirstTestInt = 7;
  private static final int SecondTestInt = 9;

  @Test
  public void testGetCoordinates() {
    Position position = new Position(FirstTestInt, SecondTestInt);
    Assertions.assertEquals(FirstTestInt, position.getX());
    Assertions.assertEquals(SecondTestInt, position.getY());
  }

  @Test
  public void testStandardConstructor() {
    Position position = new Position();
    Assertions.assertEquals(0, position.getX());
    Assertions.assertEquals(0, position.getY());
  }

  @Test
  public void testSetCoordinates() {
    Position position = new Position(FirstTestInt, SecondTestInt);
    position.setX(SecondTestInt);
    position.setY(FirstTestInt);
    Assertions.assertEquals(SecondTestInt, position.getX());
    Assertions.assertEquals(FirstTestInt, position.getY());
  }
}
