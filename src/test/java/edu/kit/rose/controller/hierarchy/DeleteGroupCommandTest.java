package edu.kit.rose.controller.hierarchy;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;


/**
 * Unit test for {@link DeleteGroupCommand}.
 */
class DeleteGroupCommandTest {
  private static final int EXPECTED_NUMBER_OF_ELEMENTS = 2;
  @Mock
  private RoadSystem roadSystem;
  @Mock
  private Project project;
  private static final String UNSET = "unset";
  private static final String SET = "set";
  private List<Element> elements;
  private Element element1;
  private Element element2;
  private Group mockGroup;
  private Group group;
  private AttributeAccessor<String> accessor;
  private String name;


  @BeforeEach
  public void setUp() {
    this.name = UNSET;
    this.elements = new ArrayList<>();
    this.element1 = new Base();
    this.element2 = new Base();
    this.elements.add(element1);
    this.elements.add(element2);
    this.name = UNSET;
    this.project = mock(Project.class);
    this.roadSystem = mock(RoadSystem.class);

    when(project.getRoadSystem()).thenReturn(this.roadSystem);
    this.mockGroup = new Group() {
      private final List<Element> auxElements = new ArrayList<>();

      @Override
      public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
        List<AttributeAccessor<?>> list = new ArrayList<>();
        list.add(accessor);
        return new RoseSortedBox<>(list);
      }

      @Override
      public SortedBox<Element> getElements() {
        return new RoseSortedBox<>(this.auxElements);
      }

      @Override
      public void addElement(Element element) {
        this.auxElements.add(element);
      }

      @Override
      public void removeElement(Element e) {
        this.auxElements.remove(e);
      }
    };

    this.group = this.mockGroup;
    this.group.addElement(element1);
    this.group.addElement(element2);

    this.accessor = new AttributeAccessor<String>(AttributeType.NAME,
            mock(Supplier.class), mock(Consumer.class)) {
      @Override
      public AttributeType getAttributeType() {
        return AttributeType.NAME;
      }

      @Override
      public void setValue(String str) {
        name = SET;
      }

      @Override
      public String getValue() {
        return name;
      }
    };

    doAnswer(e -> {
      mockGroup.removeElement(element1);
      mockGroup.removeElement(element2);
      group = mockGroup;
      HashSet<Element> set = e.getArgument(0);
      set.forEach(group::addElement);
      return group;
    }).when(this.roadSystem).createGroup(ArgumentMatchers.any());

    doAnswer(e -> this.group = null).when(this.roadSystem).removeElement(any());
  }

  @Test
  void testExecute() {
    DeleteGroupCommand command = new DeleteGroupCommand(this.project, this.group);
    command.execute();
    Assertions.assertNull(this.group);
  }

  @Test
  void testUnexecute() {
    DeleteGroupCommand command = new DeleteGroupCommand(this.project, this.group);
    command.execute();
    command.unexecute();

    Assertions.assertEquals(this.mockGroup, this.group);
    // checks if the unexecute method sets the same name as on removed group
    Assertions.assertEquals(SET, this.name);

    SortedBox<Element> groupElements = this.group.getElements();
    var groupElementsList = new LinkedList<Element>();
    groupElements.forEach(groupElementsList::add);

    Assertions.assertEquals(EXPECTED_NUMBER_OF_ELEMENTS, groupElements.getSize());
    Assertions.assertTrue(this.elements.containsAll(groupElementsList));
    Assertions.assertTrue(groupElementsList.containsAll(elements));
  }
}