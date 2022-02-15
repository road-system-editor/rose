package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;

/**
 * Utility class that helps with localizing enum value names.
 */
public final class EnumLocalizationUtility {
  private static final String MEASUREMENT_TYPE_PREFIX = "measurementType";
  private static final String CRITERION_TYPE_PREFIX = "criterionType";

  /**
   * Returns the localized title of a measurement type.
   */
  public static String localizeMeasurementTypeTitle(LocalizedTextProvider translator,
                                                    MeasurementType type) {
    return translator.getLocalizedText(
        String.format("%s.%s.title", MEASUREMENT_TYPE_PREFIX, type.toString()));
  }

  /**
   * Returns the localized short description of a plausibility criterion type.
   */
  public static String localizeCriterionTypeShortDescription(LocalizedTextProvider translator,
                                                             PlausibilityCriterionType type) {
    return translator.getLocalizedText(
        String.format("%s.%s.shortDescription", CRITERION_TYPE_PREFIX, type.toString()));
  }

  /**
   * Returns the localized detailed description of a plausibility criterion type.
   */
  public static String localizeCriterionTypeDetailedDescription(LocalizedTextProvider translator,
                                                                PlausibilityCriterionType type) {
    return translator.getLocalizedText(
        String.format("%s.%s.detailedDescription", CRITERION_TYPE_PREFIX, type.toString()));
  }

  /**
   * Returns the localized title of an attribute type.
   */
  public static String localizeAttributeTypeTitle(LocalizedTextProvider translator,
                                                    AttributeType type) {
    return translator.getLocalizedText(String.format("attributeType.%s.title", type.toString()));
  }


  /**
   * Returns the localized title of a validation type.
   */
  public static String localizeValidationTypeTitle(LocalizedTextProvider translator,
                                                   ValidationType type) {
    return translator.getLocalizedText(String.format("validationType.%s.title", type.toString()));
  }

  /**
   * Returns the localized title of a segment type.
   */
  public static String localizeSegmentTypeTitle(LocalizedTextProvider translator,
                                                SegmentType type) {
    return translator.getLocalizedText(String.format("segmentType.%s.title", type.toString()));
  }

  /**
   * Returns the localized title of a boolean.
   */
  public static String localizeBooleanTitle(LocalizedTextProvider translator,
                                                Boolean type) {
    return translator.getLocalizedText(String.format("boolean.%s.title", type.toString()));
  }

  /**
   * Returns the localized title of a speed limit.
   */
  public static String localizeSpeedLimitTypeTitle(LocalizedTextProvider translator,
                                                SpeedLimit type) {
    return translator.getLocalizedText(String.format("speedLimit.%s.title", type.toString()));
  }
}
