package edu.kit.rose.controller.hierarchy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import edu.kit.rose.infrastructure.SimpleBox;
import edu.kit.rose.infrastructure.SimpleSortedBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;

/**
 * Unit Test for {@link CreateGroupCommand}.
 */
class CreateGroupCommandTest {
  @Mock
  private Project project;
  @Mock
  private RoadSystem roadSystem;
  /**
   * to be created group.
   */
  @Mock
  private Group group;
  /**
   * parent1 and parent2 are the groups, which the
   * roadsystem contains at the beginning of testing.
   * Each of this parent contains and element.
   */
  @Mock
  private Group parent1;
  @Mock
  private Group parent2;
  /**
   * contains elements of parent1.
   */
  private ArrayList<Element> parent1Elements;
  /**
   * contains elements of parent2.
   */
  private ArrayList<Element> parent2Elements;
  private ArrayList<Element> groupElements;
  /**
   * the elements that should be added to group
   * when createGroup will be called.
   */
  private ArrayList<Element> elements;
  /**
   * The elements that the roadsystem contains.
   */
  private ArrayList<Element> roadElements;
  private Element element1;
  private Element element2;

  @BeforeEach
  public void setUp() {
    this.roadElements = new ArrayList<>();
    this.parent1Elements = new ArrayList<>();
    this.parent2Elements = new ArrayList<>();
    this.groupElements = new ArrayList<>();
    this.elements = new ArrayList<>();

    this.group = mock(Group.class);
    this.parent1 = mock(Group.class);
    this.parent2 = mock(Group.class);
    this.project = mock(Project.class);
    this.roadSystem = mock(RoadSystem.class);

    this.element1 = new Base();
    this.element2 = new Exit();

    this.elements.add(this.element1);
    this.elements.add(this.element2);

    this.roadElements.add(this.parent1);
    this.roadElements.add(this.parent2);

    when(this.project.getRoadSystem()).thenReturn(this.roadSystem);
    when(this.roadSystem.getElements()).thenReturn(new SimpleBox<>(this.roadElements));
    when(this.parent1.getElements()).thenReturn(new SimpleSortedBox<>(this.parent1Elements));
    when(this.parent2.getElements()).thenReturn(new SimpleSortedBox<>(this.parent2Elements));
    when(this.parent1.isContainer()).thenReturn(true);
    when(this.parent2.isContainer()).thenReturn(true);

    doAnswer(e -> this.parent1Elements.add(e.getArgument(0)))
            .when(this.parent1).addElement(any(Element.class));
    doAnswer(e -> this.parent2Elements.add(e.getArgument(0)))
            .when(this.parent2).addElement(any(Element.class));
    doAnswer(e -> this.parent1Elements.remove((Element) e.getArgument(0)))
            .when(this.parent1).removeElement(any(Element.class));
    doAnswer(e -> this.parent2Elements.remove((Element) e.getArgument(0)))
            .when(this.parent2).removeElement(any(Element.class));

    this.parent1.addElement(this.element1);
    this.parent2.addElement(this.element2);

    doAnswer(e -> {
      this.group = mock(Group.class);

      when(this.group.getElements()).thenReturn(new SimpleSortedBox<>(this.groupElements));

      doAnswer(arguments -> this.groupElements.add(arguments.getArgument(0))).
              when(this.group).addElement(any(Element.class));
      doAnswer(arguments -> this.groupElements.remove((Element) arguments.getArgument(0))).
              when(this.group).removeElement(any(Element.class));

      for (Element element : (Collection<Element>) e.getArgument(0)) {
        this.group.addElement(element);
      }
      this.roadElements.add(this.group);
      return this.group;
    }).when(this.roadSystem).createGroup(ArgumentMatchers.<Collection<Element>>any());

    doAnswer(e -> this.roadElements.remove((Element) e.getArgument(0)))
            .when(roadSystem).removeElement(any(Element.class));
  }

  /**
   * Tests if the execute command does remove the elements
   * from previous parents and that the created group contains this elements.
   */
  @Test
  void executeTest() {
    CreateGroupCommand command = new CreateGroupCommand(this.project, this.elements);
    command.execute();

    Assertions.assertTrue(this.group.getElements().contains(this.element1));
    Assertions.assertTrue(this.group.getElements().contains(this.element2));

    Assertions.assertFalse(this.parent1.getElements().contains(this.element1));
    Assertions.assertFalse(this.parent2.getElements().contains(this.element2));
  }

  /**
   * Tests if the unexecute adds the elemets back to its
   * previous parents and checks if the group is removed from the roadsystem.
   */
  @Test
  public void unexecuteTest() {
    CreateGroupCommand command = new CreateGroupCommand(this.project, this.elements);
    command.execute();
    command.unexecute();

    Assertions.assertFalse(this.roadElements.contains(this.group));

    Assertions.assertTrue(this.parent1.getElements().contains(this.element1));
    Assertions.assertTrue(this.parent2.getElements().contains(this.element2));
  }
}