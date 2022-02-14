package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Objects;

/**
 * Encapsulates the functionality of creating a street segment
 * and makes it changeable.
 */
public class CreateStreetSegmentCommand implements ChangeCommand {

  private final Project project;
  private final SegmentType segmentType;
  private final ReplacementLog replacementLog;
  private Segment segment;

  /**
   * Creates a {@link CreateStreetSegmentCommand} that creates a streetsegment of a specified type.
   *
   * @param project     the model facade to execute {@link CreateStreetSegmentCommand} on
   * @param segmentType the type of the segment to create
   */
  public CreateStreetSegmentCommand(ReplacementLog replacementLog, Project project,
                                    SegmentType segmentType) {
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.project = project;
    this.segmentType = segmentType;
  }

  @Override
  public void execute() {
    var oldSegment = this.segment;
    this.segment = project.getRoadSystem().createSegment(this.segmentType);
    this.segment.move(new Movement(this.project.getZoomSetting().getCenterOfView().getX(),
            this.project.getZoomSetting().getCenterOfView().getY()));

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
