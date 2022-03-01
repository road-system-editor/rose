package edu.kit.rose.controller.hierarchy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.RoseSelectionBuffer;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.util.RoadSystemUtility;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link RoseHierarchyController}.
 */
class RoseHierarchyControllerTest {
  private static final Path CONFIG_PATH = Path.of("build/tmp/no-config.json");
  private static final Boolean SELECTED = true;
  private static final Boolean UNSELECTED = false;

  private RoadSystem roadSystem;
  private Group group;
  private Element element;
  private SelectionBuffer selectionBuffer;
  private RoseHierarchyController controller;

  @BeforeEach
  public void setUp() {
    var modelFactory = new ModelFactory(CONFIG_PATH);
    Project project = modelFactory.createProject();
    this.roadSystem = project.getRoadSystem();
    this.element = roadSystem.createSegment(SegmentType.BASE);
    this.group = roadSystem.createGroup(Set.of());
    this.selectionBuffer = new RoseSelectionBuffer();

    this.controller = new RoseHierarchyController(new RoseStorageLock(),
            new RoseChangeCommandBuffer(), this.selectionBuffer, project,
            mock(Navigator.class), new ReplacementLog());
  }

  @Test
  void createGroupTest() {
    this.controller.createGroup();
    assertNotNull(RoadSystemUtility.findAnyGroup(this.roadSystem));
  }

  @Test
  void deleteGroupTest() {
    this.controller.deleteGroup(this.group);
    assertFalse(this.roadSystem.getElements().contains(group));
  }

  @Test
  void addElementToGroupTest() {
    this.controller.addElementToGroup(this.element, this.group);
    assertTrue(this.group.contains(this.element));
  }

  @Test
  void setGroupNameTest() {
    var groupName = "my favourite group";
    this.controller.setGroupName(this.group, groupName);
    assertEquals(groupName, this.group.getName());
  }

  @Test
  void toggleSegmentSelectionTest() {
    Segment segment = new Base();

    this.controller.toggleSegmentSelection(segment);
    Assertions.assertTrue(this.selectionBuffer.getSelectedSegments().contains(segment));
    this.controller.toggleSegmentSelection(segment);
    Assertions.assertFalse(this.selectionBuffer.getSelectedSegments().contains(segment));
  }

  @Test
  void addSegmentSelectionTest() {
    Segment segment = new Base();

    this.controller.addSegmentSelection(segment);
    Assertions.assertTrue(this.selectionBuffer.getSelectedSegments().contains(segment));
  }

  @Test
  void removeSegmentSelectionTest() {
    Segment segment = new Base();

    this.controller.addSegmentSelection(segment);
    Assertions.assertTrue(this.selectionBuffer.getSelectedSegments().contains(segment));
    this.controller.removeSegmentSelection(segment);
    Assertions.assertFalse(this.selectionBuffer.getSelectedSegments().contains(segment));
  }

  @Test
  void clearSegmentSelectionTest() {
    Segment segment1 = new Base();
    Segment segment2 = new Base();

    this.controller.addSegmentSelection(segment1);
    this.controller.addSegmentSelection(segment2);
    this.controller.clearSegmentSelection();
    Assertions.assertFalse(this.selectionBuffer.getSelectedSegments().contains(segment1));
    Assertions.assertFalse(this.selectionBuffer.getSelectedSegments().contains(segment2));
  }

  @Test
  void subscriptionAndNotifyTest() {
    Segment segment = new Base();
    BiConsumer<Segment, Boolean> consumer = mockConsumer();

    this.controller.addSubscription(consumer);
    this.controller.notifyAddition(segment);
    verify(consumer, Mockito.times(1)).accept(segment, SELECTED);
    this.controller.notifyRemoval(segment);
    verify(consumer, times(1)).accept(segment, UNSELECTED);
    this.controller.removeSubscription(consumer);
    this.controller.notifyAddition(segment);
    verify(consumer, times(1)).accept(segment, SELECTED);
  }

  @SuppressWarnings("unchecked")
  private static BiConsumer<Segment, Boolean> mockConsumer() {
    return mock(BiConsumer.class);
  }

}