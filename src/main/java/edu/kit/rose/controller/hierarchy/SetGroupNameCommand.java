package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Group;

/**
 * Encapsulates the functionality of renaming a group and makes
 * it changeable.
 *
 */
public class SetGroupNameCommand implements ChangeCommand {

    /**
     * Creates a {@link SetGroupNameCommand} that assigns a new value to a group.
     *
     * @param project the model facade to execute the {@link SetGroupNameCommand} on
     * @param group the group with a name to change
     * @param newName the new name of the group
     */
    public SetGroupNameCommand(Project project, Group group, String newName) {

    }

    @Override
    public void execute() {

    }

    @Override
    public void unexecute() {

    }
}
