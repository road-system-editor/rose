package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.validation.EqualsValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.LessThanValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.NorValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.NotEqualsValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.OrValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Modells a Compatiblity Criterion. See Pflichtenheft: "Kompatibilitätskriterium"
 */
public class CompatibilityCriterion extends RoseSetObservable<SegmentType,
        PlausibilityCriterion> implements PlausibilityCriterion {
  private static final boolean USE_DISCREPANCY = true;
  private static final boolean NOT_USE_DISCREPANCY = false;
  private final Set<SegmentType> segmentTypes;
  private final HashMap<Element, Violation> elementViolationMap;
  private String name;
  private AttributeType attributeType;
  private ValidationType operatorType;
  private double discrepancy;
  private RoadSystem roadSystem;
  private ViolationManager violationManager;

  /**
   * Constructor.
   *
   * @param roadSystem       The Roadsystem this Criterion applied to.
   * @param violationManager manager to which violations will be added
   */
  public CompatibilityCriterion(RoadSystem roadSystem, ViolationManager violationManager) {
    Objects.requireNonNull(violationManager);
    this.name = "";
    this.discrepancy = 0;
    this.segmentTypes = new HashSet<>();
    this.violationManager = violationManager;
    this.roadSystem = roadSystem;
    this.elementViolationMap = new HashMap<>();
  }

  /**
   * Sets the {@link RoadSystem} used by this CompatibilityCriterion.
   * Checks do not work until this method has been called at least once with a valid roadSystem.
   *
   * @param roadSystem the roadSystem.
   */
  public void setRoadSystem(RoadSystem roadSystem) {
    this.roadSystem = roadSystem;
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
   * Provides the type of Operator this Criterion is using.
   *
   * @return the type of Operator this Criterion is using.
   */
  public ValidationType getOperatorType() {
    return this.operatorType;
  }

  /**
   * Sets the Type of Operator this Criterion is supposed to use.
   *
   * @param operatorType the Type of Operator this Criterion is supposed to use.
   */
  public void setOperatorType(ValidationType operatorType) {
    if (this.operatorType != operatorType) {
      this.operatorType = operatorType;
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
  public SortedBox<ValidationType> getCompatibleOperatorTypes() {
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
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    if (!this.name.equals(name)) {
      this.name = name;
      notifySubscribers();
    }
  }

  @Override
  public Box<SegmentType> getSegmentTypes() {
    return new RoseBox<>(this.segmentTypes);
  }

  @Override
  public PlausibilityCriterionType getType() {
    return PlausibilityCriterionType.COMPATIBILITY;
  }

  @Override
  public void setViolationManager(ViolationManager violationManager) {
    if (this.violationManager != violationManager) {
      SortedBox<Violation> violations = this.violationManager.getViolations();
      for (var violation : violations) {
        if (violation.violatedCriterion().getType()
                .equals(PlausibilityCriterionType.COMPATIBILITY)) {
          this.violationManager.removeViolation(violation);
        }
      }
      this.violationManager = violationManager;
      checkAll();
    }
  }

  @Override
  public void addSegmentType(SegmentType type) {
    if (this.segmentTypes.add(type)) {
      subscribers.forEach(s -> s.notifyAddition(type));
      checkAll();
      notifySubscribers();
    }
  }

  @Override
  public void removeSegmentType(SegmentType type) {
    if (this.segmentTypes.remove(type)) {
      subscribers.forEach(s -> s.notifyRemoval(type));
      checkAll();
      notifySubscribers();
    }
  }

  @Override
  public void notifyChange(Element unit) {
    if (this.roadSystem == null) {
      throw new IllegalStateException("can not check connections without set roadSystem.");
    }
    ArrayList<Segment> invalidSegments;
    if (this.operatorType != null && !unit.isContainer()) {
      ValidationStrategy<?> strategy;

      switch (this.operatorType) {
        case EQUALS -> {
          strategy = new EqualsValidationStrategy<>();
          invalidSegments = getInvalidSegments(strategy, (Segment) unit, NOT_USE_DISCREPANCY);
        }
        case LESS_THAN -> {
          strategy = new LessThanValidationStrategy<>();
          invalidSegments = getInvalidSegments(strategy, (Segment) unit, USE_DISCREPANCY);
        }
        case NOR -> {
          strategy = new NorValidationStrategy();
          invalidSegments = getInvalidSegments(strategy, (Segment) unit, NOT_USE_DISCREPANCY);
        }
        case NOT_EQUALS -> {
          strategy = new NotEqualsValidationStrategy<>();
          invalidSegments = getInvalidSegments(strategy, (Segment) unit, NOT_USE_DISCREPANCY);
        }
        case OR -> {
          strategy = new OrValidationStrategy();
          invalidSegments = getInvalidSegments(strategy, (Segment) unit, NOT_USE_DISCREPANCY);
        }
        default -> throw new IllegalArgumentException("invalid operator type");
      }
      if (!invalidSegments.isEmpty()
              && this.segmentTypes.contains(((Segment) unit).getSegmentType())) {
        if (!coveredByOtherSegments(invalidSegments, (Segment) unit)) {
          if (this.elementViolationMap.containsKey(unit)) {
            notifyRemoval(unit);
          }
          invalidSegments.add((Segment) unit);
          Violation violation = new Violation(this, invalidSegments);
          this.violationManager.addViolation(violation);
          this.elementViolationMap.put(unit, violation);
        }
      } else if (elementViolationMap.containsKey(unit)) {
        notifyRemoval(unit);
      }
    }
  }


  @Override
  public void notifyAddition(Element unit) {
    notifyChange(unit);
  }

  @Override
  public void notifyRemoval(Element unit) {
    this.violationManager.removeViolation(this.elementViolationMap.get(unit));
    this.elementViolationMap.remove(unit);
  }

  @Override
  public PlausibilityCriterion getThis() {
    return this;
  }

  /**
   * Checks if the segments, that the unit is not compatible with, did not already
   * cause a violation where this unit is already invoked.
   * Example: only 2 segments on the road. segment1 is not compatible with segment2.
   * after the segment1 was checked a violation was created. When segment2 is being checked
   * a new violation should not be created.
   *
   * @param invalidSegments the segments that are not compatible with the unit
   * @param unit            the unit that is being checked
   * @return true if the invalid segments already caused violation where the unit was invoked.
   */
  private boolean coveredByOtherSegments(ArrayList<Segment> invalidSegments, Segment unit) {
    for (var segment : invalidSegments) {
      if (!(elementViolationMap.containsKey(segment)
              && elementViolationMap.get(segment).offendingSegments().contains(unit))) {
        return false;
      }
    }
    return true;
  }

  private ArrayList<Segment> getInvalidSegments(ValidationStrategy<?> strategy,
                                                Segment segment, boolean useDiscrepancy) {
    ArrayList<Segment> invalidSegments = new ArrayList<>();
    Box<Segment> adjacentSegments = this.roadSystem.getAdjacentSegments(segment);
    AttributeAccessor<?> segmentAccessor =
            getAccessorOfType(segment.getAttributeAccessors(), this.attributeType);


    for (Segment adjacentSegment : adjacentSegments) {
      if (this.segmentTypes.contains(adjacentSegment.getSegmentType())) {
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

  private void checkAll() {
    if (this.roadSystem != null && this.attributeType != null && this.operatorType != null) {
      roadSystem.getElements().forEach(this::notifyChange);
    }
  }
}
