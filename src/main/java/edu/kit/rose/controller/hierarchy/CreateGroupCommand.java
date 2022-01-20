package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.List;

/**
 * Encapsulates the functionality of creating a new group
 * and makes it changeable.
 */
public class CreateGroupCommand implements ChangeCommand {

  /**
   * Creates a {@link CreateGroupCommand} that creates a group out of a list of segments.
   *
   * @param project  the model facade to execute the {@link CreateGroupCommand} on
   * @param segments the segments that will be in the group
   */
  public CreateGroupCommand(Project project, List<Segment> segments) {

  }

  @Override
  public void execute() {

  }

  @Override
  public void unexecute() {

  }
}
