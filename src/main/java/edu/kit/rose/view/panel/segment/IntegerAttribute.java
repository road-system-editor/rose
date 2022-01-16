package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import javafx.scene.Node;

/**
 * This is the {@link EditableAttribute} implementation for the {@link edu.kit.rose.model.roadsystem.DataType} {@code INTEGER}.
 */
class IntegerAttribute extends EditableAttribute<Integer> {
  /**
   * Creates a new integer attribute editor for the given {@code attribute}.
   *
   * @param attribute
   * @param controller
   */
  IntegerAttribute(AttributeAccessor<Integer> attribute, AttributeController controller) {
    super(attribute, controller);
  }

  @Override
  protected Node createInputField() {
    return null;
  }

  @Override
  public void notifyChange(AttributeAccessor<Integer> unit) {

  }
}
