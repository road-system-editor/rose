package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for the Base Class.
 */
public class BaseTest {

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
  public void testGetPosition() {
    Assertions.assertEquals(0, testBase.getCenter().getX());
    Assertions.assertEquals(0, testBase.getCenter().getY());
  }

  /**
   * Tests if the Connector.getPosition function returns the initial location.
   */
  @Test
  public void testGetConnectorPosition() {
    Assertions.assertEquals(-50, testBase.getEntry().getPosition().getX());
    Assertions.assertEquals(0, testBase.getEntry().getPosition().getY());
    Assertions.assertEquals(50, testBase.getExit().getPosition().getX());
    Assertions.assertEquals(0, testBase.getExit().getPosition().getY());
  }

  @Test
  public void testGetName() {
    Assertions.assertEquals("testBase", testBase.getName());
  }

  /**
   * Tests if the AttributeAccessors are of the correct Type and in the right Order.
   */
  @Test
  public void testGetAttributeAccessors() {
    SortedBox<AttributeAccessor<?>> attributeAccessors = testBase.getAttributeAccessors();
    Assertions.assertEquals(7, attributeAccessors.getSize());
    Assertions.assertEquals(AttributeType.NAME, attributeAccessors.get(0).getAttributeType());
    Assertions.assertEquals(AttributeType.LENGTH, attributeAccessors.get(1).getAttributeType());
    Assertions.assertEquals(AttributeType.SLOPE, attributeAccessors.get(2).getAttributeType());
    Assertions.assertEquals(AttributeType.LANE_COUNT, attributeAccessors.get(3).getAttributeType());
    Assertions.assertEquals(AttributeType.LANE_COUNT, attributeAccessors.get(4).getAttributeType());
    Assertions.assertEquals(AttributeType.CONURBATION,
        attributeAccessors.get(5).getAttributeType());
    Assertions.assertEquals(AttributeType.MAX_SPEED, attributeAccessors.get(6).getAttributeType()
    );
  }

  /**
   * Tests if the getSegmentType returns Base.
   */
  @Test
  public void testGetSegmentType() {
    Assertions.assertSame(SegmentType.BASE, testBase.getSegmentType());
  }

  /**
   * Tests if isContainer returns false.
   */
  @Test
  public void testIsContainer() {
    Assertions.assertFalse(testBase.isContainer());
  }

  /**
   * Tests the compareTo function by creating a new Base and checking if the old one is smaller
   * than the new One.
   */
  @Test
  public void testCompareTo() {
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
  public void testMove() {
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
  public void testMoveConnectors() {
    final int startEntryX = -50;
    final int startEntryY = 0;
    final int startExitX = 50;
    final int startExitY = 0;
    final int movementX = 50;
    final int movementY = 50;
    Movement testMovement = new Movement(movementX, movementY);
    testBase.move(testMovement);
    Assertions.assertEquals(startEntryX + movementX,
        testBase.getEntry().getPosition().getX());
    Assertions.assertEquals(startEntryY + movementY,
        testBase.getEntry().getPosition().getY());
    Assertions.assertEquals(startExitX + movementX,
        testBase.getExit().getPosition().getX());
    Assertions.assertEquals(startExitY + movementY,
        testBase.getExit().getPosition().getY());
  }

  /**
   * Tests the Standard Constructor.
   */
  @Test
  public void testStandardConstructor() {
    testBase = new Base();
    Assertions.assertEquals(xStartCenter, testBase.getCenter().getX());
    Assertions.assertEquals(yStartCenter, testBase.getCenter().getY());
    Assertions.assertEquals(SegmentType.BASE.name(), testBase.getName());
  }

  /**
   * Tests the GetConnectors function.
   */
  @Test
  public void testGetConnectors() {
    Box<Connector> connectors = testBase.getConnectors();
    Assertions.assertEquals(2, connectors.getSize());
  }

  /**
   * Tests the getThis function.
   */
  @Test
  public void testGetThis() {
    Assertions.assertEquals(testBase, testBase.getThis());
  }

  //TODO: Tests for subscribers.

}