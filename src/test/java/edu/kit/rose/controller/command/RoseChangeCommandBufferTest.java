package edu.kit.rose.controller.command;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseChangeCommandBuffer}.
 */
public class RoseChangeCommandBufferTest {
  private ChangeCommand command1;
  private ChangeCommand command2;
  private ChangeCommand command3;
  private RoseChangeCommandBuffer buffer;

  /**
   * Initializes a command buffer with 3 commands in it.
   */
  @BeforeEach
  void setUp() {
    this.command1 = mock(ChangeCommand.class);
    this.command2 = mock(ChangeCommand.class);
    this.command3 = mock(ChangeCommand.class);
    this.buffer = new RoseChangeCommandBuffer();

    this.buffer.addAndExecuteCommand(command1);
    this.buffer.addAndExecuteCommand(command2);
    this.buffer.addAndExecuteCommand(command3);
  }

  @Test
  void testUndo() {
    this.buffer.undo();
    this.buffer.undo();

    verify(command1, never()).unexecute();
    verify(command2, times(1)).unexecute();
    verify(command3, times(1)).unexecute();
  }

  @Test
  void testRedo() {
    this.buffer.undo();
    this.buffer.undo();
    this.buffer.redo();

    verify(command1, times(1)).execute();
    verify(command1, never()).unexecute();
    verify(command2, times(1)).unexecute();
    verify(command2, times(2)).execute();
    verify(command3, times(1)).unexecute();
    verify(command3, times(1)).execute();

    this.buffer.redo();
    verify(command3, times(1)).unexecute();
    verify(command3, times(2)).execute();

    this.buffer.redo();
    verify(command1, times(1)).execute();
    verify(command1, never()).unexecute();
    verify(command2, times(1)).unexecute();
    verify(command2, times(2)).execute();
    verify(command3, times(1)).unexecute();
    verify(command3, times(2)).execute();
  }

  @Test
  void testClear() {
    this.buffer.clear();
    this.buffer.undo();

    verify(command1, never()).unexecute();
    verify(command2, never()).unexecute();
    verify(command3, never()).unexecute();
  }

  @Test
  void commandsDeletedAfterNewCommandAddedTest() {
    ChangeCommand command4 = mock(ChangeCommand.class);

    this.buffer.undo();
    this.buffer.undo();
    this.buffer.addAndExecuteCommand(command4);
    this.buffer.redo();
    this.buffer.redo();
    verify(command1, times(1)).execute();
    verify(command1, never()).unexecute();
    verify(command2, times(1)).unexecute();
    verify(command2, times(1)).execute();
    verify(command3, times(1)).unexecute();
    verify(command3, times(1)).execute();
    verify(command4, times(1)).execute();
  }
}
