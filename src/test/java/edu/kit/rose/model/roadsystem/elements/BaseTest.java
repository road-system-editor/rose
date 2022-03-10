package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Base} class.
 */
class BaseTest {

  Base testBase;
  private static final int xStartCenter = 0;
  private static final int yStartCenter = 0;

  @BeforeEach
  public void initialize() {
    testBase = new Base("testBase");
  }

  /**
   * Tests if the getPosition function returns the initial location (0,0).
   */
  @Test
  void testGetPosition() {
    Assertions.assertEquals(0, testBase.getCenter().getX());
    Assertions.assertEquals(0, testBase.getCenter().getY());
  }

  /**
   * Tests if the Connector.getPosition function returns the initial location.
   */
  @Test
  void testGetConnectorPosition() {
    Assertions.assertEquals(0, testBase.getEntry().getPosition().getY());
    Assertions.assertEquals(-27, testBase.getEntry().getPosition().getX());
    Assertions.assertEquals(0, testBase.getExit().getPosition().getY());
    Assertions.assertEquals(33, testBase.getExit().getPosition().getX());
  }

  /**
   * Tests if the getSegmentType returns Base.
   */
  @Test
  void testGetSegmentType() {
    Assertions.assertSame(SegmentType.BASE, testBase.getSegmentType());
  }

  /**
   * Tests if isContainer returns false.
   */
  @Test
  void testIsContainer() {
    Assertions.assertFalse(testBase.isContainer());
  }

  /**
   * Tests the compareTo function by creating a new Base and checking if the old one is smaller
   * than the new One.
   */
  @Test
  void testCompareTo() {
    Base secondTestBase = new Base("secondTestBase");
    Assertions.assertTrue(testBase.compareTo(secondTestBase) < 0);

    Base firstTestBase = new Base("firstTestBase");
    secondTestBase = new Base("secondTestBase");
    Assertions.assertTrue(firstTestBase.compareTo(secondTestBase) < 0);
  }

  /**
   * Tests the move function by moving the Base and checking the new center Position.
   */
  @Test
  void testMove() {
    int startX = 0;
    int startY = 0;
    int movementX = 50;
    int movementY = 50;
    Movement testMovement = new Movement(movementX, movementY);
    testBase.move(testMovement);
    Assertions.assertEquals(startX + movementX, testBase.getCenter().getX());
    Assertions.assertEquals(startY + movementY, testBase.getCenter().getY());
    Assertions.assertEquals(startX + movementX, testBase.getCenter().getX());
    Assertions.assertEquals(startY + movementY, testBase.getCenter().getY());
  }

  /**
   * Tests the move function by moving it and checking the Positions of the Connectors.
   */
  @Test
  void testMoveConnectors() {
    final double startEntryY = 0;
    final double startEntryX = -27;
    final double startExitY = 0;
    final double startExitX = 33;
    final double movementX = 50;
    final double movementY = 50;

    Movement testMovement = new Movement(movementX, movementY);
    testBase.move(testMovement);

    Assertions.assertEquals(startEntryX,
        testBase.getEntry().getPosition().getX());
    Assertions.assertEquals(startEntryY,
        testBase.getEntry().getPosition().getY());
    Assertions.assertEquals(startExitX,
        testBase.getExit().getPosition().getX());
    Assertions.assertEquals(startExitY,
        testBase.getExit().getPosition().getY());
  }

  @Test
  void testMoveSingleConnector() {
    testBase.getEntry().move(new Movement(10, -10));
    var entryPos = testBase.getAbsoluteConnectorPosition(testBase.getEntry());
    Assertions.assertEquals(-10, entryPos.getY());
    Assertions.assertEquals(-17, entryPos.getX());
    var centerPos = testBase.getCenter();
    Assertions.assertEquals(8, centerPos.getX());
    Assertions.assertEquals(-5, centerPos.getY());
    var exitPos = testBase.getAbsoluteConnectorPosition(testBase.getExit());
    Assertions.assertEquals(0, exitPos.getY());
    Assertions.assertEquals(33, exitPos.getX());
  }

  /**
   * Tests the Standard Constructor.
   */
  @Test
  void testStandardConstructor() {
    testBase = new Base();
    Assertions.assertEquals(xStartCenter, testBase.getCenter().getX());
    Assertions.assertEquals(yStartCenter, testBase.getCenter().getY());
    Assertions.assertEquals(SegmentType.BASE.name(), testBase.getName());
  }

  /**
   * Tests the GetConnectors function.
   */
  @Test
  void testGetConnectors() {
    Box<Connector> connectors = testBase.getConnectors();
    Assertions.assertEquals(2, connectors.getSize());
  }

  /**
   * Tests the getThis function.
   */
  @Test
  void testGetThis() {
    Assertions.assertEquals(testBase, testBase.getThis());
  }

  //TODO: Tests for subscribers.


}
