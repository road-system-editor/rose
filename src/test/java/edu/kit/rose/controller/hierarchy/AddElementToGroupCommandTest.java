package edu.kit.rose.controller.hierarchy;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.SimpleBox;
import edu.kit.rose.infrastructure.SimpleSortedBox;
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
import org.mockito.Mock;


/**
 * Unit Test for {@link AddElementToGroupCommand}.
 */
class AddElementToGroupCommandTest {
  @Mock
  private Project mockProject;
  @Mock
  private RoadSystem mockRoadSystem;
  @Mock
  private Group fromGroup;
  @Mock
  private Group toGroup;
  private Element element;
  private ArrayList<Element> parentGroupElements;
  private ArrayList<Element> elements;

  @BeforeEach
  public void setUp() {
    this.element = new Base();
    this.elements = new ArrayList<>();
    this.parentGroupElements = new ArrayList<>();
    this.toGroup = mock(Group.class);
    this.fromGroup = mock(Group.class);
    this.mockProject = mock(Project.class);
    this.mockRoadSystem = mock(RoadSystem.class);

    when(this.mockProject.getRoadSystem()).thenReturn(this.mockRoadSystem);
    when(this.mockRoadSystem.getElements()).thenReturn(new SimpleBox<>(List.of(this.fromGroup)));

    when(this.toGroup.getElements()).thenReturn(new SimpleSortedBox<>(this.elements));
    when(this.fromGroup.getElements()).thenReturn(new SimpleSortedBox<>(this.parentGroupElements));
    when(this.fromGroup.isContainer()).thenReturn(true);

    doAnswer(e -> this.elements.add(this.element))
            .when(toGroup).addElement(this.element);
    doAnswer(e -> this.parentGroupElements.add(this.element))
            .when(fromGroup).addElement(this.element);
    doAnswer(e -> this.elements.remove(this.element))
            .when(toGroup).removeElement(this.element);
    doAnswer(e -> this.parentGroupElements.remove(this.element))
            .when(fromGroup).removeElement(this.element);

    this.fromGroup.addElement(this.element);
  }

  @Test
  void execute() {
    ChangeCommand addElementCommand =
            new AddElementToGroupCommand(this.mockProject, this.element, this.toGroup);
    addElementCommand.execute();

    Assertions.assertTrue(this.toGroup.getElements().contains(this.element));
    Assertions.assertFalse(this.fromGroup.getElements().contains(this.element));
  }

  @Test
  void unexecute() {
    ChangeCommand addElementCommand =
            new AddElementToGroupCommand(this.mockProject, this.element, this.toGroup);
    addElementCommand.execute();
    addElementCommand.unexecute();

    Assertions.assertFalse(this.toGroup.getElements().contains(this.element));
    Assertions.assertTrue(this.fromGroup.getElements().contains(this.element));
  }
}