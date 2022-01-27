package edu.kit.rose.model.plausibility.criteria;


import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.SimpleBox;
import edu.kit.rose.infrastructure.SimpleSetObservable;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.HashSet;
import java.util.Set;

/**
 * A ValueCriterion represents a Value Criterion (see Pflichtenheft: "Wertebereichkriterium")
 * This checks if the value of an {@link edu.kit.rose.model.roadsystem.attributes.AttributeType}
 * of a {@link Segment} is legal.
 */
class ValueCriterion extends SimpleSetObservable<SegmentType, PlausibilityCriterion>
        implements PlausibilityCriterion {
  private String name;
  private Set<SegmentType> segmentTypes;
  private final ViolationManager violationManager;

  public ValueCriterion(ViolationManager violationManager) {
    this.name = "";
    this.segmentTypes = new HashSet<>();
    this.violationManager = violationManager;
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
    return new SimpleBox<SegmentType>(this.segmentTypes);
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
}
