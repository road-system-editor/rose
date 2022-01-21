package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;

/**
 * Encapsulates the functionality of adding an {@link Element} to a {@link Group}
 * and makes it changeable.
 */
public class AddElementToGroupCommand implements ChangeCommand {
  private final Element element;
  private final Group group;

  /**
   * Creates a new {@link AddElementToGroupCommand} that adds an {@link Element} to a {@link Group}.
   *
   * @param element the element to add
   * @param group   the group to add an element to
   */
  public AddElementToGroupCommand(Element element, Group group) {
    this.element = element;
    this.group = group;
  }

  @Override
  public void execute() {
    this.group.addElement(this.element);
  }

  @Override
  public void unexecute() {
    this.group.removeElement(element);
  }
}
