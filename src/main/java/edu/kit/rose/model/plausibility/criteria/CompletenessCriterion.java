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
 * Models a Completeness Criteria (see Pflichtenheft: "Vollständigkeitskriterium")
 * This type of Criterion can be used to ensure certain
 * Attributes are set to a value if necessary for export or similar.
 */
class CompletenessCriterion extends SimpleSetObservable<SegmentType, PlausibilityCriterion>
        implements PlausibilityCriterion {

  private String name;
  private final Set<SegmentType> segmentTypes;
  private final ViolationManager violationManager;

  public CompletenessCriterion(ViolationManager violationManager) {
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
    return new SimpleBox<>(this.segmentTypes);
  }


  @Override
  public PlausibilityCriterionType getType() {
    return PlausibilityCriterionType.COMPLETENESS;
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
  public void notifyChange(Segment unit) {

  }

  @Override
  public PlausibilityCriterion getThis() {
    return this;
  }
}
