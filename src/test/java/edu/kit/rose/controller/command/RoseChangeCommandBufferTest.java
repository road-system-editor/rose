package edu.kit.rose.controller.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseChangeCommandBuffer}.
 */
public class RoseChangeCommandBufferTest {
  private TestChangeCommand command1;
  private TestChangeCommand command2;
  private TestChangeCommand command3;
  private RoseChangeCommandBuffer buffer;

  /**
   * Initializes a command buffer with 3 commands in it.
   */
  @BeforeEach
  void setUp() {
    this.command1 = new TestChangeCommand(1);
    this.command2 = new TestChangeCommand(2);
    this.command3 = new TestChangeCommand(3);
    this.buffer = new RoseChangeCommandBuffer();

    this.buffer.addCommand(command1);
    this.buffer.addCommand(command2);
    this.buffer.addCommand(command3);
  }

  @Test
  void testUndo() {
    this.buffer.undo();
    this.buffer.undo();

    Assertions.assertEquals("", this.command1.getMessage());
    Assertions.assertEquals("unexecuted2", this.command2.getMessage());
    Assertions.assertEquals("unexecuted3", this.command3.getMessage());
  }

  @Test
  void testRedo() {
    this.buffer.undo();
    this.buffer.undo();
    this.buffer.redo();

    Assertions.assertEquals("", this.command1.getMessage());
    Assertions.assertEquals("executed2", this.command2.getMessage());
    Assertions.assertEquals("unexecuted3", this.command3.getMessage());
  }

  @Test
  void testClear() {
    this.buffer.clear();
    this.buffer.undo();

    // if the buffer is cleared then undo will not change the initial state of the commands
    Assertions.assertEquals("", this.command1.getMessage());
    Assertions.assertEquals("", this.command2.getMessage());
    Assertions.assertEquals("", this.command3.getMessage());
  }

  private static class TestChangeCommand implements ChangeCommand {
    private String message = "";
    private final int id;

    public TestChangeCommand(int id) {
      this.id = id;
    }

    @Override
    public void execute() {
      this.message = "executed" + this.id;
    }

    @Override
    public void unexecute() {
      this.message = "unexecuted" + this.id;
    }

    public String getMessage() {
      return this.message;
    }
  }
}
