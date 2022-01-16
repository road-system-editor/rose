package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Group;

/**
 * Encapsulates the functionality of deleting a group
 * and makes it changeable.
 */
public class DeleteGroupCommand implements ChangeCommand {

  /**
   * Creates a {@link DeleteGroupCommand} that deletes the given group.
   *
   * @param project the model facade to execute the {@link DeleteGroupCommand} on
   * @param group   the group to be deleted
   */
  public DeleteGroupCommand(Project project, Group group) {

  }

  @Override
  public void execute() {

  }

  @Override
  public void unexecute() {

  }
}
