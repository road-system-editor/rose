package edu.kit.rose.controller;

import static edu.kit.rose.util.AccessorUtility.findAccessorOfType;
import static edu.kit.rose.util.RoadSystemUtility.findAnyGroup;
import static edu.kit.rose.util.RoadSystemUtility.findAnySegmentOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LanguageSelector;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests whether command sequences that involve creating and editing a segment can be redone
 * correctly.
 */
class CreateAndEditElementTest {
  private static final Path CONFIG_FILE = Path.of("build/tmp/config.json");
  private static final String SEGMENT_NAME = "the vanishing segment";

  private RoadSystem roadSystem;
  private ApplicationController applicationController;
  private AttributeController attributeController;
  private HierarchyController hierarchyController;
  private RoadSystemController roadSystemController;

  @BeforeEach
  void beforeEach() {
    var navigator = mock(Navigator.class);
    var languageSelector = mock(LanguageSelector.class);
    var modelFactory = new ModelFactory(CONFIG_FILE);
    var applicationDataSystem = modelFactory.createApplicationDataSystem();
    var project = modelFactory.createProject();
    var controllerFactory = new ControllerFactory(navigator, languageSelector,
        applicationDataSystem, project);

    this.roadSystem = project.getRoadSystem();
    this.applicationController = controllerFactory.getApplicationController();
    this.attributeController = controllerFactory.getAttributeController();
    this.hierarchyController = controllerFactory.getHierarchyController();
    this.roadSystemController = controllerFactory.getRoadSystemController();
  }

  /**
   * The following sequence is executed, then un-done und then re-done:
   * 1. create a segment
   * 2. set the name of the segment
   * 3. create a group
   * 4. add the segment to the group
   */
  @Test
  void testCreateAndEditSequence() {
    roadSystemController.createStreetSegment(SegmentType.BASE);
    Segment segment = findAnySegmentOfType(this.roadSystem, SegmentType.BASE);
    assertNotNull(segment);
    AttributeAccessor<String> nameAccessor = findAccessorOfType(segment, AttributeType.NAME);
    assertNotNull(nameAccessor);

    attributeController.setAttribute(nameAccessor, SEGMENT_NAME);
    assertEquals(SEGMENT_NAME, nameAccessor.getValue());

    hierarchyController.createGroup();
    assertEquals(2, this.roadSystem.getElements().getSize());
    Group group = findAnyGroup(this.roadSystem);
    assertNotNull(group);

    hierarchyController.addElementToGroup(segment, group);
    assertTrue(group.contains(segment));

    // undo all
    applicationController.undo();
    assertFalse(group.contains(segment));

    applicationController.undo();
    assertEquals(1, this.roadSystem.getElements().getSize());
    assertFalse(this.roadSystem.getElements().contains(group));

    applicationController.undo();
    assertNotEquals(SEGMENT_NAME, nameAccessor.getValue());

    applicationController.undo();
    assertEquals(0, this.roadSystem.getElements().getSize());

    // redo all
    applicationController.redo();
    segment = findAnySegmentOfType(this.roadSystem, SegmentType.BASE);
    assertNotNull(segment);
    nameAccessor = findAccessorOfType(segment, AttributeType.NAME);
    assertNotNull(nameAccessor);

    applicationController.redo();
    assertEquals(SEGMENT_NAME, nameAccessor.getValue());

    applicationController.redo();
    assertEquals(2, this.roadSystem.getElements().getSize());
    group = findAnyGroup(this.roadSystem);
    assertNotNull(group);

    applicationController.redo();
    assertTrue(group.contains(segment));
  }
}
