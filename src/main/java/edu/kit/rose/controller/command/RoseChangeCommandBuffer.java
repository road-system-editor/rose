package edu.kit.rose.controller.command;

import java.util.Stack;

/**
 * Implements a container that stores ChangeCommand in
 * the order they get added. This container is responsible for
 * executing the stored ChangeCommand in the correct change order
 * when it comes to undo/redo requests.
 *
 */
public class RoseChangeCommandBuffer implements ChangeCommandBuffer {
  private static final int POINT_TO_NOTHING = -1;

  private final Stack<ChangeCommand> changeCommandStack;
  private int undoRedoPointer;

  public RoseChangeCommandBuffer() {
    this.changeCommandStack = new Stack<>();
    this.undoRedoPointer = POINT_TO_NOTHING;
  }

  @Override
  public void undo() {
    if (this.undoRedoPointer == POINT_TO_NOTHING)
      return;

    ChangeCommand command = this.changeCommandStack.get(this.undoRedoPointer);
    command.unexecute();
    this.undoRedoPointer--;
  }

  @Override
  public void redo() {
    if (this.undoRedoPointer == this.changeCommandStack.size() - 1)
      return;
    this.undoRedoPointer++;
    ChangeCommand command = this.changeCommandStack.get(this.undoRedoPointer);
    command.execute();
  }

  @Override
  public void addCommand(ChangeCommand changeCommand) {
    deleteCommandsAfterPointer();
    this.changeCommandStack.push(changeCommand);
    this.undoRedoPointer++;
  }

  @Override
  public void clear() {
    changeCommandStack.clear();
    this.undoRedoPointer = POINT_TO_NOTHING;
  }

  private void deleteCommandsAfterPointer() {
    if (this.changeCommandStack.size() > undoRedoPointer + 1) {
      this.changeCommandStack.subList(undoRedoPointer + 1, this.changeCommandStack.size()).clear();
    }
  }
}
