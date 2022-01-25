package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.regex.Pattern;

/**
 * This is the {@link EditableAttribute} implementation for the
 * {@link edu.kit.rose.model.roadsystem.DataType} {@code FRACTIONAL}.
 */
class FractionalAttribute extends TextFieldAttribute<Double> {

  private static final Pattern INPUT_PATTERN = Pattern.compile("^[+-]?[0-9]*\\.?[0-9]+$");

  /**
   * Creates a new fractional attribute editor for the given {@code attribute}.
   *
   * @param attribute  the attribute to display.
   * @param controller the controller that should handle attribute value updates.
   */
  FractionalAttribute(AttributeAccessor<Double> attribute, AttributeController controller) {
    super(attribute, controller);
  }

  @Override
  protected boolean validate(String input) {
    if (!INPUT_PATTERN.matcher(input).matches()) {
      return false;
    }

    try {
      Double.parseDouble(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  protected Double parse(String input) {
    return Double.parseDouble(input);
  }
}
