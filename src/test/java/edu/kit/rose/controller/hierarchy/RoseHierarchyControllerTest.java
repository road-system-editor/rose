package edu.kit.rose.controller.hierarchy;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.selection.RoseSelectionBuffer;
import edu.kit.rose.infrastructure.SimpleSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Unit test for {@link RoseHierarchyController}.
 */
class RoseHierarchyControllerTest {
  private static final String UNSET = "unset";
  private static final String SET = "set";
  @Mock
  private Project project;
  @Mock
  private RoadSystem roadSystem;
  private RoseHierarchyController controller;
  private AttributeAccessor<String> nameAccessor;
  private CriteriaManager criteriaManager;
  private Group createMockGroup;
  private Group group;
  private Element element1;
  private Element element2;
  private String name;

  @BeforeEach
  public void setUp() {
    this.name = UNSET;
    this.createMockGroup = null;
    this.project = mock(Project.class);
    this.roadSystem = mock(RoadSystem.class);
    this.element1 = new Base();
    this.criteriaManager = new CriteriaManager();

    when(this.project.getRoadSystem()).thenReturn(this.roadSystem);
    this.controller = new RoseHierarchyController(new RoseStorageLock(),
            new RoseChangeCommandBuffer(), new RoseSelectionBuffer(), this.project);

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

    doAnswer(e -> {
      this.createMockGroup = new Group();
      return this.group;
    }).when(this.roadSystem).createGroup(any());

    doAnswer(e -> {
      if (this.group == e.getArgument(0)) {
        this.group = null;
      }
      return 0;
    }).when(this.roadSystem).removeElement(any());
  }

  @Test
  void createGroupTest() {
    this.controller.createGroup();
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
    this.controller.setGroupName(this.group, SET);
    Assertions.assertEquals(SET, this.name);
  }
}