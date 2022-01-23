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
import org.junit.jupiter.api.BeforeAll;
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
   * Tests if the getPosition function returns the initial location (0,0)
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
  public void testAttributeAccessors() {
    SortedBox<AttributeAccessor<?>> attributeAccessors = testBase.getAttributeAccessors();
    Assertions.assertEquals(7, attributeAccessors.getSize());
    /*Assertions.assertEquals(AttributeType.NAME, attributeAccessors.get(0).getAttributeType());
    Assertions.assertEquals(AttributeType.LENGTH, attributeAccessors.get(1).getAttributeType());
    Assertions.assertEquals(AttributeType.SLOPE, attributeAccessors.get(2).getAttributeType());
    Assertions.assertEquals(AttributeType.LANE_COUNT, attributeAccessors.get(3).getAttributeType());
    Assertions.assertEquals(AttributeType.LANE_COUNT, attributeAccessors.get(4).getAttributeType());
    Assertions.assertEquals(AttributeType.CONURBATION,
        attributeAccessors.get(5).getAttributeType());
    Assertions.assertEquals(AttributeType.MAX_SPEED, attributeAccessors.get(6).getAttributeType()
    );*/
    //TODO: use after attributeAccessor is implemented.
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
  }

  /**
   * Tests the move function by moving the Base and checking the new center Position.
   */
  @Test
  public void testMove() {
    int xStart = 0;
    int yStart = 0;
    int xMovement = 50;
    int yMovement = 50;
    Movement testMovement = new Movement(xMovement, yMovement);
    testBase.move(testMovement);
    Assertions.assertEquals(xStart + xMovement, testBase.getCenter().getX());
    Assertions.assertEquals(yStart + yMovement, testBase.getCenter().getY());
    Assertions.assertEquals(xStart + xMovement, testBase.getCenter().getX());
    Assertions.assertEquals(yStart + yMovement, testBase.getCenter().getY());
  }

  /**
   * Tests the move function by moving it and checking the Positions of the Connectors.
   */
  @Test
  public void testMoveConnectors() {
    int xStartEntry = -50;
    int yStartEntry = 0;
    int xStartExit = 50;
    int yStartExit = 0;
    int xMovement = 50;
    int yMovement = 50;
    Movement testMovement = new Movement(xMovement, yMovement);
    testBase.move(testMovement);
    Assertions.assertEquals(xStartEntry + xMovement,
        testBase.getEntry().getPosition().getX());
    Assertions.assertEquals(yStartEntry + yMovement,
        testBase.getEntry().getPosition().getY());
    Assertions.assertEquals(xStartExit + xMovement,
        testBase.getExit().getPosition().getX());
    Assertions.assertEquals(yStartExit + yMovement,
        testBase.getExit().getPosition().getY());
  }

  /**
   * Tests the Standard Constructor
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
