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
import java.util.HashSet;
import java.util.Objects;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

/**
 * Modells a Compatiblity Criterion. See Pflichtenheft: "Kompatibilitätskriterium"
 */
public class CompatibilityCriterion extends RoseSetObservable<SegmentType,
        PlausibilityCriterion> implements PlausibilityCriterion {
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
  private final Set<SegmentType> segmentTypes;
  private final MultiValuedMap<Element, Violation> elementViolationMap;
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
    this.elementViolationMap = new HashSetValuedHashMap<>();
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
        if (violation.violatedCriterion() == this) {
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

    if (unit.isContainer()) {
      return;
    }
    Segment segment = (Segment) unit;

    //The Criterion does not know when a segment gets removed.
    //So here we check if it is still part of the RoadSystem and remove all Violations if it is not.
    if (!this.roadSystem.getElements().contains(segment)) {
      removeViolationsOfSegment(segment);
    }

    if (this.operatorType != null) {
      ValidationStrategy<?> strategy = TYPE_TO_STRATEGY_MAP.get(this.operatorType);
      Boolean useDiscrepancy = TYPE_TO_DISCREPANCY_MAP.get(this.operatorType);
      ArrayList<Segment> invalidSegments = getInvalidSegments(strategy, segment, useDiscrepancy);

      updateViolations(invalidSegments, segment);
    }
  }

  private void removeViolationsOfSegment(Segment segment) {
    HashSetValuedHashMap<Element, Violation> elementViolationMapCopy =
        new HashSetValuedHashMap<>(elementViolationMap);
    if (elementViolationMap.containsKey(segment)) {
      for (Violation vio : elementViolationMapCopy.get(segment)) {
        this.violationManager.removeViolation(vio);
        elementViolationMap.removeMapping(segment, vio);
      }
    }
  }

  private void updateViolations(List<Segment> invalidSegments, Segment segment) {
    HashSetValuedHashMap<Element, Violation> elementViolationMapCopy =
        new HashSetValuedHashMap<>(elementViolationMap);
    if (!invalidSegments.isEmpty()
        && this.segmentTypes.contains((segment).getSegmentType())) {
      if (elementViolationMapCopy.containsKey(segment)) {
        for (Violation vio : elementViolationMapCopy.get(segment)) {
          if (!(vio.offendingSegments().size() == invalidSegments.size())
              || !vio.offendingSegments().containsAll(invalidSegments)) {

            if (this.elementViolationMap.containsKey(segment)) {
              this.violationManager.removeViolation(vio);
              elementViolationMap.removeMapping(segment, vio);
            }
          }
        }
      }

      for (Segment seg : invalidSegments) {
        Violation violation = new Violation(this, List.of(seg, segment));
        this.violationManager.addViolation(violation);
        this.elementViolationMap.put(segment, violation);
      }

    } else {
      removeViolationsOfSegment(segment);
    }
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

  @Override
  public PlausibilityCriterion getThis() {
    return this;
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

  @Override
  public void notifyAddition(Element unit) {
    notifyChange(unit);
  }

  @Override
  public void notifyRemoval(Element unit) {
    if (!unit.isContainer()) {
      removeViolationsOfSegment((Segment) unit);
    }
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
