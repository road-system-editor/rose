package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentFactory;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 * Tests the {@link CreateStreetSegmentCommand} class.
 */
public class CreateStreetSegmentCommandTest {

  private RoadSystem roadSystem;

  private Project project;

  /**
   * Sets up all mock objects.
   */
  @BeforeEach
  public void setUp() {
    this.roadSystem = Mockito.mock(RoadSystem.class);
    this.project = Mockito.mock(Project.class);

    Mockito.when(project.getRoadSystem()).thenReturn(roadSystem);
    Mockito.when(roadSystem.getElements()).thenReturn(new RoseBox<Element>());

    Mockito.when(roadSystem.createSegment(ArgumentMatchers.any(SegmentType.class)))
        .thenAnswer(invocation -> createSegment(invocation.getArgument(0)));
  }

  private Segment createSegment(SegmentType segmentType) {
    return switch (segmentType) {
      case BASE -> new Base();
      case ENTRANCE -> new Entrance();
      case EXIT -> new Exit();
    };
  }

  @Test
  public void testExecute() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);

    Mockito.when(roadSystem.createSegment(Mockito.any(SegmentType.class)))
        .thenAnswer(invocation -> {
          called.set(true);
          return null;
        });

    CreateStreetSegmentCommand command
        = new CreateStreetSegmentCommand(this.project, SegmentType.BASE);

    command.execute();
    Assertions.assertTrue(called.get());
  }

  @Test
  public void testUnExecute() {
    CreateStreetSegmentCommand command
        = new CreateStreetSegmentCommand(this.project, SegmentType.BASE);

    command.execute();
    command.unexecute();

    Mockito.verify(roadSystem, Mockito.times(1))
        .removeElement(ArgumentMatchers.any(Segment.class));
  }
}