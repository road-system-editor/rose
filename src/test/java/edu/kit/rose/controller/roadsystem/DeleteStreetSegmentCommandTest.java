package edu.kit.rose.controller.roadsystem;

import static edu.kit.rose.util.AccessorUtility.assertEqualAccessors;
import static edu.kit.rose.util.RoadSystemUtility.findAnySegmentOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link DeleteStreetSegmentCommand} class.
 */
public class DeleteStreetSegmentCommandTest {
  private static final Path CONFIG_PATH = Path.of("build/tmp/no-config.json");

  private Project project;
  private RoadSystem roadSystem;
  private ReplacementLog replacementLog;

  private Base segmentToDelete;

  private DeleteStreetSegmentCommand command;

  /**
   * Sets up all mock objects.
   */
  @BeforeEach
  public void setUp() throws IOException {
    Files.deleteIfExists(CONFIG_PATH);
    var modelFactory = new ModelFactory(CONFIG_PATH);

    this.project = modelFactory.createProject();
    this.roadSystem = this.project.getRoadSystem();
    this.replacementLog = new ReplacementLog();
    this.segmentToDelete = (Base) this.roadSystem.createSegment(SegmentType.BASE);

    this.command = new DeleteStreetSegmentCommand(this.replacementLog, this.project,
        List.of(this.segmentToDelete));
  }

  @Test
  public void testConstructor() {
    assertThrows(NullPointerException.class, () -> new DeleteStreetSegmentCommand(
        null, this.project, List.of(this.segmentToDelete)));
    assertThrows(NullPointerException.class, () -> new DeleteStreetSegmentCommand(
        this.replacementLog, null, List.of(this.segmentToDelete)));
    assertThrows(NullPointerException.class, () -> new DeleteStreetSegmentCommand(
        this.replacementLog, this.project, null));
    assertThrows(NullPointerException.class, () -> new DeleteStreetSegmentCommand(
        null, null, null));
  }

  @Test
  public void testSegmentContainedInRoadSystem() {
    assumeTrue(this.roadSystem.getElements().contains(segmentToDelete));

    command.execute();
    assertFalse(this.roadSystem.getElements().contains(segmentToDelete));
    assertNull(findAnySegmentOfType(this.roadSystem, SegmentType.BASE));

    command.unexecute();
    assertNotNull(findAnySegmentOfType(this.roadSystem, SegmentType.BASE));
  }

  @Test
  void testRecreatedSegmentIsEqualToDeleted() {
    this.segmentToDelete.setName("some name");
    this.segmentToDelete.setComment("insightful comment on the structure of this segment");
    this.segmentToDelete.setSlope(99.0);
    this.segmentToDelete.setLaneCount(2);
    this.segmentToDelete.move(new Movement(1021, 313));

    command.execute();
    command.unexecute();
    Base recreated = findAnySegmentOfType(this.roadSystem, SegmentType.BASE);
    assertNotNull(recreated);
    assertEquals(segmentToDelete.getSegmentType(), recreated.getSegmentType());
    assertEqualAccessors(this.segmentToDelete, recreated);
    assertEquals(segmentToDelete.getCenter(), recreated.getCenter());
  }

  @Test
  void testParent() {
    Group parent = this.roadSystem.createGroup(Set.of(this.segmentToDelete));
    assertEquals(1, parent.getElements().getSize());
    command.execute();
    command.unexecute();
    assertEquals(1, parent.getElements().getSize());
  }

  /**
   * Tests whether {@link DeleteStreetSegmentCommand#unexecute()} logs the re-creation of the
   * deleted segment to the replacement log.
   */
  @Test
  void testLogsReplacement() {
    command.execute();
    command.unexecute();
    Base recreatedSegment1 = findAnySegmentOfType(this.roadSystem, SegmentType.BASE);
    assertNotNull(recreatedSegment1);
    assertSame(recreatedSegment1, this.replacementLog.getCurrentVersion(this.segmentToDelete));

    command.execute();
    command.unexecute();
    Base recreatedSegment2 = findAnySegmentOfType(this.roadSystem, SegmentType.BASE);
    assertNotNull(recreatedSegment2);
    assertSame(recreatedSegment2, this.replacementLog.getCurrentVersion(this.segmentToDelete));
    assertSame(recreatedSegment2, this.replacementLog.getCurrentVersion(recreatedSegment1));
  }

  @Test
  void testConsidersReplacement() {
    Group parent = this.roadSystem.createGroup(Set.of(this.segmentToDelete));
    assumeTrue(parent.contains(segmentToDelete));

    // test with segment replacement
    Segment segmentToDeleteReplacement = this.roadSystem.createSegment(SegmentType.BASE);
    this.roadSystem.removeElement(this.segmentToDelete);
    parent.removeElement(segmentToDelete);
    parent.addElement(segmentToDeleteReplacement);
    this.replacementLog.replaceElement(this.segmentToDelete, segmentToDeleteReplacement);
    System.out.println(this.roadSystem.getElements().contains(parent));

    assumeTrue(this.roadSystem.getElements().contains(segmentToDeleteReplacement));
    command.execute();
    assertFalse(this.roadSystem.getElements().contains(segmentToDeleteReplacement));
    System.out.println(this.roadSystem.getElements().contains(parent));

    // test with parent replacement
    Group parentReplacement = this.roadSystem.createGroup(Set.of());
    System.out.println(this.roadSystem.getElements().contains(parent));
    this.roadSystem.removeElement(parent);
    this.replacementLog.replaceElement(parent, parentReplacement);

    command.unexecute();
    Base recreatedSegment = findAnySegmentOfType(this.roadSystem, SegmentType.BASE);
    assertNotNull(recreatedSegment);
    assertTrue(parentReplacement.contains(recreatedSegment));
  }
}
