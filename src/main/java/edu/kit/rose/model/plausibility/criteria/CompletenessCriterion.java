package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.SetObserver;
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
 * Models a Completeness Criteria (see Pflichtenheft: "Vollständigkeitskriterium")
 * This type of Criterion can be used to ensure certain
 * Attributes are set to a value if necessary for export or similar.
 */
class CompletenessCriterion extends RoseSetObservable<SegmentType, PlausibilityCriterion>
        implements PlausibilityCriterion {

  private String name;
  private final Set<SegmentType> segmentTypes;
  private final ViolationManager violationManager;
  private final Set<AttributeType> necessaryAttributeTypes;
  private final HashMap<Element, Violation> elementViolationMap;
  private ViolationManager violationManager;

  public CompletenessCriterion(ViolationManager violationManager) {
    this.name = "";
    this.segmentTypes = new HashSet<>();
    this.violationManager = violationManager;
    this.necessaryAttributeTypes = new HashSet<>();
    this.necessaryAttributeTypes.add(AttributeType.NAME);
    this.necessaryAttributeTypes.add(AttributeType.LENGTH);
    this.elementViolationMap = new HashMap<>();
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


  @Override
  public PlausibilityCriterionType getType() {
    return PlausibilityCriterionType.COMPLETENESS;
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
    boolean violated = false;
    Segment segment = (Segment) unit;
    if (!unit.isContainer()) {
      Box<AttributeAccessor<?>> accessors = unit.getAttributeAccessors();
      for (AttributeAccessor<?> accessor : accessors) {
        if (this.necessaryAttributeTypes.contains(accessor.getAttributeType())) {
          if (accessor.getValue() == null) {
            violated = true;
            if (segmentTypes.contains(segment.getSegmentType())) {
              Violation violation = new Violation(this, List.of((Segment) unit));
              this.violationManager.addViolation(violation);
              this.elementViolationMap.put(unit, violation);
            }
          }
        }
      }
      if (!violated) {
        if (elementViolationMap.containsKey(unit)) {
          violationManager.removeViolation(elementViolationMap.get(unit));
          elementViolationMap.remove(unit);
        }
      }
    }
  }

  @Override
  public PlausibilityCriterion getThis() {
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
}
