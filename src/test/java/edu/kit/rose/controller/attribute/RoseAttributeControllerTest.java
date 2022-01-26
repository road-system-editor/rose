package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        new RoseStorageLock(), Mockito.mock(Navigator.class), modelFactory.createProject(),
        applicationDataSystem);
    accessor = new AttributeAccessor<>(
        AttributeType.LANE_COUNT,
        () -> testInt,
        newValue -> testInt = newValue
    );
    testInt = INITIAL_VALUE;
  }

  @Test
  void testSetAttribute() {
    attributeController.setAttribute(accessor, SET_VALUE);
    Assertions.assertEquals(testInt, SET_VALUE);
  }

  @Test
  void testAddShownAttribute() {
    applicationDataSystem.getShownAttributeTypes()
        .forEach(applicationDataSystem::removeShownAttributeType);
    attributeController.addShownAttributeType(TEST_TYPE);
    Assertions.assertTrue(applicationDataSystem.getShownAttributeTypes().contains(TEST_TYPE));
  }

  @Test
  void testRemoveShownAttribute() {
    attributeController.addShownAttributeType(TEST_TYPE);
    attributeController.removeShownAttributeType(TEST_TYPE);
    Assertions.assertFalse(applicationDataSystem.getShownAttributeTypes().contains(TEST_TYPE));
  }
}
