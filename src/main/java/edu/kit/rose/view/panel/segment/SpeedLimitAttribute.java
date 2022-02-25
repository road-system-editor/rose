package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
                   AttributeController controller) {
    super(attribute, controller, Arrays.asList(SpeedLimit.values()));
  }

  @Override
  protected String localizeOption(SpeedLimit option) {
    Objects.requireNonNull(option);

    return EnumLocalizationUtility.localizeSpeedLimitTypeTitle(getTranslator(), option);
  }
}