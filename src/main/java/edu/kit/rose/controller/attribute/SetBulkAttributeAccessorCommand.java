package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Encapsulates the functionality of setting an attribute accessors value in bulk
 * and makes it changeable.
 *
 * @param <T> the type of the accessor's value
 */
public class SetBulkAttributeAccessorCommand<T> implements ChangeCommand {

  private final AttributeAccessor<T> accessor;
  private final T newValue;
  private final ReplacementLog replacementLog;
  private final Map<Segment, T> segmentToOldValueMap;

  /**
   * Creates a {@link SetBulkAttributeAccessorCommand} that sets an accessor's value to a new value.
   *
   * @param accessor the accessor with the value to be set
   * @param newValue the value to set on the accessor
   */
  public SetBulkAttributeAccessorCommand(ReplacementLog replacementLog,
                                     AttributeAccessor<T> accessor,
                                     T newValue, Collection<Segment> segments) {

    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.accessor = Objects.requireNonNull(accessor);
    this.newValue = newValue;
    this.segmentToOldValueMap = new HashMap<>();

    for (Segment segment : segments) {
      AttributeAccessor<T> currentAccessor = getCurrentAccessor(segment);
      segmentToOldValueMap.put(segment, currentAccessor.getValue());
    }
  }

  @Override
  public void execute() {
    for (Segment segment : segmentToOldValueMap.keySet()) {
      AttributeAccessor<T> currentAccessor = getCurrentAccessor(segment);
      currentAccessor.setValue(newValue);
    }

  }

  @Override
  public void unexecute() {
    for (Segment segment : segmentToOldValueMap.keySet()) {
      AttributeAccessor<T> currentAccessor = getCurrentAccessor(segment);
      currentAccessor.setValue(segmentToOldValueMap.get(segment));
    }

  }

  private AttributeAccessor<T> getCurrentAccessor(Segment segment) {
    var accessors =
        this.replacementLog.getCurrentVersion(segment).getAttributeAccessors();
    AttributeAccessor<?> currentAccessor = accessors.stream().filter(acc
            -> acc.getAttributeType() == accessor.getAttributeType()).findAny().orElseThrow();

    //This cast is safe because the stream filters it by Type to ensure it is <T>.
    @SuppressWarnings("unchecked")
    AttributeAccessor<T> castedAccessor = (AttributeAccessor<T>) currentAccessor;
    return castedAccessor;
  }
}
