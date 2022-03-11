package edu.kit.rose.controller.hierarchy;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit Test for {@link CreateGroupCommand}.
 */
class CreateGroupCommandTest {
  /**
   * to be created group.
   */
  private Group group;
  private Group newGroup;
  /**
   * parent1 and parent2 are the groups, which the
   * roadsystem contains at the beginning of testing.
   * Each of this parent contains and element.
   */
  private Group parent1;
  private Group parent2;

  /**
   * The root group of the roadsystem.
   */
  private Group rootGroup;

  /**
   * The elements that the roadsystem contains.
   */
  private ArrayList<Element> roadElements;
  private Segment element1;
  private Segment element2;

  private ReplacementLog replacementLog;

  /**
   * Test subject.
   */
  private CreateGroupCommand command;

  @SuppressWarnings("unchecked")
  @BeforeEach
  public void setUp() {
    this.roadElements = new ArrayList<>();

    this.group = new Group();
    this.parent1 = new Group();
    this.parent2 = new Group();
    this.rootGroup = new Group();

    this.element1 = new Base();
    this.element2 = new Exit();

    this.roadElements.add(this.parent1);
    this.roadElements.add(this.parent2);

    Project project = mock(Project.class);
    RoadSystem roadSystem = mock(RoadSystem.class);
    when(project.getRoadSystem()).thenReturn(roadSystem);
    when(roadSystem.getElements()).thenReturn(new RoseBox<>(this.roadElements));
    when(roadSystem.getRootGroup()).thenReturn(this.rootGroup);

    this.parent1.addElement(this.element1);
    this.parent2.addElement(this.element2);

    when(roadSystem.createGroup(any()))
        .thenAnswer(e -> {
          this.group = new Group();

          for (Element element : (Collection<Element>) e.getArgument(0)) {
            this.group.addElement(element);
          }
          this.roadElements.add(this.group);
          return this.group;
        })
        .thenAnswer(e -> {
          this.newGroup = new Group();

          for (Element element : (Collection<Element>) e.getArgument(0)) {
            this.newGroup.addElement(element);
          }
          this.roadElements.add(this.newGroup);
          return this.newGroup;
        })
        .thenThrow(new RuntimeException());

    doAnswer(e -> this.roadElements.remove((Element) e.getArgument(0)))
            .when(roadSystem).removeElement(any(Element.class));

    this.replacementLog = new ReplacementLog();
    this.command = new CreateGroupCommand(
        this.replacementLog,
        project,
        List.of(element1, element2));
  }

  /**
   * Tests if the execute command does remove the elements
   * from previous parents and that the created group contains this elements.
   */
  @Test
  void testExecuteTest() {
    command.execute();

    Assertions.assertTrue(this.group.getElements().contains(this.element1));
    Assertions.assertTrue(this.group.getElements().contains(this.element1));

    Assertions.assertFalse(this.parent1.getElements().contains(this.element1));
    Assertions.assertFalse(this.parent2.getElements().contains(this.element2));
  }

  /**
   * Tests if the unexecute adds the elemets back to its
   * previous parents and checks if the group is removed from the roadsystem.
   */
  @Test
  public void testUnexecuteTest() {
    command.execute();
    command.unexecute();

    Assertions.assertFalse(this.roadElements.contains(this.group));

    Assertions.assertTrue(this.parent1.getElements().contains(this.element1));
    Assertions.assertTrue(this.parent2.getElements().contains(this.element2));
  }

  /**
   * Tests whether the command logs the group replacement to the replacement log on a redo action.
   */
  @Test
  public void testRedoReplacement() {
    command.execute();
    command.unexecute();
    command.execute();

    assertSame(newGroup, replacementLog.getCurrentVersion(group));
  }

  /**
   * Tests whether undo considers replacements when adding the contained elements back to the
   * parent.
   */
  @Test
  public void testConsidersReplacements() {
    // simulate that parent1 has been replaced with parentReplacement1
    var parentReplacement1 = new Group();
    parentReplacement1.addElement(element1);
    simulateReplacement(parent1, parentReplacement1);

    command.execute();

    Assertions.assertTrue(this.group.getElements().contains(this.element1));
    Assertions.assertTrue(this.group.getElements().contains(this.element2));

    Assertions.assertFalse(parentReplacement1.getElements().contains(this.element1));
    Assertions.assertFalse(this.parent2.getElements().contains(this.element2));

    // simulate that element1 has been replaced with elementReplacement1 and that the group
    // itself has been replaced with groupReplacement
    var elementReplacement1 = new Base();
    simulateReplacement(element1, elementReplacement1);
    var groupReplacement = new Group();
    simulateReplacement(group, groupReplacement);

    command.unexecute();

    Assertions.assertFalse(this.roadElements.contains(group));
    Assertions.assertFalse(this.roadElements.contains(groupReplacement));

    Assertions.assertTrue(parentReplacement1.getElements().contains(elementReplacement1));
    Assertions.assertTrue(this.parent2.getElements().contains(this.element2));

    command.execute();

    Assertions.assertTrue(this.newGroup.getElements().contains(elementReplacement1));
  }

  private void simulateReplacement(Element element, Element replacement) {
    replacementLog.replaceElement(element, replacement);
    if (this.roadElements.remove(element)) {
      this.roadElements.add(replacement);
    }
  }
}