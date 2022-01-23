package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
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
  @SuppressWarnings("unchecked")
  public EditableAttribute<?> forAttribute(AttributeAccessor<?> attribute) {
    switch (attribute.getAttributeType()) {
      case NAME:
        return new StringAttribute((AttributeAccessor<String>) attribute, controller);
      case SLOPE:
        return new FractionalAttribute((AttributeAccessor<Double>) attribute, controller);
      case CONURBATION:
        return new SelectableAttribute<>((AttributeAccessor<Boolean>) attribute, controller,
            List.of(true, false), null); //TODO add localizer
      case MAX_SPEED:
      case MAX_SPEED_RAMP:
      case LANE_COUNT:
      case LANE_COUNT_RAMP:
      case LENGTH:
        return new IntegerAttribute((AttributeAccessor<Integer>) attribute, controller);
      default:
        return null;
    }
  }
}
