package edu.kit.rose.controller.roadsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.HierarchyCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DuplicateStreetSegmentCommandTest {
  private RoadSystem roadSystem;
  private ReplacementLog replacementLog;
  private HierarchyCopier copier;
  private DuplicateStreetSegmentCommand command;

  @BeforeEach
  void setUp() {
    this.roadSystem = Mockito.mock(RoadSystem.class);
    this.copier = Mockito.mock(HierarchyCopier.class);
    Project project = Mockito.mock(Project.class);
    this.replacementLog = Mockito.mock(ReplacementLog.class);
    this.command = new DuplicateStreetSegmentCommand(this.replacementLog, project,
            new Base(), this.copier);

    when(replacementLog.getCurrentVersion(any())).thenReturn(Mockito.mock(Segment.class));
    when(project.getRoadSystem()).thenReturn(this.roadSystem);
    when(copier.copySegment(any())).thenReturn(new Base());
  }

  @Test
  public void testExecute() {
    command.execute();
    verify(copier, times(1)).copySegment(any(Segment.class));
  }

  @Test
  public void testUnExecute() {
    command.execute();
    command.unexecute();

    verify(roadSystem, times(1))
            .removeElement(any(Segment.class));
  }

  @Test
  public void testReplacement() {
    command.execute();
    command.unexecute();
    command.execute();
    verify(replacementLog, times(1)).replaceElement(any(), any());
  }
}