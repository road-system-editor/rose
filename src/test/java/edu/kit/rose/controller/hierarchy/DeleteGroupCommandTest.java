package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.infrastructure.SimpleSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.SimpleProject;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link DeleteGroupCommand}.
 */
class DeleteGroupCommandTest {
  private static final int EXPECTED_NUMBER_OF_ELEMENTS = 2;
  private static final String UNSETED = "unseted";
  private static final String SETED = "seted";
  private List<Element> elements;
  private Element element1;
  private Element element2;
  private RoadSystem roadSystem;
  private CriteriaManager criteriaManager;
  private Group mockGroup;
  private Group group;
  private Project project;
  private AttributeAccessor<String> accessor;
  private String name;


  @BeforeEach
  public void setUp() {
    this.name = UNSETED;
    this.elements = new ArrayList<>();
    this.element1 = new Base();
    this.element2 = new Base();
    this.elements.add(element1);
    this.elements.add(element2);
    this.criteriaManager = new CriteriaManager();
    this.name = UNSETED;
    this.mockGroup = new Group() {
      private final List<Element> auxElements = new ArrayList<>();

      @Override
      public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
        List<AttributeAccessor<?>> list = new ArrayList<>();
        list.add(accessor);
        return new SimpleSortedBox<>(list);
      }

      @Override
      public SortedBox<Element> getElements() {
        return new SimpleSortedBox<>(this.auxElements);
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

    this.accessor = new AttributeAccessor<>() {
      @Override
      public AttributeType getAttributeType() {
        return AttributeType.NAME;
      }

      @Override
      public void setValue(String str) {
        name = SETED;
      }

      @Override
      public String getValue() {
        return name;
      }
    };

    this.project = new SimpleProject(criteriaManager) {
      @Override
      public RoadSystem getRoadSystem() {
        return roadSystem;
      }
    };

    this.roadSystem = new GraphRoadSystem(this.criteriaManager, new TimeSliceSetting()) {
      @Override
      public Group createGroup(Collection<Element> includedElements) {
        mockGroup.removeElement(element1);
        mockGroup.removeElement(element2);
        group = mockGroup;
        includedElements.forEach(group::addElement);
        return group;
      }

      @Override
      public void removeElement(Element element) {
        group = null;
      }
    };
  }

  @Test
  void execute() {
    DeleteGroupCommand command = new DeleteGroupCommand(this.project, this.group);
    command.execute();
    Assertions.assertNull(this.group);
  }

  @Test
  void unexecute() {
    DeleteGroupCommand command = new DeleteGroupCommand(this.project, this.group);
    command.execute();
    command.unexecute();

    Assertions.assertEquals(this.mockGroup, this.group);
    // checks if the unexecute method sets the same name as on removed group
    Assertions.assertEquals(SETED, this.name);

    SortedBox<Element> groupElements = this.group.getElements();
    SortedBox<Element> expectedElements = new SimpleSortedBox<>(this.elements);

    Assertions.assertEquals(EXPECTED_NUMBER_OF_ELEMENTS, groupElements.getSize());

    Iterator<Element> groupIterator = groupElements.iterator();

    // checks if the elements from deleted group where added to this group
    for (Element expectedElement : expectedElements) {
      Assertions.assertEquals(expectedElement, groupIterator.next());
    }
  }
}