package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;

/**
 * Encapsulates the functionality of adding an {@link Element} to a {@link Group}
 * and makes it changeable.
 *
 */
public class AddElementToGroupCommand implements ChangeCommand {

    /**
     * Creates a new {@link AddElementToGroupCommand} that adds an {@link Element} to a {@Group}.
     *
     * @param project the model facade to execute the {@link AddElementToGroupCommand} on
     * @param element the element to add
     * @param group the group to add an element to
     */
    public AddElementToGroupCommand(Project project, Element element, Group group) {

    }

    @Override
    public void execute() {

    }

    @Override
    public void unexecute() {

    }
}
