package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the Entrance Class.
 */
public class EntranceTest {
  Entrance testEntrance;
  private static final int xStartCenter = 0;
  private static final int yStartCenter = 0;

  @BeforeEach
  public void initialize() {
    testEntrance = new Entrance("testEntrance");
  }

  /**
   * Tests if the getPosition function returns the initial location (0,0).
   */
  @Test
  public void testGetPosition() {
    Assertions.assertEquals(0, testEntrance.getCenter().getX());
    Assertions.assertEquals(0, testEntrance.getCenter().getY());
  }

  /**
   * Tests if the Connector.getPosition function returns the initial location.
   */
  @Test
  public void testGetConnectorPosition() {
    Assertions.assertEquals(0, testEntrance.getEntry().getPosition().getX());
    Assertions.assertEquals(27, testEntrance.getEntry().getPosition().getY());
    Assertions.assertEquals(0, testEntrance.getExit().getPosition().getX());
    Assertions.assertEquals(-33, testEntrance.getExit().getPosition().getY());
    Assertions.assertEquals(30, testEntrance.getRamp().getPosition().getX());
    Assertions.assertEquals(14, testEntrance.getRamp().getPosition().getY());
  }

  @Test
  public void testGetName() {
    Assertions.assertEquals("testEntrance", testEntrance.getName());
  }

  /**
   * Tests if the AttributeAccessors are of the correct Type and in the right Order.
   */
  @Test
  public void testAttributeAccessors() {
    SortedBox<AttributeAccessor<?>> attributeAccessors = testEntrance.getAttributeAccessors();
    Assertions.assertEquals(9, attributeAccessors.getSize());
    Assertions.assertEquals(AttributeType.NAME, attributeAccessors.get(0).getAttributeType());
    Assertions.assertEquals(AttributeType.COMMENT, attributeAccessors.get(1).getAttributeType());
    Assertions.assertEquals(AttributeType.LENGTH, attributeAccessors.get(2).getAttributeType());
    Assertions.assertEquals(AttributeType.SLOPE, attributeAccessors.get(3).getAttributeType());
    Assertions.assertEquals(AttributeType.LANE_COUNT, attributeAccessors.get(4).getAttributeType());
    Assertions.assertEquals(AttributeType.CONURBATION,
        attributeAccessors.get(5).getAttributeType());
    Assertions.assertEquals(AttributeType.MAX_SPEED, attributeAccessors.get(6).getAttributeType());
    Assertions.assertEquals(AttributeType.LANE_COUNT_RAMP,
        attributeAccessors.get(7).getAttributeType());
    Assertions.assertEquals(AttributeType.MAX_SPEED_RAMP,
        attributeAccessors.get(8).getAttributeType());
  }

  /**
   * Tests if the getSegmentType returns Entrance.
   */
  @Test
  public void testGetSegmentType() {
    Assertions.assertSame(SegmentType.ENTRANCE, testEntrance.getSegmentType());
  }

  /**
   * Tests if isContainer returns false.
   */
  @Test
  public void testIsContainer() {
    Assertions.assertFalse(testEntrance.isContainer());
  }

  /**
   * Tests the compareTo function by creating a new Entrance and checking if the old one is smaller
   * than the new One.
   */
  @Test
  public void testCompareTo() {
    Entrance secondTestEntrance = new Entrance("secondTestEntrance");
    Assertions.assertTrue(testEntrance.compareTo(secondTestEntrance) < 0);
  }

  /**
   * Tests the move function by moving the Entrance and checking the new center Position.
   */
  @Test
  public void testMove() {
    int startX = 0;
    int startY = 0;
    int movementX = 50;
    int movementY = 50;
    Movement testMovement = new Movement(movementX, movementY);
    testEntrance.move(testMovement);
    Assertions.assertEquals(startX + movementX, testEntrance.getCenter().getX());
    Assertions.assertEquals(startY + movementY, testEntrance.getCenter().getY());
    Assertions.assertEquals(startX + movementX, testEntrance.getCenter().getX());
    Assertions.assertEquals(startY + movementY, testEntrance.getCenter().getY());
  }

  /**
   * Tests the move function by moving it and checking the Positions of the Connectors.
   */
  @Test
  public void testMoveConnectors() {
    final double startEntryX = 0;
    final double startEntryY = 27;
    final double startExitX = 0;
    final double startExitY = -33;
    final int startRampX = 30;
    final int startRampY = 14;
    final int movementX = 50;
    final int movementY = 50;

    Movement testMovement = new Movement(movementX, movementY);
    testEntrance.move(testMovement);

    Assertions.assertEquals(startEntryX,
        testEntrance.getEntry().getPosition().getX());
    Assertions.assertEquals(startEntryY,
        testEntrance.getEntry().getPosition().getY());
    Assertions.assertEquals(startExitX,
        testEntrance.getExit().getPosition().getX());
    Assertions.assertEquals(startExitY,
        testEntrance.getExit().getPosition().getY());
    Assertions.assertEquals(startRampX,
        testEntrance.getRamp().getPosition().getX());
    Assertions.assertEquals(startRampY,
        testEntrance.getRamp().getPosition().getY());
  }

  /**
   * Tests the Standard Constructor.
   */
  @Test
  public void testStandardConstructor() {
    testEntrance = new Entrance();
    Assertions.assertEquals(xStartCenter, testEntrance.getCenter().getX());
    Assertions.assertEquals(yStartCenter, testEntrance.getCenter().getY());
    Assertions.assertEquals(SegmentType.ENTRANCE.name(), testEntrance.getName());
  }

  /**
   * Tests the GetConnectors function.
   */
  @Test
  public void testGetConnectors() {
    Box<Connector> connectors = testEntrance.getConnectors();
    Assertions.assertEquals(3, connectors.getSize());
  }

  /**
   * Tests the getThis function.
   */
  @Test
  public void testGetThis() {
    Assertions.assertEquals(testEntrance, testEntrance.getThis());
  }

  //TODO: Tests for subscribers.

}