package edu.kit.rose.model.plausibility.criteria;


import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A ValueCriterion represents a Value Criterion (see Pflichtenheft: "Wertebereichkriterium")
 * This checks if the value of an {@link edu.kit.rose.model.roadsystem.attributes.AttributeType}
 * of a {@link Segment} is legal.
 */
class ValueCriterion extends RoseSetObservable<SegmentType, PlausibilityCriterion>
        implements PlausibilityCriterion {
  private static final int UPPER_LENGTH = 5000;
  private static final int LOWER_LENGTH = 1;
  private static final int UPPER_LANE_COUNT = 10;
  private static final int LOWER_LANE_COUNT = 1;
  private static final int UPPER_LANE_COUNT_RAMP = 10;
  private static final int LOWER_LANE_COUNT_RAMP = 1;
  private static final int UPPER_MAX_SPEED = 400;
  private static final int LOWER_MAX_SPEED = 30;
  private static final int UPPER_MAX_SPEED_RAMP = 200;
  private static final int LOWER_MAX_SPEED_RAMP = 30;
  private static final double UPPER_SLOPE = 10;
  private static final double LOWER_SLOPE = -10;
  private String name;
  private final Set<AttributeType> affectedAttributeTypes;
  private final Set<SegmentType> segmentTypes;
  private final ViolationManager violationManager;

  /**
   * Constructor.
   *
   * @param violationManager manager to which violations will be added
   */
  public ValueCriterion(ViolationManager violationManager) {
    this.name = "";
    this.segmentTypes = new HashSet<>();
    this.affectedAttributeTypes = new HashSet<>();
    this.violationManager = violationManager;
  }

  /**
   * Gets the types of attributes that do not
   * fill the requirements for value criteria.
   *
   * @return a box of affected types.
   */
  public Box<AttributeType> getAffectedAttributeType() {
    return new RoseBox<>(this.affectedAttributeTypes);
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
    return new RoseBox<>(this.segmentTypes);
  }


  @Override
  public PlausibilityCriterionType getType() {
    return PlausibilityCriterionType.VALUE;
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

  }

  @Override
  public ValueCriterion getThis() {
    return this;
  }

  @Override
  public void notifyAddition(Element unit) {
    boolean valid = true;
    Segment segment = (Segment) unit;
    if (this.segmentTypes.contains(segment.getSegmentType())) {
      SortedBox<AttributeAccessor<?>> accessors = unit.getAttributeAccessors();
      for (AttributeAccessor<?> accessor : accessors) {
        if (!checkValue(accessor)) {
          valid = false;
          affectedAttributeTypes.add(accessor.getAttributeType());
        } else {
          this.affectedAttributeTypes.remove(accessor.getAttributeType());
        }
      }
    }
    if (!valid) {
      this.violationManager.addViolation(new Violation(this, List.of(segment)));
    }
  }

  @Override
  public void notifyRemoval(Element unit) {

  }

  private boolean checkValue(AttributeAccessor accessor) {
    switch (accessor.getAttributeType()) {
      case LENGTH:
        return ((Integer) accessor.getValue() >= LOWER_LENGTH
                && (Integer) accessor.getValue() <= UPPER_LENGTH);
      case SLOPE:
        return ((Double) accessor.getValue() >= LOWER_SLOPE
                && (Double) accessor.getValue() <= UPPER_SLOPE);
      case MAX_SPEED:
        return ((Integer) accessor.getValue() >= LOWER_MAX_SPEED
                && (Integer) accessor.getValue() <= UPPER_MAX_SPEED);
      case LANE_COUNT:
        return ((Integer) accessor.getValue() >= LOWER_LANE_COUNT
                && (Integer) accessor.getValue() <= UPPER_LANE_COUNT);
      case LANE_COUNT_RAMP:
        return ((Integer) accessor.getValue()
                >= LOWER_LANE_COUNT_RAMP
                && (Integer) accessor.getValue() <= UPPER_LANE_COUNT_RAMP);
      case MAX_SPEED_RAMP:
        return ((Integer) accessor.getValue()
                >= LOWER_MAX_SPEED_RAMP
                && (Integer) accessor.getValue() <= UPPER_MAX_SPEED_RAMP);
      default:
        throw new IllegalArgumentException("no such accessor type found");
    }
  }
}
