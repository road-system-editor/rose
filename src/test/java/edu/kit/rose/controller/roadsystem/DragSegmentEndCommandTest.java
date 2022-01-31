package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.elements.MovableConnector;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 * Tests the {@link DragSegmentEndCommand} class.
 */
public class DragSegmentEndCommandTest {

  private Movement movement;
  private MovableConnector connector;
  private AtomicReference<Integer> xpos;
  private AtomicReference<Integer> ypos;

  /**
   * Sets up all mock objects.
   */
  @BeforeEach
  public void setUp() {
    xpos = new AtomicReference<>(10);
    ypos = new AtomicReference<>(20);

    movement = Mockito.mock(Movement.class);
    Mockito.when(movement.getX()).thenReturn(10);
    Mockito.when(movement.getY()).thenReturn(10);

    connector = Mockito.mock(MovableConnector.class);

    Mockito.when(connector.getPosition()).thenReturn(new Position(xpos.get(), ypos.get()));
    Mockito.doAnswer(invocation -> {
      Movement movement = invocation.getArgument(0);
      xpos.set(xpos.get() + movement.getX());
      ypos.set(ypos.get() + movement.getY());
      return null;
    }).when(connector).move(ArgumentMatchers.any(Movement.class));
  }

  @Test
  public void testExecute() {
    DragSegmentEndCommand command = new DragSegmentEndCommand(this.connector, this.movement);
    command.execute();

    Assertions.assertEquals(20, xpos.get());
    Assertions.assertEquals(30, ypos.get());
  }

  @Test
  public void testUnExecute() {
    DragSegmentEndCommand command = new DragSegmentEndCommand(this.connector, this.movement);
    command.unexecute();

    Assertions.assertEquals(0, xpos.get());
    Assertions.assertEquals(10, ypos.get());
  }
}
