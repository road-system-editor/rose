package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;

/**
 * A ValueCriterion represents a Value Criterion (see Pflichtenheft: "Wertebereichkriterium")
 * This checks if the value of an {@link edu.kit.rose.model.roadsystem.attributes.AttributeType}
 * of a {@link Segment} is legal.
 */
class ValueCriterion implements PlausibilityCriterion {

  @Override
  public String getName() {
    return null;
  }

  @Override
  public void setName(String name) {

  }

  @Override
  public Box<SegmentType> getSegmentTypes() {
    return null;
  }


  @Override
  public PlausibilityCriterionType getType() {
    return null;
  }

  @Override
  public void addSegmentType(SegmentType type) {

  }

  @Override
  public void removeSegmentType(SegmentType type) {

  }

  @Override
  public void notifyChange(Element unit) {

  }

  @Override
  public void addSubscriber(SetObserver<SegmentType, PlausibilityCriterion> observer) {

  }

  @Override
  public void removeSubscriber(SetObserver<SegmentType, PlausibilityCriterion> observer) {

  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public PlausibilityCriterion getThis() {
    return this;
  }
}
