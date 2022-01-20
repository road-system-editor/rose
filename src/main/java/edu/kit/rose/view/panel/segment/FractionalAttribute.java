package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * This is the {@link EditableAttribute} implementation for the
 * {@link edu.kit.rose.model.roadsystem.DataType} {@code FRACTIONAL}.
 */
class FractionalAttribute extends EditableAttribute<Double> {
  /**
   * Creates a new fractional attribute editor for the given {@code attribute}.
   *
   * @param attribute the attribute to display.
   * @param controller the controller that should handle attribute value updates.
   */
  FractionalAttribute(AttributeAccessor<Double> attribute, AttributeController controller) {
    super(attribute, controller);
  }

  @Override
  public void notifyChange(AttributeAccessor<Double> unit) {

  }

  @Override
  protected Node createInputField() {
    return new TextField();
  }
}
