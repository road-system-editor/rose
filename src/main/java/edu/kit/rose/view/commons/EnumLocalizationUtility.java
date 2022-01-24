package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;

/**
 * Utility class that helps with localizing enum value names.
 */
public final class EnumLocalizationUtility {
  /**
   * Returns the localized title of a measurement type.
   */
  public static String localizeMeasurementTypeTitle(LocalizedTextProvider translator,
                                                    MeasurementType type) {
    return translator.getLocalizedText(String.format("measurementType.%s.title", type.toString()));
  }

  /**
   * Returns the localized title of an attribute type.
   */
  public static String localizeAttributeTypeTitle(LocalizedTextProvider translator,
                                                    AttributeType type) {
    return translator.getLocalizedText(String.format("attributeType.%s.title", type.toString()));
  }


}
