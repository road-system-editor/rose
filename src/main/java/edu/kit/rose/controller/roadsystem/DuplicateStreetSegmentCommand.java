package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.HierarchyCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the functionality of creating a copy of a segment.
 */
public class DuplicateStreetSegmentCommand implements ChangeCommand {
  private static final int DISTANCE_FROM_ORIGIN_SEGMENT = 1;

  private final Project project;
  private final List<Segment> segmentsToDuplicate;
  private final ReplacementLog replacementLog;
  private final HierarchyCopier copier;
  private final List<Segment> segments;

  /**
   *  Constructor.
   *
   * @param replacementLog      the log that stores the replacements of elements
   * @param project             the model facade for project data
   * @param segmentsToDuplicate  the segment
   */
  public DuplicateStreetSegmentCommand(ReplacementLog replacementLog, Project project,
                                       List<Segment> segmentsToDuplicate) {
    this.replacementLog = replacementLog;
    this.project = project;
    this.segmentsToDuplicate = segmentsToDuplicate;
    this.copier = new HierarchyCopier(null, this.project.getRoadSystem());
    this.segments = new ArrayList<>();
  }

  @Override
  public void execute() {
    for (int i = 0; i < this.segmentsToDuplicate.size(); i++) {
      var oldSegment = this.replacementLog.getCurrentVersion(this.segmentsToDuplicate.get(i));
      var copy = copier
              .copySegment(this.replacementLog.getCurrentVersion(
                      this.segmentsToDuplicate.get(i)), false);
      if (this.segments.size() <= i) {
        this.segments.add(copy);
      } else {
        this.segments.set(i, copy);
      }
      this.segments.get(i)
              .move(new Movement(DISTANCE_FROM_ORIGIN_SEGMENT, DISTANCE_FROM_ORIGIN_SEGMENT));

      if (oldSegment != null) {
        this.replacementLog.replaceElement(oldSegment, this.segments.get(i));
      }
    }
  }

  @Override
  public void unexecute() {
    for (var segment : this.segments) {
      this.project.getRoadSystem()
              .removeElement(this.replacementLog.getCurrentVersion(segment));
    }
  }
}
