package edu.kit.rose.controller.roadsystem;

import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
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

  @BeforeEach
  void setUp() {
    this.roadSystem = new GraphRoadSystem(
        MockingUtility.mockCriteriaManager(),
        Mockito.mock(TimeSliceSetting.class)
    );
    Project project = Mockito.mock(Project.class);
    this.replacementLog = new ReplacementLog();
    Segment segment = new Base();

    when(project.getRoadSystem()).thenReturn(this.roadSystem);

    this.command = new DuplicateStreetSegmentCommand(this.replacementLog, project,
            List.of(segment));
  }

  @Test
  void testExecute() {
    command.execute();
    Assertions.assertEquals(1, this.roadSystem.getElements().getSize());
    Segment segment = (Segment)  this.roadSystem.getElements().iterator().next();
    Assertions.assertEquals(50, segment.getCenter().getX());
    Assertions.assertEquals(50, segment.getCenter().getY());
  }

  @Test
  void testUnExecute() {
    command.execute();
    command.unexecute();

    Assertions.assertEquals(0, this.roadSystem.getElements().getSize());
  }

  @Test
  void testReplacement() {
    command.execute();
    Segment segment = (Segment) this.roadSystem.getElements().iterator().next();
    command.unexecute();
    command.execute();
    Assertions.assertNotEquals(segment, this.replacementLog.getCurrentVersion(segment));
  }
}