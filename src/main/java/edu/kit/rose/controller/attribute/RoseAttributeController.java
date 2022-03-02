package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Provides methods for setting the values of attribute accessors.
 *
 */
public class RoseAttributeController extends Controller implements AttributeController {

  private final ChangeCommandBuffer changeCommandBuffer;
  private final SelectionBuffer selectionBuffer;
  private final ApplicationDataSystem applicationDataSystem;
  private final Project project;
  private final ReplacementLog replacementLog;

  /**
   * Creates a new {@link RoseAttributeController}.
   *
   * @param changeCommandBuffer the buffer for change commands
   * @param selectionBuffer     the container that stores selected segments
   * @param storageLock         the coordinator for controller actions
   * @param navigator           the navigator for the controller
   * @param applicationDataSystem             the model facade for project data
   * @param replacementLog      the replacement log.
   */
  public RoseAttributeController(ChangeCommandBuffer changeCommandBuffer,
                                 SelectionBuffer selectionBuffer, StorageLock storageLock,
                                 Navigator navigator, Project project,
                                 ApplicationDataSystem applicationDataSystem,
                                 ReplacementLog replacementLog) {
    super(storageLock, navigator);
    this.changeCommandBuffer = changeCommandBuffer;
    this.selectionBuffer = selectionBuffer;
    this.project = project;
    this.applicationDataSystem = applicationDataSystem;
    this.replacementLog = replacementLog;
  }

  @Override
  public <T> void setAttribute(AttributeAccessor<T> accessor, T value) {
    if (getStorageLock().isStorageLockAcquired()) {
      return;
    }

    var command  = new SetAttributeAccessorCommand<>(replacementLog, accessor,
        accessor.getValue(), value);
    changeCommandBuffer.addAndExecuteCommand(command);
  }

  @Override
  public <T> void setBulkAttribute(AttributeAccessor<T> accessor, T value) {
    if (getStorageLock().isStorageLockAcquired()) {
      return;
    }

    var command  = new SetBulkAttributeAccessorCommand<>(replacementLog, accessor,
        accessor.getValue(), value, selectionBuffer.getSelectedSegments());
    changeCommandBuffer.addAndExecuteCommand(command);
  }

  @Override
  public void addShownAttributeType(AttributeType attributeType) {
    if (getStorageLock().isStorageLockAcquired()) {
      return;
    }

    this.applicationDataSystem.addShownAttributeType(attributeType);
  }

  @Override
  public void removeShownAttributeType(AttributeType attributeType) {
    if (getStorageLock().isStorageLockAcquired()) {
      return;
    }

    this.applicationDataSystem.removeShownAttributeType(attributeType);
  }

  @Override
  public SortedBox<AttributeAccessor<?>> getBulkEditAccessors() {
    var elements = selectionBuffer.getSelectedSegments();

    if (elements.isEmpty()) {
      return new RoseSortedBox<>(List.of());
    } else {
      // O(#types)
      List<AttributeType> types = new ArrayList<>();
      for (var accessor : elements.stream().findAny().get().getAttributeAccessors()) {
        types.add(accessor.getAttributeType());
      }

      // O(#elements * #types^2)
      types.removeIf(type -> !type.isBulkable()
          || !elements.stream().allMatch(element -> getAccessorForType(element, type) != null));

      List<AttributeAccessor<?>> bulkAccessors = types.stream()
          .map(type -> switch (type.getDataType()) {
            case STRING -> bulkAccessor(elements, type, String.class);
            case INTEGER -> bulkAccessor(elements, type, Integer.class);
            case FRACTIONAL -> bulkAccessor(elements, type, Double.class);
            case BOOLEAN -> bulkAccessor(elements, type, Boolean.class);
            case SPEED_LIMIT -> bulkAccessor(elements, type, SpeedLimit.class);
          }).collect(Collectors.toList());

      return new RoseSortedBox<>(bulkAccessors);
    }
  }

  private static <T> AttributeAccessor<T> bulkAccessor(List<Segment> elements,
                                                       AttributeType type, Class<T> clazz) {
    @SuppressWarnings("unchecked")
    List<AttributeAccessor<T>> accessors =
        elements.stream()
            .map(element -> (AttributeAccessor<T>) getAccessorForType(element, type))
            .toList();

    return new AttributeAccessor<>(type, () -> bulkGet(accessors), value -> bulkSet(accessors,
        value));
  }

  private static <T> T bulkGet(List<AttributeAccessor<T>> containedAccessors) {
    T value = containedAccessors.stream().findAny().get().getValue();

    for (var accessor : containedAccessors) {
      if (!Objects.equals(accessor.getValue(), value)) {
        return null;
      }
    }
    return value;
  }

  private static <T> void bulkSet(List<AttributeAccessor<T>> containedAccessors, T value) {
    for (var accessor : containedAccessors) {
      accessor.setValue(value);
    }
  }

  private static AttributeAccessor<?> getAccessorForType(Element element, AttributeType type) {
    for (var accessor : element.getAttributeAccessors()) {
      if (accessor.getAttributeType() == type) {
        return accessor;
      }
    }
    return null;
  }

}
