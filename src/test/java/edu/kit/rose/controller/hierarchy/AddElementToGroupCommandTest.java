package edu.kit.rose.controller.hierarchy;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit Test for {@link AddElementToGroupCommand}.
 */
class AddElementToGroupCommandTest {
  private List<Element> roadSystemElements;
  private Project mockProject;
  private Group fromGroup;
  private Group toGroup;
  private Element element;

  private ReplacementLog replacementLog;
  private AddElementToGroupCommand command;

  @BeforeEach
  void setUp() {
    this.replacementLog = new ReplacementLog();
    this.element = new Base();
    this.toGroup = new Group();
    this.fromGroup = new Group();
    this.mockProject = mock(Project.class);
    this.roadSystemElements = new ArrayList<>();
    this.roadSystemElements.add(this.fromGroup);
    this.roadSystemElements.add(this.element);

    RoadSystem mockRoadSystem = mock(RoadSystem.class);
    when(this.mockProject.getRoadSystem()).thenReturn(mockRoadSystem);
    when(mockRoadSystem.getElements()).thenAnswer(stub -> new RoseBox<>(this.roadSystemElements));

    this.fromGroup.addElement(this.element);

    this.command = new AddElementToGroupCommand(
        this.replacementLog, this.mockProject, this.element, this.toGroup);
  }

  @Test
  void testConstructor() {
    assertThrows(NullPointerException.class, () -> new AddElementToGroupCommand(
        null, this.mockProject, this.element, this.toGroup));
    assertThrows(NullPointerException.class, () -> new AddElementToGroupCommand(
        this.replacementLog, null, this.element, this.toGroup));
    assertThrows(NullPointerException.class, () -> new AddElementToGroupCommand(
        this.replacementLog, this.mockProject, null, this.toGroup));
    assertThrows(NullPointerException.class, () -> new AddElementToGroupCommand(
        this.replacementLog, this.mockProject, this.element, null));
  }

  @Test
  void testExecute() {
    command.execute();

    assertTrue(this.toGroup.getElements().contains(this.element));
    Assertions.assertFalse(this.fromGroup.getElements().contains(this.element));
  }

  @Test
  void testUnExecute() {
    command.execute();
    command.unexecute();

    Assertions.assertFalse(this.toGroup.getElements().contains(this.element));
    assertTrue(this.fromGroup.getElements().contains(this.element));
  }

  @Test
  void testConsidersParentReplacement() {
    Group newFromGroup = new Group();
    newFromGroup.addElement(element);
    simulateReplace(fromGroup, newFromGroup);

    command.execute();
    Assertions.assertFalse(newFromGroup.contains(element));

    command.unexecute();
    assertTrue(newFromGroup.contains(element));
  }

  @Test
  void testConsidersElementReplacement() {
    Element newElement = new Base();
    fromGroup.removeElement(element);
    fromGroup.addElement(newElement);
    simulateReplace(element, newElement);

    command.execute();
    Assertions.assertFalse(fromGroup.contains(newElement));

    command.unexecute();
    assertTrue(fromGroup.contains(newElement));
  }

  private void simulateReplace(Element oldElement, Element newElement) {
    this.replacementLog.replaceElement(oldElement, newElement);
    if (this.roadSystemElements.remove(oldElement)) {
      this.roadSystemElements.add(newElement);
    }
  }
}