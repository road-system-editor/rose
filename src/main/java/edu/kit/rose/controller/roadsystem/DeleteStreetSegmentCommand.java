package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.HierarchyCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Objects;

/**
 * Encapsulates the functionality of deleting a street segment
 * and makes it changeable.
 *
 * @author ROSE Team
 */
public class DeleteStreetSegmentCommand implements ChangeCommand {

  private final ReplacementLog replacementLog;
  private final Project project;
  private Segment segment;
  private Group segmentParentGroup;

  /**
   * Creates a {@link DeleteStreetSegmentCommand} that deletes a street segment.
   *
   * @param replacementLog the replacement log to use for finding and registering current segment
   *     versions, may not be {@code null}.
   * @param project the model facade to execute {@link DeleteStreetSegmentCommand} on, may not be
   *     {@code null}.
   * @param segment the segment to delete, may not be {@code null}.
   */
  public DeleteStreetSegmentCommand(ReplacementLog replacementLog, Project project,
                                    Segment segment) {
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.project = Objects.requireNonNull(project);
    this.segment = Objects.requireNonNull(segment);
  }

  @Override
  public void execute() {
    storeParentGroups();

    this.project.getRoadSystem().removeElement(getCurrentSegment());
  }

  private void storeParentGroups() {
    var currentSegment = getCurrentSegment();

    for (Element element : this.project.getRoadSystem().getElements()) {
      if (element.isContainer()) {
        Group g = (Group) element;
        if (g.contains(currentSegment)) {
          g.removeElement(currentSegment);
          segmentParentGroup = g;
          break;
        }
      }
    }
  }

  @Override
  public void unexecute() {
    HierarchyCopier copier = new HierarchyCopier(this.replacementLog, this.project.getRoadSystem());

    var oldSegment = getCurrentSegment();
    this.segment = copier.copySegment(oldSegment);
    this.replacementLog.replaceElement(oldSegment, this.segment);

    var currentParent = getCurrentParent();
    if (currentParent != null) {
      currentParent.addElement(this.segment);
      this.segmentParentGroup = null;
    }
  }

  private Segment getCurrentSegment() {
    this.segment = this.replacementLog.getCurrentVersion(this.segment);
    return this.segment;
  }

  private Group getCurrentParent() {
    this.segmentParentGroup = this.replacementLog.getCurrentVersion(this.segmentParentGroup);
    return this.segmentParentGroup;
  }
}
