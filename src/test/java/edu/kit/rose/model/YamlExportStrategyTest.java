package edu.kit.rose.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link YamlExportStrategy}.
 */
public class YamlExportStrategyTest {
  private static final Path EXPORT_FILE = Path.of("build/tmp/yaml-export.yml");

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
    setAttributeValue(base, AttributeType.NAME, "GWBFRStuttgart");
    setAttributeValue(base, AttributeType.LENGTH, 3000);
    setAttributeValue(base, AttributeType.LANE_COUNT, 2);
    setAttributeValue(base, AttributeType.CONURBATION, true);
    setAttributeValue(base, AttributeType.MAX_SPEED, -1);

    Exit exit = (Exit) rs.createSegment(SegmentType.EXIT);
    setAttributeValue(exit, AttributeType.NAME, "AusfahrtKarlsbadFRStuttgart");
    setAttributeValue(exit, AttributeType.LENGTH, 250);
    setAttributeValue(exit, AttributeType.LANE_COUNT, 2);
    setAttributeValue(exit, AttributeType.SLOPE, 2);
    setAttributeValue(exit, AttributeType.CONURBATION, true);
    setAttributeValue(exit, AttributeType.MAX_SPEED, -1);
    setAttributeValue(exit, AttributeType.MAX_SPEED_RAMP, 60);

    Entrance entrance = (Entrance) rs.createSegment(SegmentType.ENTRANCE);
    setAttributeValue(entrance, AttributeType.NAME, "EinfahrtKarlsbadFRStuttgart");
    setAttributeValue(entrance, AttributeType.LENGTH, 250);
    setAttributeValue(entrance, AttributeType.LANE_COUNT, 2);
    setAttributeValue(entrance, AttributeType.SLOPE, 2);
    setAttributeValue(entrance, AttributeType.CONURBATION, false);
    setAttributeValue(entrance, AttributeType.MAX_SPEED, -1);
    setAttributeValue(entrance, AttributeType.MAX_SPEED_RAMP, 60);

    Group g = rs.createGroup(Set.of(entrance, exit));
    setAttributeValue(g, AttributeType.NAME, "Autobahnkreuz");

    rs.connectConnectors(base.getExit(), exit.getEntry());
    rs.connectConnectors(base.getEntry(), entrance.getExit());
    rs.connectConnectors(exit.getExit(), entrance.getEntry());
    rs.connectConnectors(entrance.getRamp(), exit.getRamp());

    project = mock(Project.class);
    when(project.getRoadSystem()).thenReturn(rs);
  }

  @Test
  void testExportProject() throws IOException {
    var exportStrategy = new YamlExportStrategy(project);
    assertTrue(exportStrategy.exportToFile(EXPORT_FILE.toFile()));

    assertTrue(Files.exists(EXPORT_FILE));

    // Since we don't have an importer for the FREEVAL YAML format, we test this export through
    // exemplary lines that it needs to contain (independent of the order of entries)
    String contents = Files.readString(EXPORT_FILE);
    assertTrue(contents.contains("Segmente:\n"));
    assertTrue(contents.contains("\n    Name: AusfahrtKarlsbadFRStuttgart"));
    assertTrue(contents.contains("\n  Zeitintervall: 10"));
  }

  @SuppressWarnings("unchecked")
  static <T> void setAttributeValue(Element element, AttributeType type, T value) {
    for (var acc : element.getAttributeAccessors()) {
      if (acc.getAttributeType() == type) {
        ((AttributeAccessor<T>) acc).setValue(value);
        return;
      }
    }
    throw new RuntimeException();
  }
}
