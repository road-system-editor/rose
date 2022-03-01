package edu.kit.rose.controller.command;

/**
 * Represents a container that stores a fixed number of
 * ChangeCommands in the order are added, and provides the functionality
 * to execute these commands in context of change.
 */
public interface ChangeCommandBuffer {

  /**
   * Unexecutes/Undoes the newest {@link ChangeCommand} that has not been undone.
   * If there's not a command in the buffer to be undone, this method does nothing.
   */
  void undo();

  /**
   * Re-executes/Redoes the last undone {@link ChangeCommand}.
   * If there's not a command in the buffer to be redone, this method does nothing.
   */
  void redo();

  /**
   * Adds a new {@link ChangeCommand} and executes it.
   *
   * @param changeCommand the command to be added
   */
  void addAndExecuteCommand(ChangeCommand changeCommand);

  /**
   * Deletes all {@link ChangeCommand}s in the {@link ChangeCommandBuffer}.
   */
  void clear();
}
