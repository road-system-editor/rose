package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.HierarchyCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Encapsulates the functionality of deleting a group
 * and makes it changeable.
 */
public class DeleteGroupCommand implements ChangeCommand {
  private final ReplacementLog replacementLog;
  private final Project project;
  private Group group;
  private Group parent;

  /**
   * Creates a {@link DeleteGroupCommand} that deletes the given group.
   *
   * @param project the model facade to execute the {@link DeleteGroupCommand} on
   * @param group   the group to be deleted
   */
  public DeleteGroupCommand(ReplacementLog replacementLog, Project project, Group group) {
    this.replacementLog = replacementLog;
    this.project = project;
    this.group = group;
  }

  @Override
  public void execute() {
    var groupToDelete = this.replacementLog.getCurrentVersion(this.group);

    this.parent = this.project.getRoadSystem().getElements().stream()
        .filter(Element::isContainer)
        .map(element -> (Group) element)
        .filter(container -> container.contains(groupToDelete))
        .findAny().orElse(null);

    if (this.parent != null) {
      this.parent.removeElement(groupToDelete);
    }

    this.project.getRoadSystem().removeElement(groupToDelete);
  }

  @Override
  public void unexecute() {
    // copy group
    var copier = new HierarchyCopier(this.replacementLog, this.project.getRoadSystem());
    var groupToCopy = this.replacementLog.getCurrentVersion(this.group);
    this.group = copier.copyGroup(groupToCopy, true);

    // assign parent
    var currentParent = this.replacementLog.getCurrentVersion(this.parent);
    currentParent.addElement(this.group);
  }
}
