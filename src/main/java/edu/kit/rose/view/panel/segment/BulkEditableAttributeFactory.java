package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;

/**
 * The editable attribute factory can generate {@link EditableAttribute}s from
 * {@link AttributeAccessor}s. That will be used for bulk edits.
 */
class BulkEditableAttributeFactory {
  private final AttributeController controller;

  /**
   * Initializes the factory.
   *
   * @param controller the attribute value update handler to pass to all created
   *     {@link EditableAttribute}s.
   */
  public BulkEditableAttributeFactory(AttributeController controller) {
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
      case STRING -> new StringAttribute((AttributeAccessor<String>) attribute, controller,
          this.controller::setBulkAttribute);
      case FRACTIONAL -> new FractionalAttribute((AttributeAccessor<Double>) attribute, controller,
          this.controller::setAttribute);
      case BOOLEAN -> new BooleanAttribute((AttributeAccessor<Boolean>) attribute, controller,
          this.controller::setAttribute);
      case INTEGER -> new IntegerAttribute((AttributeAccessor<Integer>) attribute, controller,
          this.controller::setAttribute);
      case SPEED_LIMIT -> new SpeedLimitAttribute((AttributeAccessor<SpeedLimit>) attribute,
          controller,
          this.controller::setAttribute);
      default -> throw new IllegalArgumentException("unknown data type");
    };
  }
}
