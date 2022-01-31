package edu.kit.rose.view.panel.problem;

import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * The message factory generates short and detailed descriptions of
 * {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion} {@link Violation}s.
 *
 * @param translator the translator to use for localizing problem descriptions.
 */
public record MessageFactory(LocalizedTextProvider translator) {

  /**
   * Generates a short and localized description of the given violation, that informs the
   * user about which segment caused a {@link Violation} against which
   * {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion}.
   *
   * @param violation the violation to describe.
   * @return the generated description.
   */
  public String generateShortDescription(Violation violation) {
    List<String> segmentNames = violation.offendingSegments().stream()
        .map(Segment::getName)
        .toList();

    PlausibilityCriterionType type = violation.violatedCriterion().getType();

    switch (type) {
      case VALUE, COMPLETENESS -> { // criteria with one offending segment
        return String.format(shortDescription(type), segmentNames.get(0));
      }
      case COMPATIBILITY -> { // criteria with two offending segments
        return String.format(shortDescription(type), segmentNames.get(0), segmentNames.get(1));
      }
      default -> throw new IllegalArgumentException("unknown criterion type");
    }
  }

  /**
   * Generates a detailed and localized description of the given {@code violation}, that informs
   * the user about what exactly is causing it.
   *
   * @param violation the violation to describe.
   * @return the generated description.
   */
  public String generateDetailedDescription(Violation violation) {
    List<String> segmentNames = violation.offendingSegments().stream()
        .map(Segment::getName)
        .toList();

    PlausibilityCriterionType type = violation.violatedCriterion().getType();

    switch (type) {
      case VALUE, COMPLETENESS -> {
        return String.format(detailedDescription(type), segmentNames.get(0));
      }
      case COMPATIBILITY -> {
        return String.format(detailedDescription(type),
            segmentNames.get(0),
            segmentNames.get(1),
            violation.violatedCriterion().getName());
      }
      default -> throw new IllegalArgumentException("unknown criterion type");
    }
  }

  private String shortDescription(PlausibilityCriterionType type) {
    return EnumLocalizationUtility.localizeCriterionTypeShortDescription(this.translator, type);
  }

  private String detailedDescription(PlausibilityCriterionType type) {
    return EnumLocalizationUtility.localizeCriterionTypeDetailedDescription(this.translator, type);
  }
}
