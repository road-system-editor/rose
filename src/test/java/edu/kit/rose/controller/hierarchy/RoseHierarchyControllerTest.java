package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.selection.RoseSelectionBuffer;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link RoseHierarchyController}.
 */
class RoseHierarchyControllerTest {
  private static final String UNSETED = "unseted";
  private static final String SETED = "seted";
  private RoseHierarchyController controller;
  private AttributeAccessor<String> nameAccessor;
  private Project project;
  private CriteriaManager criteriaManager;
  private RoadSystem roadSystem;
  private Group createMockGroup;
  private Group group;
  private Element element1;
  private Element element2;
  private String name;

  @BeforeEach
  public void setUp() {
    this.name = UNSETED;
    this.createMockGroup = null;
    this.element1 = new Base();
    this.criteriaManager = new CriteriaManager();
    this.group = new Group() {
      @Override
      public void addElement(Element e) {
        element2 = e;
      }

      @Override
      public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
        ArrayList<AttributeAccessor<?>> list = new ArrayList<>();
        list.add(nameAccessor);
        return new SimpleSortedBox<>(list);
      }
    };

    this.nameAccessor = new AttributeAccessor<>() {
      @Override
      public AttributeType getAttributeType() {
        return AttributeType.NAME;
      }

      @Override
      public void setValue(String str) {
        name = str;
      }

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

    this.controller = new RoseHierarchyController(new RoseStorageLock(), new RoseChangeCommandBuffer(),
            new RoseSelectionBuffer(), this.project);

    this.roadSystem = new GraphRoadSystem(criteriaManager, new TimeSliceSetting()) {

      @Override
      public Group createGroup(Collection<Element> includedElements) {
        createMockGroup = new Group();
        return group;
      }

      @Override
      public void removeElement(Element e) {
        if (group == e) {
          group = null;
        }
      }
    };
  }

  @Test
  void createGroupTest() {
    this.controller.createGroup(new ArrayList<>());
    Assertions.assertNotNull(this.createMockGroup);
  }

  @Test
  void deleteGroupTest() {
    this.controller.deleteGroup(this.group);
    Assertions.assertNull(this.group);
  }

  @Test
  void addElementToGroupTest() {
    this.controller.addElementToGroup(this.element1, this.group);
    // this group mockup assigns to element2 the element that he is becoming in addElement method
    Assertions.assertEquals(this.element1, this.element2);
  }

  @Test
  void setGroupNameTest() {
    this.controller.setGroupName(this.group, SETED);
    Assertions.assertEquals(SETED, this.name);
  }
}