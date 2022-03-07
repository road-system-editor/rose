package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import java.util.function.BiConsumer;


/**
 * The editable attribute factory can generate {@link EditableAttribute}s from
 * {@link AttributeAccessor}s.
 */
class EditableAttributeFactory {
  private final AttributeController controller;
  private final boolean isBulkEdit;

  /**
   * Initializes the factory.
   *
   * @param controller the attribute value update handler to pass to all created
   *     {@link EditableAttribute}s.
   */
  public EditableAttributeFactory(AttributeController controller, boolean isBulkEdit) {
    this.controller = controller;
    this.isBulkEdit = isBulkEdit;
  }

  /**
   * Creates an editable attribute component for a given attribute accessor.
   *
   * @param attribute the attribute to create an editable attribute component for.
   * @return the created editable attribute component.
   */
  @SuppressWarnings({"unchecked", "UnnecessaryDefault"})
  public EditableAttribute<?> forAttribute(AttributeAccessor<?> attribute) {
    return switch (attribute.getAttributeType().getDataType()) {
      case STRING -> new StringAttribute((AttributeAccessor<String>) attribute, controller,
          getConsumer());
      case FRACTIONAL -> new FractionalAttribute((AttributeAccessor<Double>) attribute, controller,
          getConsumer());
      case BOOLEAN -> new BooleanAttribute((AttributeAccessor<Boolean>) attribute, controller,
          getConsumer());
      case INTEGER -> new IntegerAttribute((AttributeAccessor<Integer>) attribute, controller,
          getConsumer());
      case SPEED_LIMIT -> new SpeedLimitAttribute((AttributeAccessor<SpeedLimit>) attribute,
          controller,
          getConsumer());
      default -> throw new IllegalArgumentException("unknown data type");
    };
  }

  private <T> BiConsumer<AttributeAccessor<T>, T> getConsumer() {
    return isBulkEdit ? this.controller::setBulkAttribute : this.controller::setAttribute;
  }
}
