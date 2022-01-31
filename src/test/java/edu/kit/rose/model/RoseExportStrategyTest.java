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
  private static final Path EXPORT_FILE = Path.of("build/tmp/rose-export.yml");

  Project project;

  @BeforeEach
  void beforeEach() {
    var criteriaManager = mock(CriteriaManager.class);
    when(criteriaManager.getCriteria()).thenReturn(new RoseSortedBox<>(List.of()));

    RoadSystem rs = new GraphRoadSystem(
        criteriaManager,
        new TimeSliceSetting(15, 10)
    );

    Base segment1 = (Base) rs.createSegment(SegmentType.BASE);
    setAttributeValue(segment1, AttributeType.NAME, "GWBFRStuttgart");
    setAttributeValue(segment1, AttributeType.LENGTH, 3000);
    setAttributeValue(segment1, AttributeType.LANE_COUNT, 2);
    setAttributeValue(segment1, AttributeType.CONURBATION, true);
    setAttributeValue(segment1, AttributeType.MAX_SPEED, -1);

    Exit segment2 = (Exit) rs.createSegment(SegmentType.EXIT);
    setAttributeValue(segment2, AttributeType.NAME, "AusfahrtKarlsbadFRStuttgart");
    setAttributeValue(segment2, AttributeType.LENGTH, 250);
    setAttributeValue(segment2, AttributeType.LANE_COUNT, 2);
    setAttributeValue(segment2, AttributeType.SLOPE, 2);
    setAttributeValue(segment2, AttributeType.CONURBATION, true);
    setAttributeValue(segment2, AttributeType.MAX_SPEED, -1);
    setAttributeValue(segment2, AttributeType.MAX_SPEED_RAMP, 60);

    rs.connectConnectors(segment1.getExit(), segment2.getEntry());

    project = mock(Project.class);
    when(project.getRoadSystem()).thenReturn(rs);
  }

  @Test
  void testExportProject() throws IOException {
    new RoseExportStrategy(project).exportToFile(EXPORT_FILE.toFile());

    assertTrue(Files.exists(EXPORT_FILE));
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
}
