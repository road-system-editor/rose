package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.Objects;

/**
 * Encapsulates the functionality of setting an attribute accessors value
 * and makes it changeable.
 *
 * @param <T> the type of the accessor's value
 * @author ROSE Team
 */
public class SetAttributeAccessorCommand<T> implements ChangeCommand {

  private final AttributeAccessor<T> accessor;
  private final T oldValue;
  private final T newValue;
  private final ReplacementLog replacementLog;

  /**
   * Creates a {@link SetAttributeAccessorCommand} that sets an accessor's value to a new value.
   *
   * @param accessor the accessor with the value to be set
   * @param oldValue the previous value of the accessor
   * @param newValue the value to set on the accessor
   */
  public SetAttributeAccessorCommand(ReplacementLog replacementLog,
                                     AttributeAccessor<T> accessor,
                                     T oldValue,
                                     T newValue) {
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.accessor = Objects.requireNonNull(accessor);
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  @Override
  public void execute() {
    this.replacementLog.getCurrentAccessorVersion(accessor).setValue(newValue);
  }

  @Override
  public void unexecute() {
    this.replacementLog.getCurrentAccessorVersion(accessor).setValue(oldValue);
  }
}
