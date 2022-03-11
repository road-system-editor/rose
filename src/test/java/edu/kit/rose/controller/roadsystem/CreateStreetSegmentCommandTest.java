package edu.kit.rose.controller.roadsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests the {@link CreateStreetSegmentCommand} class.
 */
class CreateStreetSegmentCommandTest {
  private RoadSystem roadSystem;
  private Project project;
  private ReplacementLog replacementLog;

  private CreateStreetSegmentCommand command;

  /**
   * Sets up all mock objects.
   */
  @BeforeEach
  void setUp() {
    this.roadSystem = Mockito.mock(RoadSystem.class);
    this.project = Mockito.mock(Project.class);
    ZoomSetting zoomSetting = Mockito.mock(ZoomSetting.class);
    this.replacementLog = Mockito.mock(ReplacementLog.class);

    when(replacementLog.getCurrentVersion(any())).thenReturn(Mockito.mock(Segment.class));
    when(zoomSetting.getCenterOfView()).thenReturn(new Position(0, 0));
    when(project.getRoadSystem()).thenReturn(roadSystem);
    when(roadSystem.getElements()).thenReturn(new RoseBox<>());
    when(project.getZoomSetting()).thenReturn(zoomSetting);

    when(roadSystem.createSegment(any(SegmentType.class)))
        .thenAnswer(invocation -> createSegment(invocation.getArgument(0)));

    this.command = new CreateStreetSegmentCommand(this.replacementLog, this.project,
        SegmentType.BASE);
  }

  private Segment createSegment(SegmentType segmentType) {
    return switch (segmentType) {
      case BASE -> new Base();
      case ENTRANCE -> new Entrance();
      case EXIT -> new Exit();
    };
  }

  @Test
  void testExecute() {
    command.execute();
    verify(roadSystem, times(1)).createSegment(SegmentType.BASE);
  }

  @Test
  void testUnExecute() {
    command.execute();
    command.unexecute();

    Mockito.verify(roadSystem, Mockito.times(1))
        .removeElement(any(Segment.class));
  }

  @Test
  void testReplacement() {
    command.execute();
    command.unexecute();
    command.execute();
    verify(replacementLog, times(1)).replaceElement(any(), any());
  }
}