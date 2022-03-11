package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.roadsystem.elements.Group;

/**
 * Encapsulates the functionality of renaming a group and makes
 * it changeable.
 */
public class SetGroupNameCommand implements ChangeCommand {
  private final ReplacementLog replacementLog;
  private final Group group;
  private final String newName;
  private String lastName;

  /**
   * Creates a {@link SetGroupNameCommand} that assigns a new value to a group.
   *
   * @param group   the group with a name to change
   * @param newName the new name of the group
   */
  public SetGroupNameCommand(ReplacementLog replacementLog, Group group, String newName) {
    this.replacementLog = replacementLog;
    this.group = group;
    this.newName = newName;
  }

  @Override
  public void execute() {
    Group currentGroup = this.replacementLog.getCurrentVersion(this.group);
    this.lastName = currentGroup.getName();
    currentGroup.setName(this.newName);
  }

  @Override
  public void unexecute() {
    Group currentGroup = this.replacementLog.getCurrentVersion(this.group);
    currentGroup.setName(this.lastName);
  }
}
