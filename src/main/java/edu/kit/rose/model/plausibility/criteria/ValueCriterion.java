package edu.kit.rose.model.plausibility.criteria;


import com.google.common.collect.Range;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A ValueCriterion represents a Value Criterion (see Pflichtenheft: "Wertebereichkriterium")
 * This checks if the value of an {@link edu.kit.rose.model.roadsystem.attributes.AttributeType}
 * of a {@link Segment} is legal.
 */
class ValueCriterion extends RoseSetObservable<SegmentType, PlausibilityCriterion>
        implements PlausibilityCriterion {
  protected static final Range<Double> LENGTH_RANGE = Range.closed(1.0, 5000.0);
  protected static final Range<Double> LANE_COUNT_RANGE = Range.closed(1.0, 10.0);
  protected static final Range<Double> LANE_COUNT_RAMP_RANGE = Range.closed(1.0, 10.0);
  protected static final Range<Double> MAX_SPEED_RANGE = Range.closed(30.0, 400.0);
  protected static final Range<Double> MAX_SPEED_RAMP_RANGE = Range.closed(30.0, 200.0);
  protected static final Range<Double> SLOPE_RANGE = Range.closed(-10.0, 10.0);

  private String name;
  private final AttributeType attributeType;
  private final Range<Double> range;
  private final Set<SegmentType> segmentTypes;
  private final HashMap<Element, Violation> elementViolationMap;
  private ViolationManager violationManager;


  /**
   * Constructor.
   *
   * @param violationManager manager to which violations will be added
   */
  public ValueCriterion(ViolationManager violationManager,
                        AttributeType type, Range<Double> range) {
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
          if (!checkValue(accessor)) {
            Violation violation = new Violation(this, List.of((Segment) unit));
            this.violationManager.addViolation(violation);
            this.elementViolationMap.put(unit, violation);
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
    if (accessor.getAttributeType().equals(this.attributeType)) {
      switch (accessor.getAttributeType().getDataType()) {
        case INTEGER:
          return this.range.contains(Double.valueOf((Integer) accessor.getValue()));
        case FRACTIONAL:
          return this.range.contains((Double) accessor.getValue());
        default: return true;
      }
    }
    return true;
  }
}
