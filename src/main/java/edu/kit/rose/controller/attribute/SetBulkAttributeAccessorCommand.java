package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Collection;
import java.util.Objects;

/**
 * Encapsulates the functionality of setting an attribute accessors value in bulk
 * and makes it changeable.
 *
 * @param <T> the type of the accessor's value
 * @author ROSE Team
 */
public class SetBulkAttributeAccessorCommand<T> implements ChangeCommand {

  private final AttributeAccessor<T> accessor;
  private final T oldValue;
  private final T newValue;
  private final ReplacementLog replacementLog;
  private final Collection<Segment> segments;

  /**
   * Creates a {@link SetAttributeAccessorCommand} that sets an accessor's value to a new value.
   *
   * @param accessor the accessor with the value to be set
   * @param oldValue the previous value of the accessor
   * @param newValue the value to set on the accessor
   */
  public SetBulkAttributeAccessorCommand(ReplacementLog replacementLog,
                                     AttributeAccessor<T> accessor,
                                     T oldValue,
                                     T newValue, Collection<Segment> segments) {

    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.accessor = Objects.requireNonNull(accessor);
    this.oldValue = oldValue;
    this.newValue = newValue;
    this.segments = segments;
  }

  @Override
  public void execute() {
    for (Segment segment : segments) {
      AttributeAccessor<T> currentAccessor = getCurrentAccessor(segment);
      if (currentAccessor != null) {
        this.replacementLog.getCurrentAccessorVersion(currentAccessor).setValue(newValue);
      }
    }

  }

  @Override
  public void unexecute() {
    for (Segment segment : segments) {
      AttributeAccessor<T> currentAccessor = getCurrentAccessor(segment);
      if (currentAccessor != null) {
        this.replacementLog.getCurrentAccessorVersion(currentAccessor).setValue(oldValue);
      }
    }

  }

  private AttributeAccessor<T> getCurrentAccessor(Segment segment) {
    var accessors =
        this.replacementLog.getCurrentVersion(segment).getAttributeAccessors();
    var accessorList =
         accessors.stream().filter(acc
             -> acc.getAttributeType() == accessor.getAttributeType()).toList();
    if (accessorList.size() == 1) {
      return (AttributeAccessor<T>) accessorList.get(0);
    }

    return null;
  }
}
