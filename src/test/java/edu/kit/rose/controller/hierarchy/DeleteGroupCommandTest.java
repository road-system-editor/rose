package edu.kit.rose.controller.hierarchy;

import static edu.kit.rose.util.RoadSystemUtility.findElementByName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link DeleteGroupCommand}.
 */
class DeleteGroupCommandTest {
  private static final String PARENT_NAME = "proud parent";
  private static final String GROUP_NAME = "creative group name";
  private static final String ELEMENT_1_NAME = "some base segment";
  private static final String ELEMENT_2_NAME = "some entrance segment";

  private RoadSystem roadSystem;

  private ReplacementLog replacementLog;

  private DeleteGroupCommand command;

  @BeforeEach
  public void setUp() {
    var criteriaManager = new CriteriaManager();
    this.roadSystem = new GraphRoadSystem(criteriaManager, mock(TimeSliceSetting.class));
    criteriaManager.setRoadSystem(this.roadSystem);

    var element1 = roadSystem.createSegment(SegmentType.BASE);
    element1.setName(ELEMENT_1_NAME);

    var element2 = roadSystem.createSegment(SegmentType.ENTRANCE);
    element2.setName(ELEMENT_2_NAME);

    var group = roadSystem.createGroup(Set.of(element1, element2));
    group.setName(GROUP_NAME);

    var parent = roadSystem.createGroup(Set.of(group));
    parent.setName(PARENT_NAME);

    Project project = mock(Project.class);
    when(project.getRoadSystem()).thenReturn(this.roadSystem);

    this.replacementLog = new ReplacementLog();
    this.command = new DeleteGroupCommand(this.replacementLog, project, group);
  }

  /**
   * Tests whether the command handles the deletion and creation of the group in the road system.
   */
  @Test
  void testGroupContainedInRoadSystem() {
    assertNotNull(group());

    this.command.execute();
    assertNull(group());

    this.command.unexecute();
    assertNotNull(group());
  }

  /**
   * Tests whether {@link DeleteGroupCommand#unexecute()} restores the elements contained in the
   * deleted group.
   */
  @Test
  void testElementsContainedInGroup() {
    assertTrue(group().contains(element1()));
    assertTrue(group().contains(element2()));

    this.command.execute();

    assertNull(element1());
    assertNull(element2());

    this.command.unexecute();

    assertNotNull(group());
    assertNotNull(element1());
    assertNotNull(element2());
    assertTrue(group().contains(element1()));
    assertTrue(group().contains(element1()));
  }

  /**
   * Tests whether {@link DeleteGroupCommand#unexecute()} restores attribute values of the deleted
   * group.
   */
  @Test
  void testAttributes() {
    var groupComment = "funny, creative and insightful comment for the group";
    group().setComment(groupComment);
    var elementComment1 = "boring comment";
    element1().setComment(elementComment1);
    var elementLaneCount2 = 4;
    element2().setLaneCount(elementLaneCount2);

    this.command.execute();
    this.command.unexecute();

    assumeTrue(group() != null);
    assertEquals(GROUP_NAME, group().getName());
    assertEquals(groupComment, group().getComment());

    assumeTrue(element1() != null);
    assertEquals(ELEMENT_1_NAME, element1().getName());
    assertEquals(elementComment1, element1().getComment());

    assumeTrue(element2() != null);
    assertEquals(ELEMENT_2_NAME, element2().getName());
    assertEquals(elementLaneCount2, element2().getLaneCount());
  }

  /**
   * Tests whether the parent of the group is restored in {@link DeleteGroupCommand#unexecute()}.
   */
  @Test
  void testParent() {
    command.execute();
    assertFalse(parent().getElements().contains(group()));
    assertEquals(0, parent().getElements().getSize());


    command.unexecute();
    assertEquals(1, parent().getElements().getSize());
  }

  private Group parent() {
    Element element = findElementByName(this.roadSystem, PARENT_NAME);
    assertTrue(element == null || element.isContainer());
    return (Group) element;
  }

  private Group group() {
    Element element = findElementByName(this.roadSystem, GROUP_NAME);
    assertTrue(element == null || element.isContainer());
    return (Group) element;
  }

  private Base element1() {
    Element element = findElementByName(this.roadSystem, ELEMENT_1_NAME);
    assertTrue(element == null || !element.isContainer());
    Segment segment = (Segment) element;
    assertTrue(segment == null || segment.getSegmentType() == SegmentType.BASE);
    return (Base) segment;
  }

  private Entrance element2() {
    Element element = findElementByName(this.roadSystem, ELEMENT_2_NAME);
    assertTrue(element == null || !element.isContainer());
    Segment segment = (Segment) element;
    assertTrue(segment == null || segment.getSegmentType() == SegmentType.ENTRANCE);
    return (Entrance) segment;
  }
}