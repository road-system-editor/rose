package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

/**
 *  An Abstract CompatibilityCriterion holds the Methods that are needed to create a Criterion
 *  that checks the Connection of multiple Segments.
 */
public abstract class AbstractCompatibilityCriterion extends RoseSetObservable<SegmentType,
    PlausibilityCriterion> implements PlausibilityCriterion {

  private final MultiValuedMap<Element, Violation> elementViolationMap;
  private final Set<SegmentType> segmentTypes;
  private String name;
  private RoadSystem roadSystem;
  private ViolationManager violationManager;

  /**
   * Constructor.
   *
   * @param roadSystem the road system this criterion applies to. This may be {@code null} but it
   *     must be set before this criterion is able to receive notifications.
   * @param violationManager manager to which violations will be added. This may be {@code null} but
   *     it must be set before this criterion is able to receive notifications.
   */
  protected AbstractCompatibilityCriterion(RoadSystem roadSystem,
                                           ViolationManager violationManager) {
    this.name = "";
    this.roadSystem = roadSystem;
    this.violationManager = violationManager;
    this.elementViolationMap = new HashSetValuedHashMap<>();
    this.segmentTypes = new HashSet<>();
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
  public void setViolationManager(ViolationManager violationManager) {
    if (this.violationManager != violationManager) {
      if (this.violationManager != null) {
        SortedBox<Violation> violations = this.violationManager.getViolations();
        for (var violation : violations) {
          if (violation.violatedCriterion() == this) {
            this.violationManager.removeViolation(violation);
          }
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
  public void notifyAddition(Element unit) {
    notifyChange(unit);
  }

  @Override
  public void notifyRemoval(Element unit) {
    if (!unit.isContainer()) {
      removeViolationsOfSegment((Segment) unit);
    }
  }

  protected void removeViolationsOfSegment(Segment segment) {
    HashSetValuedHashMap<Element, Violation> elementViolationMapCopy =
        new HashSetValuedHashMap<>(elementViolationMap);
    if (elementViolationMap.containsKey(segment)) {
      for (Violation vio : elementViolationMapCopy.get(segment)) {
        this.violationManager.removeViolation(vio);
        elementViolationMap.removeMapping(segment, vio);
      }
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
      return;
    }
    checkCriterion(segment);

  }

  protected abstract void checkCriterion(Segment segment);

  protected void updateViolations(List<Segment> invalidSegments, Segment segment) {
    if (this.violationManager == null) {
      return;
    }
    HashSetValuedHashMap<Element, Violation> elementViolationMapCopy =
        new HashSetValuedHashMap<>(elementViolationMap);
    if (!invalidSegments.isEmpty()
        && this.segmentTypes.contains((segment).getSegmentType())) {
      if (elementViolationMapCopy.containsKey(segment)) {
        for (Violation vio : elementViolationMapCopy.get(segment)) {
          if (vio.offendingSegments().size() != invalidSegments.size()
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

  protected abstract void checkAll();

  /**
   * Returns the road system to use for connection lookups.
   *
   * @return the road system to use for connection lookups.
   */
  protected RoadSystem getRoadSystem() {
    return this.roadSystem;
  }

  @Override
  public PlausibilityCriterion getThis() {
    return this;
  }
}
