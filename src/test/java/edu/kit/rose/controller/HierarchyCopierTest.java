package edu.kit.rose.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import edu.kit.rose.controller.commons.HierarchyCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
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
import edu.kit.rose.util.AccessorUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HierarchyCopier}.
 */
public class HierarchyCopierTest {
  private ReplacementLog replacementLog;
  private HierarchyCopier copier;

  @BeforeEach
  void beforeEach() {
    var criteriaManager = new CriteriaManager();
    var timeSliceSetting = mock(TimeSliceSetting.class);
    RoadSystem roadSystem = new GraphRoadSystem(criteriaManager, timeSliceSetting);
    criteriaManager.setRoadSystem(roadSystem);

    this.replacementLog = new ReplacementLog();
    this.copier = new HierarchyCopier(this.replacementLog, roadSystem);
  }

  @Test
  void testCopyBase() {
    var original = new Base();
    original.setName("base name");
    original.setComment("base comment");
    original.setConurbation(true);
    original.setLaneCount(3);
    original.setLength(260);
    original.setMaxSpeed(60);
    original.setSlope(10);
    original.move(new Movement(12, 34));
    original.rotate(74);
    // TODO move entrance and exit connector once movable connectors are merged

    var copy = copier.copySegment(original);
    assertNotNull(copy);
    assertSame(SegmentType.BASE, copy.getSegmentType());
    assertEqualAccessors(original, copy);
    assertEqualPositioning(original, copy);
  }

  @Test
  void testCopyEntrance() {
    var original = new Entrance();
    original.setName("entrance name");
    original.setComment("entrance comment");
    original.setConurbation(false);
    original.setLaneCount(2);
    original.setLength(842);
    original.setMaxSpeed(100);
    original.setSlope(11);
    original.move(new Movement(31, 44));
    original.rotate(19);

    var copy = copier.copySegment(original);
    assertNotNull(copy);
    assertSame(SegmentType.ENTRANCE, copy.getSegmentType());
    assertEqualAccessors(original, copy);
    assertEqualPositioning(original, copy);
    assertCorrectReplacement(original, copy);
  }

  @Test
  void testCopyExit() {
    var original = new Entrance();
    original.setName("exit name");
    original.setComment("exit comment");
    original.setConurbation(true);
    original.setLaneCount(1);
    original.setLength(1);
    original.setMaxSpeed(10);
    original.setSlope(1234);
    original.move(new Movement(2, -1));
    original.rotate(10);

    var copy = copier.copySegment(original);
    assertNotNull(copy);
    assertSame(SegmentType.ENTRANCE, copy.getSegmentType());
    assertEqualAccessors(original, copy);
    assertEqualPositioning(original, copy);
    assertCorrectReplacement(original, copy);
  }

  @Test
  void testCopyGroup() {
    var original = new Group();
    original.setName("creative group name");
    original.setComment("some comment");

    var copy = copier.copyGroup(original);
    assertNotNull(copy);
    assertEqualAccessors(original, copy);
    assertCorrectReplacement(original, copy);
  }

  @Test
  void testCopyRecursively() {
    var parent = new Group();
    parent.setName("parent");

    var child = new Group();
    child.setName("child");
    parent.addElement(child);

    var grandChild1 = new Group();
    grandChild1.setName("grandChild");
    child.addElement(grandChild1);

    var grandChild2 = new Base();
    grandChild2.setSlope(2);
    child.addElement(grandChild2);

    var parentCopy = copier.copyGroup(parent);
    assertEqualElement(parent, parentCopy);

    var childCopy = parentCopy.getElements().get(0);
    assertEqualElement(child, childCopy);

    var grandChild1Copy = ((Group) childCopy).getElements().get(0);
    assertEqualElement(grandChild1, grandChild1Copy);

    var grandChild2Copy = ((Group) childCopy).getElements().get(1);
    assertEqualElement(grandChild2, grandChild2Copy);
  }

  void assertEqualElement(Element original, Element copy) {
    assertNotSame(original, copy);
    assertEquals(original.isContainer(), copy.isContainer());
    assertEqualAccessors(original, copy);
    assertCorrectReplacement(original, copy);
  }

  void assertCorrectReplacement(Element original, Element copy) {
    assertSame(copy, this.replacementLog.getCurrentVersion(original));
  }

  void assertEqualPositioning(Segment expected, Segment actual) {
    assertEquals(expected.getCenter(), actual.getCenter());
    assertEquals(expected.getRotation(), actual.getRotation());
  }

  void assertEqualAccessors(Element expected, Element actual) {
    for (var accessor1 : expected.getAttributeAccessors()) {
      var accessor2 = AccessorUtility.findAccessorOfType(actual, accessor1.getAttributeType());
      assertNotNull(accessor2);
      assertEquals(accessor1.getValue(), accessor2.getValue());
    }
  }
}
