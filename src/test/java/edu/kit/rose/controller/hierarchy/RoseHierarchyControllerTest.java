package edu.kit.rose.controller.hierarchy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.RoseSelectionBuffer;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.util.RoadSystemUtility;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link RoseHierarchyController}.
 */
class RoseHierarchyControllerTest {
  private static final Path CONFIG_PATH = Path.of("build/tmp/no-config.json");

  private RoadSystem roadSystem;
  private Group group;
  private Element element;

  private RoseHierarchyController controller;

  @BeforeEach
  public void setUp() {
    var modelFactory = new ModelFactory(CONFIG_PATH);
    Project project = modelFactory.createProject();
    this.roadSystem = project.getRoadSystem();
    this.element = roadSystem.createSegment(SegmentType.BASE);
    this.group = roadSystem.createGroup(Set.of());

    this.controller = new RoseHierarchyController(new RoseStorageLock(),
            new RoseChangeCommandBuffer(), new RoseSelectionBuffer(), project,
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
}