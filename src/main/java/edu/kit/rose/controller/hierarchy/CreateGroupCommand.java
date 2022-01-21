package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the functionality of creating a new group
 * and makes it changeable.
 */
public class CreateGroupCommand implements ChangeCommand {
  private final Project project;
  private final List<Element> segments;
  private Group group;

  /**
   * Creates a {@link CreateGroupCommand} that creates a group out of a list of segments.
   *
   * @param project  the model facade to execute the {@link CreateGroupCommand} on
   * @param segments the segments that will be in the group
   */
  public CreateGroupCommand(Project project, List<Segment> segments) {
    this.project = project;
    this.segments = new ArrayList<>(segments);
  }

  @Override
  public void execute() {
    this.group = this.project.getRoadSystem().createGroup(this.segments);
  }

  @Override
  public void unexecute() {
    this.project.getRoadSystem().removeElement(this.group);
  }
}
