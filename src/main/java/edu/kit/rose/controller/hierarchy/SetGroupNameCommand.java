package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Group;

/**
 * Encapsulates the functionality of renaming a group and makes
 * it changeable.
 */
public class SetGroupNameCommand implements ChangeCommand {
  private final Group group;
  private final String newName;
  private String lastName;

  /**
   * Creates a {@link SetGroupNameCommand} that assigns a new value to a group.
   *
   * @param group   the group with a name to change
   * @param newName the new name of the group
   */
  public SetGroupNameCommand(Group group, String newName) {
    this.group = group;
    this.newName = newName;
  }

  @Override
  public void execute() {
    this.lastName = this.group.getName();
    setNameToGroup(this.newName);
  }

  @Override
  public void unexecute() {
    setNameToGroup(this.lastName);
  }

  private void setNameToGroup(String name) {
    for (AttributeAccessor<?> attribute : this.group.getAttributeAccessors()) {
      if (attribute.getAttributeType().equals(AttributeType.NAME)) {
        AttributeAccessor<String> nameAttribute = (AttributeAccessor<String>) attribute;
        nameAttribute.setValue(name);
        return;
      }
    }
  }
}
