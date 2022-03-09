package edu.kit.rose.controller.roadsystem;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link DragStreetSegmentsCommand} class.
 */
public class DragStreetSegmentsCommandTest {
  private static final Movement MOVEMENT = new Movement(10, 20);
  private static final Movement REVERSE_MOVEMENT = new Movement(-10, -20);
  private RoadSystem roadSystem;
  private ReplacementLog replacementLog;
  private List<Segment> segments;
  /**
   * The first segment to move.
   */
  private Base base;
  /**
   * The second segment to move.
   */
  private Exit exit;

  private DragStreetSegmentsCommand command;

  /**
   * Sets up the mocking objects.
   */
  @BeforeEach
  public void setUp() {
    var project = mock(Project.class);
    this.roadSystem = mock(RoadSystem.class);
    when(project.getRoadSystem()).thenReturn(this.roadSystem);

    this.segments = new LinkedList<>();
    this.base = new Base();
    this.segments.add(this.base);

    this.exit = new Exit();
    this.segments.add(this.exit);

    this.replacementLog = new ReplacementLog();
    this.command = new DragStreetSegmentsCommand(this.replacementLog, project, this.segments,
        MOVEMENT, new HashSet<>(), null);
  }

  @Test
  public void testCallsMoveSegments() {
    command.execute();
    verify(roadSystem, times(1))
        .moveSegments(eq(this.segments), eq(MOVEMENT));

    command.unexecute();
    verify(roadSystem, times(1))
        .moveSegments(eq(this.segments), eq(REVERSE_MOVEMENT));
  }

  @Test
  public void testConsidersReplacements() {
    // replace exit before execution
    Exit exitReplacement = new Exit();
    this.replacementLog.replaceElement(exit, exitReplacement);

    command.execute();
    verify(roadSystem, times(1))
        .moveSegments(eq(List.of(base, exitReplacement)), eq(MOVEMENT));

    // replace base before un-execution
    Base baseReplacement = new Base();
    this.replacementLog.replaceElement(base, baseReplacement);

    command.unexecute();
    verify(roadSystem, times(1))
        .moveSegments(eq(List.of(baseReplacement, exitReplacement)), eq(REVERSE_MOVEMENT));
  }
}
