package edu.kit.rose.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseExportStrategy}.
 */
public class RoseExportStrategyTest {
  private static final Path EXPORT_FILE = Path.of("build/tmp/rose-export-strategy-test.rose.json");
  private static final Position ZOOM_CENTER_POSITION = new Position(420, 69);
  private static final int ZOOM_LEVEL = 10;
  private static final Position BASE_CENTER = new Position(12, 34);
  private static final int BASE_ROTATION = 31;
  private static final Position EXIT_CENTER = new Position(56, 78);
  private static final int EXIT_ROTATION = 93;

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
    base.move(toMovement(BASE_CENTER));
    base.rotate(BASE_ROTATION);
    setAttributeValue(base, AttributeType.NAME, "GWBFRStuttgart");
    setAttributeValue(base, AttributeType.LENGTH, 3000);
    setAttributeValue(base, AttributeType.LANE_COUNT, 2);
    setAttributeValue(base, AttributeType.CONURBATION, true);
    setAttributeValue(base, AttributeType.MAX_SPEED, -1);

    Exit exit = (Exit) rs.createSegment(SegmentType.EXIT);
    exit.move(toMovement(EXIT_CENTER));
    exit.rotate(EXIT_ROTATION);
    setAttributeValue(exit, AttributeType.NAME, "AusfahrtKarlsbadFRStuttgart");
    setAttributeValue(exit, AttributeType.LENGTH, 250);
    setAttributeValue(exit, AttributeType.LANE_COUNT, 2);
    setAttributeValue(exit, AttributeType.SLOPE, 2);
    setAttributeValue(exit, AttributeType.CONURBATION, true);
    setAttributeValue(exit, AttributeType.MAX_SPEED, -1);
    setAttributeValue(exit, AttributeType.MAX_SPEED_RAMP, 60);

    rs.connectConnectors(base.getExit(), exit.getEntry());

    var zoomSetting = new ZoomSetting();
    zoomSetting.setCenterOfView(ZOOM_CENTER_POSITION);
    zoomSetting.setZoomLevel(ZOOM_LEVEL);

    project = mock(Project.class);
    when(project.getZoomSetting()).thenReturn(zoomSetting);
    when(project.getRoadSystem()).thenReturn(rs);
  }

  @Test
  void testExportProject() throws IOException {
    new RoseExportStrategy(project).exportToFile(EXPORT_FILE.toFile());

    assertTrue(Files.exists(EXPORT_FILE));

    var criteriaManager = mock(CriteriaManager.class);
    when(criteriaManager.getCriteria()).thenReturn(new RoseSortedBox<>());


    Project imported = mock(Project.class);
    var roadSystem = new GraphRoadSystem(criteriaManager, new TimeSliceSetting());
    when(imported.getRoadSystem()).thenReturn(roadSystem);
    var zoomSetting = new ZoomSetting();
    when(imported.getZoomSetting()).thenReturn(zoomSetting);

    RoseExportStrategy.importToProject(imported, EXPORT_FILE.toFile());
    assertEquals(2, roadSystem.getElements().getSize());

    Base baseSegment = null;
    Exit exitSegment = null;
    for (var element : roadSystem.getElements()) {
      assertFalse(element.isContainer());

      var type = ((Segment) element).getSegmentType();
      if (type == SegmentType.BASE) {
        baseSegment = (Base) element;
      } else if (type == SegmentType.EXIT) {
        exitSegment = (Exit) element;
      }
    }

    assertNotNull(baseSegment);
    assertEquals("GWBFRStuttgart", baseSegment.getName());
    assertEquals(3000, RoseExportStrategyTest
        .<Integer>getAttributeValue(baseSegment, AttributeType.LENGTH));
    assertEquals(BASE_CENTER, baseSegment.getCenter());
    assertEquals(BASE_ROTATION, baseSegment.getRotation());
    // TODO assert movable connector positions once that is merged into main


    assertNotNull(exitSegment);
    assertEquals("AusfahrtKarlsbadFRStuttgart", exitSegment.getName());
    assertEquals(250, RoseExportStrategyTest
        .<Integer>getAttributeValue(exitSegment, AttributeType.LENGTH));
    assertEquals(2, RoseExportStrategyTest
        .<Integer>getAttributeValue(exitSegment, AttributeType.SLOPE));
    assertEquals(60, RoseExportStrategyTest
        .<Integer>getAttributeValue(exitSegment, AttributeType.MAX_SPEED_RAMP));
    assertEquals(EXIT_CENTER, exitSegment.getCenter());
    assertEquals(EXIT_ROTATION, exitSegment.getRotation());

    assertEquals(1, roadSystem.getConnections(baseSegment).getSize());
    var connection = roadSystem.getConnections(baseSegment).iterator().next();
    // TODO check whether connection is between the correct ends

    assertEquals(ZOOM_CENTER_POSITION, imported.getZoomSetting().getCenterOfView());
    assertEquals(ZOOM_LEVEL, imported.getZoomSetting().getZoomLevel());
  }

  @SuppressWarnings("unchecked")
  private static <T> T getAttributeValue(Element element, AttributeType type) {
    for (var accessor : element.getAttributeAccessors()) {
      if (accessor.getAttributeType() == type) {
        return ((AttributeAccessor<T>) accessor).getValue();
      }
    }

    fail("missing attribute");
    return null;
  }

  @Test
  void testImportProject() {
  }

  @SuppressWarnings("unchecked")
  static <T> void setAttributeValue(Segment segment, AttributeType type, T value) {
    for (var acc : segment.getAttributeAccessors()) {
      if (acc.getAttributeType() == type) {
        ((AttributeAccessor<T>) acc).setValue(value);
        return;
      }
    }
    throw new RuntimeException();
  }

  private static Movement toMovement(Position position) {
    return new Movement(position.getX(), position.getY());
  }
}
