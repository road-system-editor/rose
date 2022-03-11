package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ConnectionCopier;
import edu.kit.rose.controller.commons.HierarchyCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates the functionality of deleting a group
 * and makes it changeable.
 */
public class DeleteGroupCommand implements ChangeCommand {
  private final ReplacementLog replacementLog;
  private final Project project;
  private Group group;
  private Group parent;
  private final Set<Connection> storedConnections;

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
    this.storedConnections = new HashSet<>();
  }

  @Override
  public void execute() {
    var groupToDelete = this.replacementLog.getCurrentVersion(this.group);

    this.parent = this.project.getRoadSystem().getElements().stream()
        .filter(Element::isContainer)
        .map(element -> (Group) element)
        .filter(container -> container.contains(groupToDelete))
        .findAny().orElse(null);

    Set<Connection> storedConnections = new HashSet<>();
    this.saveSegmentConnections(groupToDelete, storedConnections);
    this.storedConnections.clear();
    this.storedConnections.addAll(storedConnections);

    if (this.parent != null) {
      var currentParent = this.replacementLog.getCurrentVersion(this.parent);
      currentParent.removeElement(groupToDelete);
    }

    this.project.getRoadSystem().removeElement(groupToDelete);
  }

  private void saveSegmentConnections(Element targetElement, Set<Connection> connections) {
    if (targetElement.isContainer()) {
      Group targetGroup = (Group) targetElement;
      for (Element targetGroupChild : targetGroup.getElements()) {
        saveSegmentConnections(targetGroupChild, connections);
      }
    } else {
      Segment targetSegment = (Segment) targetElement;
      for (Segment connectedSegment
          : this.project.getRoadSystem().getAdjacentSegments(targetSegment)) {
        Box<Connection> targetSegmentConnections = this.project.getRoadSystem().getConnections(
            targetSegment, connectedSegment);
        targetSegmentConnections.forEach(connections::add);
      }
    }
  }

  @Override
  public void unexecute() {
    // copy group
    var copier = new HierarchyCopier(this.replacementLog, this.project.getRoadSystem());
    var groupToCopy = this.replacementLog.getCurrentVersion(this.group);
    this.group = copier.copyGroup(groupToCopy);

    // assign parent
    if (this.parent != null) {
      var currentParent = this.replacementLog.getCurrentVersion(this.parent);
      currentParent.addElement(this.group);
    }

    restoreStoredConnections();
  }

  private void restoreStoredConnections() {
    ConnectionCopier copier
        = new ConnectionCopier(this.replacementLog, this.project.getRoadSystem());

    for (Connection connection : this.storedConnections) {
      copier.copyConnection(connection);
    }
  }
}
