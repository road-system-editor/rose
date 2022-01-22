package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
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

  @BeforeEach
  public void initialize() {
    Base testBase = new Base("testBase");
  }

  @Test
  public void testGetPosition() {
    Assertions.assertEquals(0, testBase.getCenter().getX());
    Assertions.assertEquals(0, testBase.getCenter().getY());
  }

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

  @Test
  public void testAttributeAccessors() {
    SortedBox<AttributeAccessor<?>> attributeAccessors = testBase.getAttributeAccessors();
    Assertions.assertEquals(7, attributeAccessors.getSize());
    Assertions.assertEquals(AttributeType.NAME, attributeAccessors.get(0).getAttributeType());
    Assertions.assertEquals(AttributeType.LENGTH, attributeAccessors.get(1).getAttributeType());
    Assertions.assertEquals(AttributeType.SLOPE, attributeAccessors.get(2).getAttributeType());
    Assertions.assertEquals(AttributeType.LANE_COUNT, attributeAccessors.get(3).getAttributeType());
    Assertions.assertEquals(AttributeType.LANE_COUNT, attributeAccessors.get(4).getAttributeType());
    Assertions.assertEquals(AttributeType.CONURBATION,
        attributeAccessors.get(5).getAttributeType());
    Assertions.assertEquals(AttributeType.MAX_SPEED, attributeAccessors.get(6).getAttributeType());
  }

  @Test
  public void testGetSegmentType() {
    Assertions.assertSame(SegmentType.BASE, testBase.getSegmentType());
  }

  @Test
  public void testIsContainer() {
    Assertions.assertFalse(testBase.isContainer());
  }

  //CompareTo
  @Test
  public void testCompareTo() {
    Base secondTestBase = new Base("secondTestBase");
    Assertions.assertTrue(testBase.compareTo(secondTestBase) > 0);
  }

  //Move
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
}
