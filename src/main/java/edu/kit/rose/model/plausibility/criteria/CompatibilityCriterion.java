package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.validation.EqualsValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.LessThanValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.NorValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.NotEqualsValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.OperatorType;
import edu.kit.rose.model.plausibility.criteria.validation.OrValidationStrategy;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationStrategy;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Modells a Compatiblity Criterion. See Pflichtenheft: "Kompatibilitätskriterium"
 */
public class CompatibilityCriterion extends RoseSetObservable<SegmentType,
        PlausibilityCriterion> implements PlausibilityCriterion {
  private static final boolean USE_DISCREPANCY = true;
  private static final boolean NOT_USE_DISCREPANCY = false;
  private String name;
  private AttributeType attributeType;
  private OperatorType operatorType;
  private double discrepancy;
  private final Set<SegmentType> segmentTypes;
  private final RoadSystem roadSystem;
  private final ViolationManager violationManager;

  /**
   * Constructor.
   *
   * @param roadSystem The Roadsystem this Criterion applied to.
   * @param violationManager manager to which violations will be added
   */
  CompatibilityCriterion(RoadSystem roadSystem, ViolationManager violationManager) {
    this.name = "";
    this.discrepancy = 0;
    this.segmentTypes = new HashSet<>();
    this.violationManager = violationManager;
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
    this.attributeType = attributeType;
  }

  /**
   * Provides the type of Operator this Criterion is using.
   *
   * @return the type of Operator this Criterion is using.
   */
  public OperatorType getOperatorType() {
    return this.operatorType;
  }

  /**
   * Sets the Type of Operator this Criterion is supposed to use.
   *
   * @param operatorType the Type of Operator this Criterion is supposed to use.
   */
  public void setOperatorType(OperatorType operatorType) {
    this.operatorType = operatorType;
  }

  /**
   * Gives a {@link SortedBox} of {@link OperatorType}s that contains all Types that are
   * compatible with this criterion.
   * (This is depending on the AttributeType this criterion has)
   *
   * @return containing all {@link OperatorType}s that are compatible with this criterion.
   */
  public SortedBox<OperatorType> getCompatibleOperatorTypes() {
    return new RoseSortedBox<>(Arrays.stream(OperatorType.values()).filter(e -> e.getCompatible()
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
    this.discrepancy = discrepancy;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Box<SegmentType> getSegmentTypes() {
    return null;
  }

  @Override
  public PlausibilityCriterionType getType() {
    return PlausibilityCriterionType.COMPATIBILITY;
  }

  @Override
  public void addSegmentType(SegmentType type) {
    this.segmentTypes.add(type);
  }

  @Override
  public void removeSegmentType(SegmentType type) {
    this.segmentTypes.remove(type);
  }

  @Override
  public void notifyChange(Element unit) {
    ArrayList<Segment> invalidSegments;
    if (!this.operatorType.equals(OperatorType.DEFAULT)) {
      ValidationStrategy strategy;

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
          strategy = new NorValidationStrategy<>();
          invalidSegments = getInvalidSegments(strategy, (Segment) unit, NOT_USE_DISCREPANCY);
        }
        case NOT_EQUALS -> {
          strategy = new NotEqualsValidationStrategy<>();
          invalidSegments = getInvalidSegments(strategy, (Segment) unit, NOT_USE_DISCREPANCY);
        }
        case OR -> {
          strategy = new OrValidationStrategy<>();
          invalidSegments = getInvalidSegments(strategy, (Segment) unit, NOT_USE_DISCREPANCY);
        }
        default -> throw new InvalidParameterException("invalid operator type");
      }
      if (!invalidSegments.isEmpty()
              && this.segmentTypes.contains(((Segment) unit).getSegmentType())) {
        invalidSegments.add((Segment) unit);
        this.violationManager.addViolation(new Violation(this, invalidSegments));
      }
    }
  }

  @Override
  public PlausibilityCriterion getThis() {
    return this;
  }

  private ArrayList<Segment> getInvalidSegments(ValidationStrategy strategy,
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
   * @param strategy        the strategy to be validated
   * @param accessor1       first accessor to be checked
   * @param accessor2       second accessor to be checked
   * @param useDiscrepancy  true the strategy requires discrepancy
   *                        and false otherwise
   * @return                true if the accessors are valid to each other according
   *                        to validation strategy and false otherwise
   */
  private boolean checkValid(ValidationStrategy strategy,
                             AttributeAccessor accessor1, AttributeAccessor accessor2,
                             boolean useDiscrepancy) {
    switch (this.attributeType.getDataType()) {
      case BOOLEAN -> {
        AttributeAccessor<Boolean> auxAccessor1 = (AttributeAccessor<Boolean>) accessor1;
        AttributeAccessor<Boolean> auxAccessor2 = (AttributeAccessor<Boolean>) accessor2;
        ValidationStrategy<Boolean> auxStrategy = (ValidationStrategy<Boolean>) strategy;
        if (useDiscrepancy) {
          return auxStrategy.validate(auxAccessor1.getValue(),
                  auxAccessor2.getValue(), this.discrepancy);
        }
        return auxStrategy.validate(auxAccessor1.getValue(), auxAccessor2.getValue());
      }
      case STRING -> {
        AttributeAccessor<String> auxAccessor1 = (AttributeAccessor<String>) accessor1;
        AttributeAccessor<String> auxAccessor2 = (AttributeAccessor<String>) accessor2;
        ValidationStrategy<String> auxStrategy = (ValidationStrategy<String>) strategy;
        if (useDiscrepancy) {
          return auxStrategy.validate(auxAccessor1.getValue(),
                  auxAccessor2.getValue(), this.discrepancy);
        }
        return auxStrategy.validate(auxAccessor1.getValue(), auxAccessor2.getValue());
      }
      case INTEGER -> {
        AttributeAccessor<Integer> auxAccessor1 = (AttributeAccessor<Integer>) accessor1;
        AttributeAccessor<Integer> auxAccessor2 = (AttributeAccessor<Integer>) accessor2;
        ValidationStrategy<Integer> auxStrategy = (ValidationStrategy<Integer>) strategy;
        if (useDiscrepancy) {
          return auxStrategy.validate(auxAccessor1.getValue(),
                  auxAccessor2.getValue(), this.discrepancy);
        }
        return auxStrategy.validate(auxAccessor1.getValue(), auxAccessor2.getValue());
      }
      case FRACTIONAL -> {
        AttributeAccessor<Double> auxAccessor1 = (AttributeAccessor<Double>) accessor1;
        AttributeAccessor<Double> auxAccessor2 = (AttributeAccessor<Double>) accessor2;
        ValidationStrategy<Double> auxStrategy = (ValidationStrategy<Double>) strategy;
        if (useDiscrepancy) {
          return auxStrategy.validate(auxAccessor1.getValue(),
                  auxAccessor2.getValue(), this.discrepancy);
        }
        return auxStrategy.validate(auxAccessor1.getValue(), auxAccessor2.getValue());
      }
      default -> throw new IllegalArgumentException("no such data type found");
    }
  }

  @Override
  public void notifyAddition(Element unit) {

  }

  @Override
  public void notifyRemoval(Element unit) {

  }
}
