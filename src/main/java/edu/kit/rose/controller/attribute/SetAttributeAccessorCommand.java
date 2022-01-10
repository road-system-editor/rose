package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;

/**
 * Encapsulates the functionality of setting an attribute accessors value
 * and makes it changeable.
 *
 * @author ROSE Team
 * @param <T> the type of the accessor's value
 */
public class SetAttributeAccessorCommand<T> implements ChangeCommand {

    /**
     * Creates a {@link SetAttributeAccessorCommand} that sets an accessor's value to a new value.
     *
     * @param project the model facade to execute the {@link SetAttributeAccessorCommand} on
     * @param accessor the accessor with the value to be set
     * @param oldValue the previous value of the accessor
     * @param newValue the value to set on the accessor
     */
    public SetAttributeAccessorCommand(Project project, AttributeAccessor<T> accessor, T oldValue, T newValue) {
    }

    @Override
    public void execute() {

    }

    @Override
    public void unexecute() {

    }
}
