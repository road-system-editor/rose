package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ConnectionCopier;
import edu.kit.rose.controller.commons.HierarchyCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Encapsulates the functionality of deleting a street segment
 * and makes it changeable.
 *
 * @author ROSE Team
 */
public class DeleteStreetSegmentCommand implements ChangeCommand {

  private final ReplacementLog replacementLog;
  private final Project project;
  private final HashMap<Segment, Group> segmentParentGroups = new HashMap<>();
  private final List<Connection> connections = new ArrayList<>();
  //private Segment segment;
  //private Group segmentParentGroup;

  /**
   * Creates a {@link DeleteStreetSegmentCommand} that deletes a street segment.
   *
   * @param replacementLog the replacement log to use for finding and registering current segment
   *     versions, may not be {@code null}.
   * @param project the model facade to execute {@link DeleteStreetSegmentCommand} on, may not be
   *     {@code null}.
   * @param segments the segments to delete, may not be {@code null}.
   */
  public DeleteStreetSegmentCommand(ReplacementLog replacementLog, Project project,
                                    Collection<Segment> segments) {
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.project = Objects.requireNonNull(project);
    Objects.requireNonNull(segments).forEach(segment -> segmentParentGroups.put(segment, null));
  }

  @Override
  public void execute() {
    this.connections.clear();

    for (Segment segment : segmentParentGroups.keySet()) {
      Segment currentSegment = this.replacementLog.getCurrentVersion(segment);

      saveConnectionsForSegment(currentSegment);
      saveParentGroupForSegment(currentSegment);

      this.project.getRoadSystem().removeElement(currentSegment);
    }
  }

  private void saveConnectionsForSegment(Segment segment) {

    for (Segment connectedSegment :
        this.project.getRoadSystem().getAdjacentSegments(segment)) {
      for (Connection segmentConnection
          : this.project.getRoadSystem().getConnections(segment, connectedSegment)) {

        this.connections.add(segmentConnection);
      }
    }
  }

  private void saveParentGroupForSegment(Segment segment) {
    Optional<Element> parentGroup = this.project.getRoadSystem().getElements()
        .stream()
        .filter(element -> element.isContainer() && ((Group) element).contains(segment))
        .findFirst();


    if (parentGroup.isPresent()) {
      Group group = (Group) parentGroup.get();
      this.segmentParentGroups.put(segment, group);
      group.removeElement(segment);
    } else {
      this.segmentParentGroups.put(segment, null);
    }
  }

  @Override
  public void unexecute() {
    HierarchyCopier copier = new HierarchyCopier(this.replacementLog, this.project.getRoadSystem());

    Set<Segment> segmentsCopy = new HashSet<>(this.segmentParentGroups.keySet());
    for (Segment segment : segmentsCopy) {
      Segment oldSegment = this.replacementLog.getCurrentVersion(segment);
      Segment newSegment = copier.copySegment(oldSegment);
      this.segmentParentGroups.put(newSegment, this.segmentParentGroups.get(oldSegment));
      this.segmentParentGroups.remove(oldSegment);

      this.replacementLog.replaceElement(oldSegment, newSegment);
      insertSegmentIntoParentGroup(newSegment);
    }

    restoreSegmentConnections();



  }

  private void insertSegmentIntoParentGroup(Segment segment) {
    Group oldParentGroup = this.segmentParentGroups.get(segment);
    if (oldParentGroup != null) {
      Group newParentGroup = this.replacementLog.getCurrentVersion(oldParentGroup);
      newParentGroup.addElement(segment);
    }

  }

  private void restoreSegmentConnections() {
    ConnectionCopier copier
        = new ConnectionCopier(this.replacementLog, this.project.getRoadSystem());
    for (Connection connectionItem : this.connections) {

      copier.copyConnection(connectionItem);
    }
    this.connections.clear();
  }
}
