package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Exit} class.
 */
class ExitTest {
  Exit testExit;
  private static final int xStartCenter = 0;
  private static final int yStartCenter = 0;

  @BeforeEach
  public void initialize() {
    testExit = new Exit("testExit");
  }

  /**
   * Tests if the getPosition function returns the initial location (0,0).
   */
  @Test
  void testGetPosition() {
    Assertions.assertEquals(0, testExit.getCenter().getX());
    Assertions.assertEquals(0, testExit.getCenter().getY());
  }

  /**
   * Tests if the Connector.getPosition function returns the initial location.
   */
  @Test
  void testGetConnectorPosition() {
    Assertions.assertEquals(0, testExit.getEntry().getPosition().getX());
    Assertions.assertEquals(27, testExit.getEntry().getPosition().getY());
    Assertions.assertEquals(0, testExit.getExit().getPosition().getX());
    Assertions.assertEquals(-33, testExit.getExit().getPosition().getY());
    Assertions.assertEquals(32, testExit.getRamp().getPosition().getX());
    Assertions.assertEquals(-14, testExit.getRamp().getPosition().getY());
  }

  @Test
  void testGetName() {
    Assertions.assertEquals("testExit", testExit.getName());
  }

  /**
   * Tests if the getSegmentType returns Exit.
   */
  @Test
  void testGetSegmentType() {
    Assertions.assertSame(SegmentType.EXIT, testExit.getSegmentType());
  }

  /**
   * Tests if isContainer returns false.
   */
  @Test
  void testIsContainer() {
    Assertions.assertFalse(testExit.isContainer());
  }

  /**
   * Tests the compareTo function by creating a new Exit and checking if the old one is smaller
   * than the new One.
   */
  @Test
  void testCompareTo() {
    Exit secondTestExit = new Exit("secondTestExit");
    Assertions.assertTrue(testExit.compareTo(secondTestExit) < 0);
  }

  /**
   * Tests the move function by moving the Exit and checking the new center Position.
   */
  @Test
  void testMove() {
    int startX = 0;
    int startY = 0;
    int movementX = 50;
    int movementY = 50;
    Movement testMovement = new Movement(movementX, movementY);
    testExit.move(testMovement);
    Assertions.assertEquals(startX + movementX, testExit.getCenter().getX());
    Assertions.assertEquals(startY + movementY, testExit.getCenter().getY());
    Assertions.assertEquals(startX + movementX, testExit.getCenter().getX());
    Assertions.assertEquals(startY + movementY, testExit.getCenter().getY());
  }

  /**
   * Tests the move function by moving it and checking the Positions of the Connectors.
   */
  @Test
  void testMoveConnectors() {
    final double startEntryX = 0;
    final double startEntryY = 27;
    final double startExitX = 0;
    final double startExitY = -33;
    final int startRampX = 32;
    final int startRampY = -14;
    final int movementX = 50;
    final int movementY = 50;

    Movement testMovement = new Movement(movementX, movementY);
    testExit.move(testMovement);

    Assertions.assertEquals(startEntryX,
        testExit.getEntry().getPosition().getX());
    Assertions.assertEquals(startEntryY,
        testExit.getEntry().getPosition().getY());
    Assertions.assertEquals(startExitX,
        testExit.getExit().getPosition().getX());
    Assertions.assertEquals(startExitY,
        testExit.getExit().getPosition().getY());
    Assertions.assertEquals(startRampX,
        testExit.getRamp().getPosition().getX());
    Assertions.assertEquals(startRampY,
        testExit.getRamp().getPosition().getY());
  }

  /**
   * Tests the Standard Constructor.
   */
  @Test
  void testStandardConstructor() {
    testExit = new Exit();
    Assertions.assertEquals(xStartCenter, testExit.getCenter().getX());
    Assertions.assertEquals(yStartCenter, testExit.getCenter().getY());
    Assertions.assertEquals(SegmentType.EXIT.name(), testExit.getName());
  }

  /**
   * Tests the GetConnectors function.
   */
  @Test
  void testGetConnectors() {
    Box<Connector> connectors = testExit.getConnectors();
    Assertions.assertEquals(3, connectors.getSize());
  }

  /**
   * Tests the getThis function.
   */
  @Test
  void testGetThis() {
    Assertions.assertEquals(testExit, testExit.getThis());
  }

  //TODO: Tests for subscribers.

  @Test
  void rotationTest() {
    var funnyNumber = 69;
    Assertions.assertEquals(0, testExit.getRotation());

    testExit.rotate(funnyNumber);
    Assertions.assertEquals(funnyNumber, testExit.getRotation());

    testExit.rotate(-360);
    Assertions.assertEquals(funnyNumber, testExit.getRotation());

    testExit.rotate(360);
    Assertions.assertEquals(funnyNumber, testExit.getRotation());

    var evenFunnierNumber = 420;

    testExit.rotate(evenFunnierNumber);
    Assertions.assertEquals(129, testExit.getRotation());

    testExit.rotate(-evenFunnierNumber);
    Assertions.assertEquals(funnyNumber, testExit.getRotation());
  }

  @Test
  void testRotationEntryConnector() {
    var oldEntryPos = testExit.getAbsoluteConnectorPosition(testExit.getEntry());
    testExit.rotate(90);
    Assertions.assertEquals(oldEntryPos.getX(),
        testExit.getAbsoluteConnectorPosition(testExit.getEntry()).getY());
    Assertions.assertEquals(-oldEntryPos.getY(),
        testExit.getAbsoluteConnectorPosition(testExit.getEntry()).getX());
  }

  @Test
  void testRotationExitConnector() {
    var oldExitPos = testExit.getAbsoluteConnectorPosition(testExit.getExit());
    testExit.rotate(90);
    Assertions.assertEquals(oldExitPos.getX(),
        testExit.getAbsoluteConnectorPosition(testExit.getExit()).getY());
    Assertions.assertEquals(-oldExitPos.getY(),
        testExit.getAbsoluteConnectorPosition(testExit.getExit()).getX());
  }

  @Test
  void testRotationExitConnectorWithMovement() {
    var moveX = 69;
    var moveY = 420;
    testExit.move(new Movement(moveX, moveY));
    var exitPos = testExit.getAbsoluteConnectorPosition(testExit.getExit());
    var oldPositionExitOrigin = new Position(exitPos.getX() - moveX,
        exitPos.getY() - moveY);
    testExit.rotate(90);
    Assertions.assertEquals(moveY + oldPositionExitOrigin.getX(),
        testExit.getAbsoluteConnectorPosition(testExit.getExit()).getY());
    Assertions.assertEquals(moveX - oldPositionExitOrigin.getY(),
        testExit.getAbsoluteConnectorPosition(testExit.getExit()).getX());
  }

  @Test
  void testRotationCenter() {
    var oldCenter = new Position(testExit.getCenter().getX(), testExit.getCenter().getX());
    testExit.rotate(90);
    Assertions.assertEquals(oldCenter.getX(), testExit.getCenter().getX());
    Assertions.assertEquals(oldCenter.getY(), testExit.getCenter().getY());
  }
  
}
