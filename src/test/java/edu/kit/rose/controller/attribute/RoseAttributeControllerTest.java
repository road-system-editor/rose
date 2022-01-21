package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link RoseAttributeController} class.
 */
public class RoseAttributeControllerTest {

  private RoseAttributeController attributeController;
  private ApplicationDataSystem applicationDataSystem;
  private AttributeAccessor<Integer> accessor;
  private int testInt;
  private static final int INITIAL_VALUE = 0;
  private static final int SET_VALUE = 1;
  private static final AttributeType TEST_TYPE = AttributeType.NAME;

  @BeforeEach
  void setUp() {
    var modelFactory = new ModelFactory(null);
    applicationDataSystem = modelFactory.createApplicationDataSystem();
    attributeController = new RoseAttributeController(new RoseChangeCommandBuffer(),
        new RoseStorageLock(), modelFactory.createProject(), applicationDataSystem);
    accessor = new AttributeAccessor<>() {
      @Override
      public void setValue(Integer value) {
        testInt = value;
      }

      @Override
      public Integer getValue() {
        return testInt;
      }
    };
    testInt = INITIAL_VALUE;
  }

  @Test
  void testSetAttribute() {
    attributeController.setAttribute(accessor, SET_VALUE);
    Assertions.assertEquals(testInt, SET_VALUE);
  }

  @Test
  void testAddShownAttribute() { //Boxes need to provide contains() method
  }

  @Test
  void testRemoveShownAttribute() { //Boxes need to provide contains() method
  }
}
