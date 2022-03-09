package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.validation.EqualsValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.LessThanValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.NorValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.NotEqualsValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.OrValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Modells a Compatiblity Criterion. See Pflichtenheft: "Kompatibilitätskriterium"
 */
public class CompatibilityCriterion extends AbstractCompatibilityCriterion {
  private static final boolean USE_DISCREPANCY = true;
  private static final boolean NOT_USE_DISCREPANCY = false;
  private static final Map<ValidationType, ValidationStrategy<?>> TYPE_TO_STRATEGY_MAP =
      Map.of(ValidationType.EQUALS, new EqualsValidationStrategy<>(),
          ValidationType.NOT_EQUALS, new NotEqualsValidationStrategy<>(),
          ValidationType.NOR, new NorValidationStrategy(),
          ValidationType.OR, new OrValidationStrategy(),
          ValidationType.LESS_THAN, new LessThanValidationStrategy<>());
  private static final Map<ValidationType, Boolean> TYPE_TO_DISCREPANCY_MAP =
      Map.of(ValidationType.EQUALS, NOT_USE_DISCREPANCY,
          ValidationType.NOT_EQUALS, NOT_USE_DISCREPANCY,
          ValidationType.NOR, NOT_USE_DISCREPANCY,
          ValidationType.OR, NOT_USE_DISCREPANCY,
          ValidationType.LESS_THAN, USE_DISCREPANCY);


  private AttributeType attributeType;
  private ValidationType validationType;
  private double discrepancy;


  /**
   * Creates a new compatibility criterion with default settings.
   *
   * @param roadSystem the road system this criterion applies to. This may be {@code null} but it
   *     must be set before this criterion is able to receive notifications.
   * @param violationManager manager to which violations will be added. This may be {@code null} but
   *     it must be set before this criterion is able to receive notifications.
   */
  public CompatibilityCriterion(RoadSystem roadSystem, ViolationManager violationManager) {
    super(roadSystem, violationManager);
    this.discrepancy = 0;
  }

  /**
   * Provides the AttributeType that this Criterion is checking.
   *
   * @return the AttributeType that this Criterion is checking.
   */
  public AttributeType getAttributeType() {
    return this.attributeType;
  }

  /**
   * Sets the AttributeType that this Criterion is supposed to check.
   *
   * @param attributeType the AttributeType that this Criterion is supposed to check.
   */
  public void setAttributeType(AttributeType attributeType) {
    if (this.attributeType != attributeType) {
      this.attributeType = attributeType;
      checkAll();
      notifySubscribers();
    }
  }

  /**
   * Provides the type of Validation this Criterion is using.
   *
   * @return the type of Validation this Criterion is using.
   */
  public ValidationType getValidationType() {
    return this.validationType;
  }

  /**
   * Sets the Type of Validation this Criterion is supposed to use.
   *
   * @param validationType the Type of Validation this Criterion is supposed to use.
   */
  public void setValidationType(ValidationType validationType) {
    if (this.validationType != validationType) {
      this.validationType = validationType;
      checkAll();
      notifySubscribers();
    }
  }

  /**
   * Gives a {@link SortedBox} of {@link ValidationType}s that contains all Types that are
   * compatible with this criterion.
   * (This is depending on the AttributeType this criterion has)
   *
   * @return containing all {@link ValidationType}s that are compatible with this criterion.
   */
  public SortedBox<ValidationType> getCompatibleValidationTypes() {
    return new RoseSortedBox<>(Arrays.stream(ValidationType.values()).filter(e -> e.getCompatible()
            .contains(this.attributeType.getDataType())).collect(Collectors.toList()));
  }

  /**
   * Provides the legal discrepancy numeric values can have to be accepted by this criterion.
   *
   * @return the legal discrepancy numeric values can have to be accepted by this criterion.
   */
  public double getLegalDiscrepancy() {
    return this.discrepancy;
  }

  /**
   * Sets the legal discrepancy numeric values are supposed to have to be accepted by this
   * criterion.
   *
   * @param discrepancy the legal discrepancy numeric values are supposed to have to be accepted
   *                    by this criterion.
   */
  public void setLegalDiscrepancy(double discrepancy) {
    if (this.discrepancy != discrepancy) {
      this.discrepancy = discrepancy;
      checkAll();
      notifySubscribers();
    }
  }


  @Override
  protected void checkCriterion(Segment segment) {
    if (this.validationType != null) {
      ValidationStrategy<?> strategy = TYPE_TO_STRATEGY_MAP.get(this.validationType);
      Boolean useDiscrepancy = TYPE_TO_DISCREPANCY_MAP.get(this.validationType);
      ArrayList<Segment> invalidSegments = getInvalidSegments(strategy, segment, useDiscrepancy);

      updateViolations(invalidSegments, segment);
    }
  }

  private ArrayList<Segment> getInvalidSegments(ValidationStrategy<?> strategy,
                                                Segment segment, boolean useDiscrepancy) {
    ArrayList<Segment> invalidSegments = new ArrayList<>();
    Box<Segment> adjacentSegments = this.getRoadSystem().getAdjacentSegments(segment);
    AttributeAccessor<?> segmentAccessor =
            getAccessorOfType(segment.getAttributeAccessors(), this.attributeType);

    for (Segment adjacentSegment : adjacentSegments) {
      if (this.getSegmentTypes().contains(adjacentSegment.getSegmentType())) {
        AttributeAccessor<?> adjacentAccessor =
                getAccessorOfType(adjacentSegment.getAttributeAccessors(), this.attributeType);
        if (!checkValid(strategy, segmentAccessor, adjacentAccessor, useDiscrepancy)) {
          invalidSegments.add(adjacentSegment);
        }
      }
    }
    return invalidSegments;
  }

  private AttributeAccessor<?> getAccessorOfType(SortedBox<AttributeAccessor<?>> accessors,
                                                 AttributeType type) {
    for (AttributeAccessor<?> accessor : accessors) {
      if (accessor.getAttributeType().equals(type)) {
        return accessor;
      }
    }
    throw new IllegalArgumentException("the segment does not have such attribute type");
  }

  /**
   * Checks the data type of attribute type use for this criterion
   * and casts the right data types to strategy and accessors.
   *
   * @param strategy       the strategy to be validated
   * @param accessor1      first accessor to be checked
   * @param accessor2      second accessor to be checked
   * @param useDiscrepancy true the strategy requires discrepancy
   *                       and false otherwise
   * @return true if the accessors are valid to each other according
   *      to validation strategy and false otherwise
   */
  private boolean checkValid(ValidationStrategy<?> strategy,
                             AttributeAccessor<?> accessor1, AttributeAccessor<?> accessor2,
                             boolean useDiscrepancy) {
    return switch (this.attributeType.getDataType()) {
      case BOOLEAN -> (this.<Boolean>validateWithType(strategy, accessor1, accessor2,
          useDiscrepancy));
      case STRING -> (this.<String>validateWithType(strategy, accessor1, accessor2,
          useDiscrepancy));
      case INTEGER -> (this.<Integer>validateWithType(strategy, accessor1, accessor2,
          useDiscrepancy));
      case FRACTIONAL -> (this.<Double>validateWithType(strategy, accessor1, accessor2,
          useDiscrepancy));
      default -> throw new IllegalArgumentException("no such data type found");
    };
  }



  @SuppressWarnings("unchecked")
  private <T> boolean validateWithType(ValidationStrategy<?> strategy,
                                       AttributeAccessor<?> accessor1,
                                       AttributeAccessor<?> accessor2,
                                       boolean useDiscrepancy) {
    AttributeAccessor<T> auxAccessor1 = (AttributeAccessor<T>) accessor1;
    AttributeAccessor<T> auxAccessor2 = (AttributeAccessor<T>) accessor2;
    ValidationStrategy<T> auxStrategy = (ValidationStrategy<T>) strategy;
    if (useDiscrepancy) {
      return auxStrategy.validate(auxAccessor1.getValue(),
              auxAccessor2.getValue(), this.discrepancy);
    }
    return auxStrategy.validate(auxAccessor1.getValue(), auxAccessor2.getValue());
  }

  @Override
  protected void checkAll() {
    if (this.getRoadSystem() != null && this.attributeType != null && this.validationType != null) {
      this.getRoadSystem().getElements().forEach(this::notifyChange);
    }
  }

  @Override
  public PlausibilityCriterionType getType() {
    return PlausibilityCriterionType.COMPATIBILITY;
  }
}
