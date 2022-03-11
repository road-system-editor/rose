package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * This is the {@link EditableAttribute} implementation for the
 * {@link edu.kit.rose.model.roadsystem.DataType} {@code SPEED_LIMIT}.
 */
class SpeedLimitAttribute extends SelectableAttribute<SpeedLimit> {
  /**
   * Creates a new boolean attribute editor for the given {@code attribute}.
   *
   * @param attribute the attribute to display.
   * @param controller the controller that should handle attribute value updates.
   */
  SpeedLimitAttribute(AttributeAccessor<SpeedLimit> attribute,
                   AttributeController controller,
                      BiConsumer<AttributeAccessor<SpeedLimit>, SpeedLimit> consumer) {
    super(attribute, controller, Arrays.asList(SpeedLimit.values()), consumer);
  }

  @Override
  protected String localizeOption(SpeedLimit option) {
    Objects.requireNonNull(option);

    return EnumLocalizationUtility.localizeSpeedLimitTypeTitle(getTranslator(), option);
  }
}
