package edu.kit.rose.model.plausibility.criteria;


import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Range;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.DataType;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A ValueCriterion represents a Value Criterion (see Pflichtenheft: "Wertebereichkriterium")
 * This checks if the value of an {@link edu.kit.rose.model.roadsystem.attributes.AttributeType}
 * of a {@link Segment} is legal.
 */
class ValueCriterion extends RoseSetObservable<SegmentType, PlausibilityCriterion>
        implements PlausibilityCriterion {
  protected static final Range<Double> LENGTH_RANGE = new Range<>(1.0, 5000.0);
  protected static final Range<Double> LANE_COUNT_RANGE = new Range<>(1.0, 10.0);
  protected static final Range<Double> LANE_COUNT_RAMP_RANGE = new Range<>(1.0, 10.0);
  protected static final Range<Double> SLOPE_RANGE = new Range<>(-10.0, 10.0);

  private String name;
  private final AttributeType attributeType;
  private final Range<Double> range;
  private final Set<SegmentType> segmentTypes;
  private ViolationManager violationManager;
  private final HashMap<Element, Violation> elementViolationMap;


  /**
   * Constructor.
   *
   * @param violationManager manager to which violations will be added
   */
  public ValueCriterion(ViolationManager violationManager,
                        AttributeType type, Range<Double> range) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(range);
    if (type.getDataType() != DataType.INTEGER && type.getDataType() != DataType.FRACTIONAL) {
      throw new IllegalArgumentException("the value criterion can evaluate only attributes"
              + "with integer, fractional, or speed limit data types");
    }
    this.name = "";
    this.segmentTypes = new HashSet<>();
    this.violationManager = violationManager;
    this.elementViolationMap = new HashMap<>();
    this.attributeType = type;
    this.range = range;
  }

  public AttributeType getAttributeType() {
    return this.attributeType;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
    notifySubscribers();
  }

  @Override
  public Box<SegmentType> getSegmentTypes() {
    return new RoseBox<>(this.segmentTypes);
  }

  public PlausibilityCriterionType getType() {
    return PlausibilityCriterionType.VALUE;
  }

  @Override
  public void setViolationManager(ViolationManager violationManager) {
    this.violationManager = violationManager;
  }

  @Override
  public void addSegmentType(SegmentType type) {
    this.segmentTypes.add(type);
    Iterator<SetObserver<SegmentType, PlausibilityCriterion>> iterator = getSubscriberIterator();
    while (iterator.hasNext()) {
      iterator.next().notifyAddition(type);
    }
  }

  @Override
  public void removeSegmentType(SegmentType type) {
    this.segmentTypes.remove(type);
    Iterator<SetObserver<SegmentType, PlausibilityCriterion>> iterator = getSubscriberIterator();
    while (iterator.hasNext()) {
      iterator.next().notifyRemoval(type);
    }
  }

  @Override
  public void notifyChange(Element unit) {
    Segment segment = (Segment) unit;
    if (!unit.isContainer()) {
      if (this.segmentTypes.contains(segment.getSegmentType())) {
        SortedBox<AttributeAccessor<?>> accessors = unit.getAttributeAccessors();
        for (AttributeAccessor<?> accessor : accessors) {
          if (accessor.getAttributeType().equals(this.attributeType)) {
            if (!checkValue(accessor)) {
              if (!this.elementViolationMap.containsKey(unit)) {
                Violation violation = new Violation(this, List.of((Segment) unit));
                this.violationManager.addViolation(violation);
                this.elementViolationMap.put(unit, violation);
              }
            } else if (elementViolationMap.containsKey(unit)) {
              this.violationManager.removeViolation(elementViolationMap.get(unit));
              this.elementViolationMap.remove(unit);
            }
          }
        }
      }
    }
  }

  @Override
  public ValueCriterion getThis() {
    return this;
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

  private boolean checkValue(AttributeAccessor<?> accessor) {
    var dataType = accessor.getAttributeType().getDataType();
    if (dataType == DataType.INTEGER) {
      return this.range.contains(((Integer) accessor.getValue()).doubleValue());
    } else if (dataType == DataType.FRACTIONAL) {
      return this.range.contains((Double) accessor.getValue());
    } else {
      SpeedLimit value = (SpeedLimit) accessor.getValue();
      return false;
    }
  }
}
