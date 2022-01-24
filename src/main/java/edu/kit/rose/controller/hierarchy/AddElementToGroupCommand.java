package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;

/**
 * Encapsulates the functionality of adding an {@link Element} to a {@link Group}
 * and makes it changeable.
 */
public class AddElementToGroupCommand implements ChangeCommand {
  private final Project project;
  private final Element element;
  private final Group group;
  private Group parent;

  /**
   * Creates a new {@link AddElementToGroupCommand} that adds an {@link Element} to a {@link Group}.
   *
   * @param project the model facade for project data
   * @param element the element to add
   * @param group   the group to add an element to
   */
  public AddElementToGroupCommand(Project project, Element element, Group group) {
    this.project = project;
    this.element = element;
    this.group = group;
    this.parent = null;
  }

  @Override
  public void execute() {
    Box<Element> elements = this.project.getRoadSystem().getElements();
    for (Element auxElement : elements) {
      if (auxElement.isContainer()) {
        Group auxGroup = (Group) auxElement;
        if (auxGroup.contains(this.element)) {
          auxGroup.removeElement(this.element);
          this.parent = auxGroup;
        }
      }
    }
    this.group.addElement(this.element);
  }

  @Override
  public void unexecute() {
    this.group.removeElement(element);
    if (this.parent != null) {
      this.parent.addElement(this.element);
    }
  }
}
