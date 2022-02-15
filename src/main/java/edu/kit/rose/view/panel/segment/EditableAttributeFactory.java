package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import java.util.List;

/**
 * The editable attribute factory can generate {@link EditableAttribute}s from
 * {@link AttributeAccessor}s.
 */
class EditableAttributeFactory {
  private final AttributeController controller;

  /**
   * Initializes the factory.
   *
   * @param controller the attribute value update handler to pass to all created
   *     {@link EditableAttribute}s.
   */
  public EditableAttributeFactory(AttributeController controller) {
    this.controller = controller;
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
      case STRING -> new StringAttribute((AttributeAccessor<String>) attribute, controller);
      case FRACTIONAL -> new FractionalAttribute((AttributeAccessor<Double>) attribute, controller);
      case BOOLEAN -> new BooleanAttribute((AttributeAccessor<Boolean>) attribute, controller);
      case INTEGER -> new IntegerAttribute((AttributeAccessor<Integer>) attribute, controller);
      case SPEED_LIMIT -> new SpeedLimitAttribute((AttributeAccessor<SpeedLimit>) attribute,
          controller);
      default -> throw new IllegalArgumentException("unknown data type");
    };
  }
}
