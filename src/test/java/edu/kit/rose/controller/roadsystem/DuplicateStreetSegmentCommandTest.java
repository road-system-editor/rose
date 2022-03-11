package edu.kit.rose.controller.roadsystem;

import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.util.MockingUtility;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DuplicateStreetSegmentCommandTest {
  private RoadSystem roadSystem;
  private ReplacementLog replacementLog;
  private DuplicateStreetSegmentCommand command;
  private Segment segment1;
  private Segment segment2;

  @BeforeEach
  void setUp() {
    this.roadSystem = new GraphRoadSystem(
        MockingUtility.mockCriteriaManager(),
        Mockito.mock(TimeSliceSetting.class)
    );
    this.replacementLog = new ReplacementLog();
    segment1 = roadSystem.createSegment(SegmentType.BASE);
    segment2 = roadSystem.createSegment(SegmentType.BASE);
    roadSystem.connectConnectors(segment1.getConnectors().iterator().next(),
            segment2.getConnectors().iterator().next());
    Project project = Mockito.mock(Project.class);
    when(project.getRoadSystem()).thenReturn(this.roadSystem);

    this.command = new DuplicateStreetSegmentCommand(this.replacementLog, project,
            List.of(segment1, segment2));
  }

  @Test
  void testExecute() {
    command.execute();
    Assertions.assertEquals(4, this.roadSystem.getElements().getSize());
    Assertions.assertTrue(roadSystem.getConnections(segment1)
            .iterator().next().getConnectors()
            .contains(segment1.getConnectors().iterator().next()));
    Assertions.assertTrue(roadSystem.getConnections(segment1)
            .iterator().next().getConnectors()
            .contains(segment2.getConnectors().iterator().next()));
    List<Segment> elements = roadSystem.getElements().stream().map(Segment.class::cast).toList();
    Assertions.assertEquals(50, elements.get(2).getCenter().getX());
    Assertions.assertEquals(50, elements.get(2).getCenter().getY());
  }

  @Test
  void testUnExecute() {
    command.execute();
    command.unexecute();

    Assertions.assertEquals(2, this.roadSystem.getElements().getSize());
  }

  @Test
  void testReplacement() {
    command.execute();
    Segment segment = roadSystem.getElements().stream().map(Segment.class::cast).toList().get(2);
    command.unexecute();
    command.execute();
    Assertions.assertNotEquals(segment, this.replacementLog.getCurrentVersion(segment));
  }
}