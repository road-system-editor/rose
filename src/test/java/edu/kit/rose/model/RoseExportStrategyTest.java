package edu.kit.rose.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseExportStrategy}.
 */
class RoseExportStrategyTest {
  /**
   * Threshold to use for double equality comparisons.
   */
  private static final double THRESHOLD = 0.0001;
  private static final Path INVALID_PATH =
      Path.of("build", "tmp", "invalid-directory", "invalid-import.rose.json");
  private static final Path EXPORT_FILE =
      Path.of("build", "tmp", "rose-export-strategy-test.rose.json");
  private static final Position ZOOM_CENTER_POSITION = new Position(420, 69);
  private static final int ZOOM_LEVEL = 10;
  private static final int BASE_ROTATION = 31;
  private static final Position EXIT_CENTER = new Position(56, 78);
  private static final int EXIT_ROTATION = 93;
  private static final Position ENTRANCE_CENTER = new Position(910, 1112);
  private static final int ENTRANCE_ROTATION = 359;

  Position originalBaseEntrancePosition;
  Position originalBaseExitPosition;
  Position originalBaseCenterPosition;
  Project project;

  @BeforeEach
  void beforeEach() {
    var criteriaManager = mock(CriteriaManager.class);
    when(criteriaManager.getCriteria()).thenReturn(new RoseSortedBox<>(List.of()));

    RoadSystem rs = new GraphRoadSystem(
        criteriaManager,
        new TimeSliceSetting(15, 10)
    );

    Base base = (Base) rs.createSegment(SegmentType.BASE);
    base.rotate(BASE_ROTATION);
    base.setName("GWBFRStuttgart");
    base.setLength(3000);
    base.setLaneCount(2);
    base.setConurbation(true);
    base.setMaxSpeed(SpeedLimit.NONE);
    base.move(new Movement(12, 34));
    base.getEntry().move(new Movement(30, -60));
    base.getExit().move(new Movement(-40, 15));
    this.originalBaseEntrancePosition = base.getEntry().getPosition();
    this.originalBaseExitPosition = base.getExit().getPosition();
    this.originalBaseCenterPosition = base.getCenter();

    Exit exit = (Exit) rs.createSegment(SegmentType.EXIT);
    exit.move(toMovement(EXIT_CENTER));
    exit.rotate(EXIT_ROTATION);
    exit.setName("AusfahrtKarlsbadFRStuttgart");
    exit.setLength(250);
    exit.setLaneCount(2);
    exit.setSlope(2.0);
    exit.setConurbation(true);
    exit.setMaxSpeed(SpeedLimit.NONE);
    exit.setMaxSpeedRamp(SpeedLimit.SBA);

    Entrance entrance = (Entrance) rs.createSegment(SegmentType.ENTRANCE);
    entrance.move(toMovement(ENTRANCE_CENTER));
    entrance.rotate(ENTRANCE_ROTATION);
    entrance.setName("EinfahrtKarlsbadFRStuttgart");
    entrance.setLength(250);
    entrance.setLaneCount(2);
    entrance.setSlope(2.0);
    entrance.setConurbation(true);
    entrance.setMaxSpeed(SpeedLimit.NONE);
    entrance.setMaxSpeedRamp(SpeedLimit.SBA);

    Group g = rs.createGroup(Set.of(entrance, exit));
    g.setName("Autobahnkreuz");

    rs.connectConnectors(base.getExit(), exit.getEntry());
    rs.connectConnectors(base.getEntry(), entrance.getExit());
    rs.connectConnectors(exit.getExit(), entrance.getEntry());
    rs.connectConnectors(entrance.getRamp(), exit.getRamp());

    var zoomSetting = new ZoomSetting(ZOOM_CENTER_POSITION);
    zoomSetting.setZoomLevel(ZOOM_LEVEL);

    project = mock(Project.class);
    when(project.getZoomSetting()).thenReturn(zoomSetting);
    when(project.getRoadSystem()).thenReturn(rs);
  }

  @Disabled("base segments are not placed correctly as center updates are not accounted for") //TODO
  @Test
  void testReImportProject() {
    var exportStrategy = new RoseExportStrategy(project);
    assertTrue(exportStrategy.exportToFile(EXPORT_FILE.toFile()));

    assertTrue(Files.exists(EXPORT_FILE));

    var criteriaManager = mock(CriteriaManager.class);
    when(criteriaManager.getCriteria()).thenReturn(new RoseSortedBox<>());


    Project imported = mock(Project.class);
    var roadSystem = new GraphRoadSystem(criteriaManager, new TimeSliceSetting());
    when(imported.getRoadSystem()).thenReturn(roadSystem);
    var zoomSetting = new ZoomSetting(new Position(1500, 1500));
    when(imported.getZoomSetting()).thenReturn(zoomSetting);

    assertTrue(RoseExportStrategy.importToProject(imported, EXPORT_FILE.toFile()));
    assertEquals(4, roadSystem.getElements().getSize());

    Base baseSegment = null;
    Exit exitSegment = null;
    Entrance entranceSegment = null;
    Group group = null;
    for (var element : roadSystem.getElements()) {
      if (element.isContainer()) {
        group = (Group) element;
      } else {
        var type = ((Segment) element).getSegmentType();
        if (type == SegmentType.BASE) {
          baseSegment = (Base) element;
        } else if (type == SegmentType.EXIT) {
          exitSegment = (Exit) element;
        } else if (type == SegmentType.ENTRANCE) {
          entranceSegment = (Entrance) element;
        }
      }
    }

    assertNotNull(baseSegment);
    assertEquals("GWBFRStuttgart", baseSegment.getName());
    assertEquals(3000, baseSegment.getLength());
    assertEquals(BASE_ROTATION, baseSegment.getRotation());
    assertEqualPositionWithThreshold(this.originalBaseCenterPosition, baseSegment.getCenter());
    assertEqualPositionWithThreshold(
        this.originalBaseEntrancePosition, baseSegment.getEntry().getPosition());
    assertEqualPositionWithThreshold(
        this.originalBaseExitPosition, baseSegment.getExit().getPosition());


    assertNotNull(exitSegment);
    assertEquals("AusfahrtKarlsbadFRStuttgart", exitSegment.getName());
    assertEquals(250, exitSegment.getLength());
    assertEquals(2, exitSegment.getSlope(), THRESHOLD);
    assertEquals(SpeedLimit.SBA, exitSegment.getMaxSpeedRamp());
    assertEqualPositionWithThreshold(EXIT_CENTER, exitSegment.getCenter());
    assertEquals(EXIT_ROTATION, exitSegment.getRotation());


    assertNotNull(entranceSegment);
    assertEquals("EinfahrtKarlsbadFRStuttgart", entranceSegment.getName());
    assertEquals(250, entranceSegment.getLength());
    assertEquals(2, entranceSegment.getSlope(), THRESHOLD);
    assertEquals(SpeedLimit.SBA, entranceSegment.getMaxSpeedRamp());
    assertEqualPositionWithThreshold(ENTRANCE_CENTER, entranceSegment.getCenter());
    assertEquals(ENTRANCE_ROTATION, entranceSegment.getRotation());

    assertNotNull(group);
    assertEquals("Autobahnkreuz", group.getName());
    assertEquals(2, group.getElements().getSize());

    assertEquals(2, roadSystem.getConnections(baseSegment).getSize());
    // TODO assertEquals(3, roadSystem.getConnections(exitSegment).getSize());
    // TODO assertEquals(3, roadSystem.getConnections(entranceSegment).getSize());
    var connection = roadSystem.getConnections(baseSegment).iterator().next();
    // TODO check whether connection is between the correct ends

    assertEquals(ZOOM_CENTER_POSITION, imported.getZoomSetting().getCenterOfView());
    assertEquals(ZOOM_LEVEL, imported.getZoomSetting().getZoomLevel(), THRESHOLD);
  }

  @Test
  void testImportInvalidPath() {
    assertFalse(RoseExportStrategy.importToProject(this.project, INVALID_PATH.toFile()));
    assertFalse(Files.exists(INVALID_PATH));
  }

  private static Movement toMovement(Position position) {
    return new Movement(position.getX(), position.getY());
  }

  private static void assertEqualPositionWithThreshold(Position expected, Position actual) {
    assertEquals(expected.getX(), actual.getX(), THRESHOLD);
    assertEquals(expected.getY(), actual.getY(), THRESHOLD);
  }
}
