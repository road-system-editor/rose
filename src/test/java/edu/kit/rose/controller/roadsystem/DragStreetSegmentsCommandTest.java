package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 * Tests the {@link DragStreetSegmentsCommand} class.
 */
public class DragStreetSegmentsCommandTest {

  private Project project;
  private RoadSystem roadSystem;
  private Movement movement;

  /**
   * Sets up the mocking objects.
   */
  @BeforeEach
  public void setUp() {
    this.project = Mockito.mock(Project.class);
    this.roadSystem = Mockito.mock(RoadSystem.class);

    movement = Mockito.mock(Movement.class);
    Mockito.when(movement.getX()).thenReturn(10);
    Mockito.when(movement.getY()).thenReturn(20);
  }

  @Test
  public void testExecute() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);

    List<Segment> segments = new ArrayList<>();
    Segment segment = Mockito.mock(Segment.class);
    segments.add(segment);

    Mockito.when(project.getRoadSystem()).thenReturn(roadSystem);
    Mockito.doAnswer(invocation -> {
      List<Segment> s = invocation.getArgument(0);
      Movement m = invocation.getArgument(1);

      Assertions.assertTrue(s.contains(segment));
      Assertions.assertEquals(10, m.getX());
      Assertions.assertEquals(20, m.getY());
      called.set(true);
      return null;
    }).when(roadSystem).moveSegments(ArgumentMatchers.anyCollection(), ArgumentMatchers.any());

    DragStreetSegmentsCommand command
        = new DragStreetSegmentsCommand(this.project, segments, this.movement);
    command.execute();

    Assertions.assertTrue(called.get());
  }

  @Test
  public void testUnexecute() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);

    List<Segment> segments = new ArrayList<>();
    Segment segment = Mockito.mock(Segment.class);
    segments.add(segment);

    Mockito.when(project.getRoadSystem()).thenReturn(roadSystem);
    Mockito.doAnswer(invocation -> {
      List<Segment> s = invocation.getArgument(0);
      Movement m = invocation.getArgument(1);

      Assertions.assertTrue(s.contains(segment));
      Assertions.assertEquals(10, (-m.getX()));
      Assertions.assertEquals(20, (-m.getY()));
      called.set(true);
      return null;
    }).when(roadSystem).moveSegments(ArgumentMatchers.anyCollection(), ArgumentMatchers.any());

    DragStreetSegmentsCommand command
        = new DragStreetSegmentsCommand(this.project, segments, this.movement);
    command.unexecute();

    Assertions.assertTrue(called.get());
  }
}
