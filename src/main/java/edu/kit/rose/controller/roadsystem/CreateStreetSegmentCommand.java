package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;

/**
 * Encapsulates the functionality of creating a street segment
 * and makes it changeable.
 */
public class CreateStreetSegmentCommand implements ChangeCommand {

  private final Project project;
  private final SegmentType segmentType;
  private Segment segment;

  /**
   * Creates a {@link CreateStreetSegmentCommand} that creates a streetsegment of a specified type.
   *
   * @param project     the model facade to execute {@link CreateStreetSegmentCommand} on
   * @param segmentType the type of the segment to create
   */
  public CreateStreetSegmentCommand(Project project, SegmentType segmentType) {
    this.project = project;
    this.segmentType = segmentType;
  }

  @Override
  public void execute() {
    this.segment = project.getRoadSystem().createSegment(this.segmentType);
  }

  @Override
  public void unexecute() {
    this.project.getRoadSystem().removeElement(this.segment);
  }
}
