package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.*;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;

/**
 * (see Pflichtenheft: "Plausibilitätskriterium")
 * A PlausibilityCriterion is used to check if a RoadSystem fulfills Criteria that represent logical
 * requirements to Attributes and Connections between {@link Segment}s. It represents one such rule. Provided with a
 * {@link edu.kit.rose.model.plausibility.violation.ViolationManager} it will create Violations and add them to it.
 */
public interface PlausibilityCriterion extends
        UnitObserver<Segment>, SetObservable<SegmentType, PlausibilityCriterion> {

    /**
     * @return the name of this Criterion
     */
    String getName();

    /**
     * @param name the new name this PlausibilityCriterion is supposed to have.
     */
    void setName(String name);

    /**
     * @return a {@link Box} containing all {@link SegmentType}s this Criterion is applied to.
     */
    Box<SegmentType> getSegmentTypes();

    /**
     * Adds a {@link SegmentType} that this Criterion should be applied to.
     * @param type the {@link SegmentType} that this Criterion should be applied to.
     */
    void addSegmentType(SegmentType type);

    /**
     * Removes a {@link SegmentType} that this Criterion should no longer be applied to.
     * @param type the {@link SegmentType} that this Criterion should no longer be applied to.
     */
    void removeSegmentType(SegmentType type);

    /**
     * @return the type of Criterion this is.
     */
    PlausibilityCriterionType getType();



}