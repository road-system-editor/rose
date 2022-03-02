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
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
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
    base.setName("GWBFRStuttgart");
    base.setLength(3000);
    base.setLaneCount(2);
    base.setConurbation(true);
    base.setMaxSpeed(SpeedLimit.NONE);

    Exit exit = (Exit) rs.createSegment(SegmentType.EXIT);
    exit.setName("AusfahrtKarlsbadFRStuttgart");
    exit.setLength(250);
    exit.setLaneCount(2);
    exit.setSlope(2.0);
    exit.setConurbation(true);
    exit.setMaxSpeed(SpeedLimit.NONE);
    exit.setMaxSpeedRamp(SpeedLimit.SBA);

    Entrance entrance = (Entrance) rs.createSegment(SegmentType.ENTRANCE);
    entrance.setName("EinfahrtKarlsbadFRStuttgart");
    entrance.setLength(250);
    entrance.setLaneCount(2);
    entrance.setSlope(2.0);
    entrance.setConurbation(false);
    entrance.setMaxSpeed(SpeedLimit.NONE);
    entrance.setMaxSpeedRamp(SpeedLimit.SBA);

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
