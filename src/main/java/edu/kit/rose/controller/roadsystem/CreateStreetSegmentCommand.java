package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.ConnectorType;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the functionality of creating a street segment
 * and makes it changeable.
 */
public class CreateStreetSegmentCommand implements ChangeCommand {

  private final Project project;
  private final SegmentType segmentType;
  private Segment segment;
  private Group segmentParentGroup;

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
    if (this.segmentParentGroup != null) {
      this.segmentParentGroup.addElement(this.segment);
      this.segmentParentGroup = null;
    }
  }

  @Override
  public void unexecute() {
    storeParentGroups();
    this.project.getRoadSystem().removeElement(this.segment);
  }

  private void storeParentGroups() {
    for (Element element : this.project.getRoadSystem().getElements()) {
      if (element.isContainer()) {
        Group g = (Group) element;
        if (g.contains(this.segment)) {
          g.removeElement(this.segment);
          segmentParentGroup = g;
          break;
        }
      }
    }
  }
}
