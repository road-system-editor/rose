package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.SimpleSetObservable;
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
  private final List<Element> elements;
  private Group group;

  /**
   * Creates a {@link CreateGroupCommand} that creates a group out of a list of segments.
   *
   * @param project  the model facade to execute the {@link CreateGroupCommand} on
   * @param elements the elements that will be in the group
   */
  public CreateGroupCommand(Project project, List<Element> elements) {
    this.project = project;
    this.elements = new ArrayList<>(elements);
  }

  @Override
  public void execute() {
    this.group = this.project.getRoadSystem().createGroup(this.elements);
  }

  @Override
  public void unexecute() {
    this.project.getRoadSystem().removeElement(this.group);
  }
}
