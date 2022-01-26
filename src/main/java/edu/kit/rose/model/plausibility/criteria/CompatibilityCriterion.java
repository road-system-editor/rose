package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.validation.OperatorType;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.SegmentType;

/**
 * Modells a Compatiblity Criterion. See Pflichtenheft: "Kompatibilitätskriterium"
 */
public class CompatibilityCriterion implements PlausibilityCriterion {

  /**
   * Constructor.
   *
   * @param roadSystem The Roadsystem this Criterion applied to.
   */
  CompatibilityCriterion(RoadSystem roadSystem) {

  }

  /**
   * Provides the AttributeType that this Criterion is checking.
   *
   * @return the AttributeType that this Criterion is checking.
   */
  public AttributeType getAttributeType() {
    return null;
  }

  /**
   * Sets the AttributeType that this Criterion is supposed to check.
   *
   * @param attributeType the AttributeType that this Criterion is supposed to check.
   */
  public void setAttributeType(AttributeType attributeType) {

  }

  /**
   * Provides the type of Operator this Criterion is using.
   *
   * @return the type of Operator this Criterion is using.
   */
  public OperatorType getOperatorType() {
    return null;
  }

  /**
   * Sets the Type of Operator this Criterion is supposed to use.
   *
   * @param operatorType the Type of Operator this Criterion is supposed to use.
   */
  public void setOperatorType(OperatorType operatorType) {

  }

  /**
   * Gives a {@link SortedBox} of {@link OperatorType}s that contains all Types that are
   * compatible with this criterion.
   * (This is depending on the AttributeType this criterion has)
   *
   * @return containing all {@link OperatorType}s that are compatible with this criterion.
   */
  public SortedBox<OperatorType> getCompatibleOperatorTypes() {
    return null;
  }

  /**
   * Provides the legal discrepancy numeric values can have to be accepted by this criterion.
   *
   * @return the legal discrepancy numeric values can have to be accepted by this criterion.
   */
  public double getLegalDiscrepancy() {
    return 0;
  }

  /**
   * Sets the legal discrepancy numeric values are supposed to have to be accepted by this
   * criterion.
   *
   * @param discrepancy the legal discrepancy numeric values are supposed to have to be accepted
   *                    by this criterion.
   */
  public void setLegalDiscrepancy(double discrepancy) {

  }

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

  @Override
  public void notifyAddition(Element unit) {

  }

  @Override
  public void notifyRemoval(Element unit) {

  }
}
