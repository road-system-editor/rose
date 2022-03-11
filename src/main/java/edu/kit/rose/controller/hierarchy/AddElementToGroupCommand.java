package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.Objects;

/**
 * Encapsulates the functionality of adding an {@link Element} to a {@link Group}
 * and makes it changeable.
 */
public class AddElementToGroupCommand implements ChangeCommand {
  private final ReplacementLog replacementLog;
  private final Project project;
  private final Element element;
  private final Group group;
  private Group parent;

  /**
   * Creates a new {@link AddElementToGroupCommand} that adds an {@link Element} to a {@link Group}.
   *
   * @param replacementLog the replacement log to look up current element versions in.
   * @param project the model facade for project data
   * @param element the element to add
   * @param group   the group to add an element to
   */
  public AddElementToGroupCommand(ReplacementLog replacementLog, Project project, Element element,
                                  Group group) {
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.project = Objects.requireNonNull(project);
    this.element = Objects.requireNonNull(element);
    this.group = Objects.requireNonNull(group);
    this.parent = null;
  }

  @Override
  public void execute() {
    var currentElement = this.replacementLog.getCurrentVersion(this.element);

    // find parent and remove element
    this.parent = getParentGroup(currentElement);
    this.parent.removeElement(currentElement);

    // add element to new group
    var currentGroup = this.replacementLog.getCurrentVersion(this.group);
    currentGroup.addElement(currentElement);
  }

  private Group getParentGroup(Element currentElement) {
    return (Group) this.project.getRoadSystem().getElements()
        .stream()
        .filter(element1 -> element1.isContainer() && ((Group) element1).contains(currentElement))
        .findFirst()
        .orElse(this.project.getRoadSystem().getRootGroup());
  }

  @Override
  public void unexecute() {
    var currentGroup = this.replacementLog.getCurrentVersion(this.group);
    var currentElement = this.replacementLog.getCurrentVersion(this.element);
    var currentParent = this.replacementLog.getCurrentVersion(this.parent);

    currentGroup.removeElement(currentElement);
    if (currentParent != null) {
      currentParent.addElement(currentElement);
    }
  }
}
