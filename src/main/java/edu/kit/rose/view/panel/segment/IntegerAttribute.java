package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;


/**
 * This is the {@link EditableAttribute} implementation for the
 * {@link edu.kit.rose.model.roadsystem.DataType} {@code INTEGER}.
 */
class IntegerAttribute extends TextFieldAttribute<Integer> {

  private static final Pattern INPUT_PATTERN = Pattern.compile("^[+-]?[0-9]*$");

  /**
   * Creates a new integer attribute editor for the given {@code attribute}.
   *
   * @param attribute the attribute to display.
   * @param controller the controller that should handle attribute value updates.
   */
  IntegerAttribute(AttributeAccessor<Integer> attribute, AttributeController controller,
                   BiConsumer<AttributeAccessor<Integer>, Integer> consumer) {
    super(attribute, controller, consumer);
  }

  @Override
  protected boolean validate(String input) {
    if (!INPUT_PATTERN.matcher(input).matches()) {
      return false;
    }

    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  protected Integer parse(String input) {
    return Integer.parseInt(input);
  }
}
