package edu.kit.rose.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.util.MockingUtility;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link YamlExportStrategy}.
 */
class YamlExportStrategyTest {
  private static final Path EXPORT_FILE = Path.of("build/tmp/yaml-export.yml");

  Project project;

  @BeforeEach
  void beforeEach() {
    RoadSystem rs = new GraphRoadSystem(
        MockingUtility.mockCriteriaManager(),
        new TimeSliceSetting(15, 10)
    );

    Base segment1 = (Base) rs.createSegment(SegmentType.BASE);
    segment1.setName("GWBFRStuttgart");
    segment1.setLength(3000);
    segment1.setLaneCount(2);
    segment1.setConurbation(true);
    segment1.setMaxSpeed(SpeedLimit.NONE);

    Exit segment2 = (Exit) rs.createSegment(SegmentType.EXIT);
    segment2.setName("AusfahrtKarlsbadFRStuttgart");
    segment2.setLength(250);
    segment2.setLaneCount(2);
    segment2.setSlope(2.0);
    segment2.setConurbation(true);
    segment2.setMaxSpeed(SpeedLimit.NONE);
    segment2.setMaxSpeedRamp(SpeedLimit.SBA); // sample file said 60 but we don't support that

    Base segment3 = (Base) rs.createSegment(SegmentType.BASE);
    segment3.setName("KarlsbadFRStuttgart");
    segment3.setLength(500);
    segment3.setLaneCount(2);
    segment3.setSlope(2.0);
    segment3.setConurbation(true);
    segment3.setMaxSpeed(SpeedLimit.NONE);

    Entrance segment4 = (Entrance) rs.createSegment(SegmentType.ENTRANCE);
    segment4.setName("EinfahrtKarlsbadFRStuttgart");
    segment4.setLength(250);
    segment4.setLaneCount(2);
    segment4.setSlope(2.0);
    segment4.setConurbation(true);
    segment4.setMaxSpeed(SpeedLimit.NONE);
    segment4.setMaxSpeedRamp(SpeedLimit.TUNNEL); // sample file said 60 but we don't support that

    Base segment5 = (Base) rs.createSegment(SegmentType.BASE);
    segment5.setLength(3000);
    segment5.setLaneCount(2);
    segment5.setSlope(2.0);
    segment5.setConurbation(false); // changed to 'false' to test this value
    segment5.setMaxSpeed(SpeedLimit.NONE);

    rs.connectConnectors(segment1.getEntry(), segment2.getEntry());
    rs.connectConnectors(segment2.getExit(), segment3.getEntry());
    // segment6 does not exist rs.connectConnectors(segment2.getRamp(), segment6.getEntry());
    rs.connectConnectors(segment3.getExit(), segment4.getEntry());
    rs.connectConnectors(segment4.getExit(), segment5.getEntry());
    // segment7 does not exist rs.connectConnectors(segment7.getExit(), segment4.getRamp());

    Group g = rs.createGroup(Set.of(segment4, segment2)); // not part of the sample file
    g.setName("Autobahnkreuz KarlsbadFRStuttgart");

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
}
