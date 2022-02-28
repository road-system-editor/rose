package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Objects;

/**
 * Encapsulates the functionality of creating a street segment
 * and makes it changeable.
 */
public class CreateStreetSegmentCommand implements ChangeCommand {

  private static final int DEFAULT_ROTATION = 90;

  private final Project project;
  private final SegmentType segmentType;
  private final ReplacementLog replacementLog;
  private final Position position;
  private Segment segment;

  /**
   * Creates a {@link CreateStreetSegmentCommand} that creates a streetsegment of a specified type.
   *
   * @param project     the model facade to execute {@link CreateStreetSegmentCommand} on
   * @param segmentType the type of the segment to create
   */
  public CreateStreetSegmentCommand(ReplacementLog replacementLog, Project project,
                                    SegmentType segmentType) {
    this(replacementLog, project, segmentType, null);
  }

  /**
   * Creates a {@link CreateStreetSegmentCommand} that creates a streetsegment of a specified type.
   *
   * @param project     the model facade to execute {@link CreateStreetSegmentCommand} on
   * @param segmentType the type of the segment to create
   */
  public CreateStreetSegmentCommand(ReplacementLog replacementLog, Project project,
                                    SegmentType segmentType, Position position) {
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.project = project;
    this.segmentType = segmentType;
    this.position = position;
  }

  @Override
  public void execute() {
    final var oldSegment = this.segment;
    this.segment = project.getRoadSystem().createSegment(this.segmentType);

    Movement segmentMovement;
    if (this.position != null) {
      segmentMovement = new Movement(this.position.getX(), this.position.getY());
    } else {
      segmentMovement = new Movement(this.project.getZoomSetting().getCenterOfView().getX(),
          this.project.getZoomSetting().getCenterOfView().getY());
    }
    this.segment.move(segmentMovement);

    this.segment.rotate(DEFAULT_ROTATION);

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
