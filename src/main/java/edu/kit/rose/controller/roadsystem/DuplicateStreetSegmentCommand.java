package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.HierarchyCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Segment;

/**
 * Encapsulates the functionality of creating a copy of a segment.
 */
public class DuplicateStreetSegmentCommand implements ChangeCommand {
  private static final int DISTANCE_FROM_ORIGIN_SEGMENT = 1;

  private final Project project;
  private final Segment segmentToDuplicate;
  private final ReplacementLog replacementLog;
  private final HierarchyCopier copier;
  private Segment segment;

  /**
   *  Constructor.
   *
   * @param replacementLog      the log that stores the replacements of elements
   * @param project             the model facade for project data
   * @param segmentToDuplicate  the segment
   */
  public DuplicateStreetSegmentCommand(ReplacementLog replacementLog, Project project,
                                       Segment segmentToDuplicate,
                                        HierarchyCopier copier) {
    this.replacementLog = replacementLog;
    this.project = project;
    this.segmentToDuplicate = segmentToDuplicate;
    this.copier = copier;
  }

  @Override
  public void execute() {
    var oldSegment = this.segment;
    this.segment = copier.copySegment(this.segmentToDuplicate);
    this.segment
            .move(new Movement(segmentToDuplicate.getCenter().getX() + DISTANCE_FROM_ORIGIN_SEGMENT,
            segmentToDuplicate.getCenter().getY() + DISTANCE_FROM_ORIGIN_SEGMENT));

    if (oldSegment != null) {
      this.replacementLog.replaceElement(oldSegment, this.segment);
    }
  }

  @Override
  public void unexecute() {
    this.project.getRoadSystem()
            .removeElement(this.replacementLog.getCurrentVersion(this.segment));
  }
}
