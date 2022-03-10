package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.function.BiConsumer;

/**
 * This is the {@link EditableAttribute} implementation for the
 * {@link edu.kit.rose.model.roadsystem.DataType} {@code STRING}.
 */
class StringAttribute extends TextFieldAttribute<String> {

  /**
   * Creates a new string attribute editor for the given {@code attribute}.
   *
   * @param attribute the attribute to display.
   * @param controller the controller that should handle attribute value updates.
   */
  StringAttribute(AttributeAccessor<String> attribute, AttributeController controller,
                  BiConsumer<AttributeAccessor<String>, String> consumer) {
    super(attribute, controller, consumer);
  }

  @Override
  protected boolean validate(String input) {
    return true;
  }

  @Override
  protected String parse(String input) {
    return input;
  }


}
