package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * This is the {@link EditableAttribute} implementation for the
 * {@link edu.kit.rose.model.roadsystem.DataType} {@code BOOLEAN}.
 */
class BooleanAttribute extends SelectableAttribute<Boolean> {
  /**
   * Creates a new boolean attribute editor for the given {@code attribute}.
   *
   * @param attribute the attribute to display.
   * @param controller the controller that should handle attribute value updates.
   */
  BooleanAttribute(AttributeAccessor<Boolean> attribute,
                   AttributeController controller,
                   BiConsumer<AttributeAccessor<Boolean>, Boolean> consumer) {
    super(attribute, controller, List.of(true, false), consumer);
  }

  @Override
  protected String localizeOption(Boolean option) {
    Objects.requireNonNull(option);

    return EnumLocalizationUtility.localizeBooleanTitle(getTranslator(), option);
  }
}
