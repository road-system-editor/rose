package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Segment;

/**
 * Encapsulates the functionality of deleting a street segment
 * and makes it changeable.
 *
 * @author ROSE Team
 */
public class DeleteStreetSegmentCommand implements ChangeCommand {

    /**
     * Creates a {@link DeleteStreetSegmentCommand} that deletes a street segment.
     *
     * @param project the model facade to execute {@link DeleteStreetSegmentCommand} on
     * @param segment the segment to delete
     */
    public DeleteStreetSegmentCommand(Project project, Segment segment) {

    }

    @Override
    public void execute() {

    }

    @Override
    public void unexecute() {

    }
}
