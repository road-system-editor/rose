package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests the {@link RoseAttributeController} class, except for bulk edit.
 *
 * @see RoseAttributeControllerBulkEditTest
 */
class RoseAttributeControllerTest {
  private static final Path CONFIG_PATH = Path.of("build/tmp/config.json");

  private RoseAttributeController attributeController;
  private ApplicationDataSystem applicationDataSystem;
  private AttributeAccessor<Integer> accessor;
  private StorageLock storageLock;
  private int testInt;
  private static final int INITIAL_VALUE = 0;
  private static final int SET_VALUE = 1;
  private static final AttributeType TEST_TYPE = AttributeType.NAME;

  @BeforeEach
  void setUp() {
    var modelFactory = new ModelFactory(CONFIG_PATH);
    applicationDataSystem = modelFactory.createApplicationDataSystem();
    ChangeCommandBuffer changeCommandBuffer = new RoseChangeCommandBuffer();
    storageLock = Mockito.mock(StorageLock.class);
    attributeController = new RoseAttributeController(
        changeCommandBuffer,
        Mockito.mock(SelectionBuffer.class),
        storageLock,
        Mockito.mock(Navigator.class),
        modelFactory.createProject(),
        applicationDataSystem,
        new ReplacementLog());
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

  @Test
  void testWhenStorageLockAcquired() {
    attributeController.addShownAttributeType(TEST_TYPE);
    Mockito.when(storageLock.isStorageLockAcquired()).thenReturn(true);
    attributeController.removeShownAttributeType(TEST_TYPE);
    Assertions.assertTrue(applicationDataSystem.getShownAttributeTypes().contains(TEST_TYPE));

    attributeController.setAttribute(accessor, SET_VALUE);
    Assertions.assertEquals(testInt, INITIAL_VALUE);

    applicationDataSystem.getShownAttributeTypes()
            .forEach(applicationDataSystem::removeShownAttributeType);
    attributeController.addShownAttributeType(TEST_TYPE);
    Assertions.assertFalse(applicationDataSystem.getShownAttributeTypes().contains(TEST_TYPE));
  }
}
