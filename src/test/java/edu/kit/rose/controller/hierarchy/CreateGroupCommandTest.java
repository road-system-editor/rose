package edu.kit.rose.controller.hierarchy;


import edu.kit.rose.model.Project;
import edu.kit.rose.model.SimpleProject;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
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
  private RoadSystem roadSystem;
  private CriteriaManager criteriaManager;
  private Group group;
  private Project project;

  @BeforeEach
  public void setUp() {
    this.group = null;
    this.criteriaManager = new CriteriaManager();
    this.project = new SimpleProject(criteriaManager) {
      @Override
      public RoadSystem getRoadSystem() {
        return roadSystem;
      }
    };
    this.roadSystem = new GraphRoadSystem(criteriaManager, new TimeSliceSetting()) {

      @Override
      public Group createGroup(Collection<Element> includedElements) {
        group = new Group();
        return group;
      }

      @Override
      public void removeElement(Element element) {
        group = null;
      }
    };
  }

  @Test
  void executeTest() {
    List<Element> elements = new ArrayList<>();
    CreateGroupCommand command = new CreateGroupCommand(this.project, elements);
    command.execute();
    Assertions.assertNotEquals(null, this.group);
  }

  @Test
  public void unexecuteTest() {
    List<Element> elements = new ArrayList<>();
    CreateGroupCommand command = new CreateGroupCommand(this.project, elements);
    command.execute();
    command.unexecute();
    Assertions.assertNull(this.group);
  }
}