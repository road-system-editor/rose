package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the functionality of deleting a street segment
 * and makes it changeable.
 *
 * @author ROSE Team
 */
public class DeleteStreetSegmentCommand implements ChangeCommand {

  private final Project project;
  private Segment segment;
  private final List<Group> segmentParentGroups;

  /**
   * Creates a {@link DeleteStreetSegmentCommand} that deletes a street segment.
   *
   * @param project the model facade to execute {@link DeleteStreetSegmentCommand} on
   * @param segment the segment to delete
   */
  public DeleteStreetSegmentCommand(Project project, Segment segment) {
    this.project = project;
    this.segment = segment;

    this.segmentParentGroups = new ArrayList<>();
  }

  @Override
  public void execute() {

    for (Element element : this.project.getRoadSystem().getElements()) {
      if (element.isContainer()) {
        Group g = (Group) element;
        if (g.contains(this.segment)) {
          g.removeElement(this.segment);
          segmentParentGroups.add(g);
          break;
        }
      }
    }

    this.project.getRoadSystem().removeElement(segment);
  }

  @Override
  public void unexecute() {
    SegmentFactory segmentFactory = new SegmentFactory();
    this.segment = segmentFactory.createSegmentFrom(this.segment);

    segmentParentGroups.forEach(group -> group.addElement(this.segment));
    segmentParentGroups.clear();
  }
}
