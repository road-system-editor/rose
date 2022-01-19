package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.*;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Collection;

/**
 * A Standard implementation of a {@link RoadSystem}
 * using a Graph for holding the connections between the {@link Segment}s.
 */
class GraphRoadSystem extends SimpleDualSetObservable<Element, Connection, RoadSystem>
        implements RoadSystem {

  /**
   * Constructor.
   * The {@link CriteriaManager} is needed for the subscription of
   * {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion} to
   * {@link Segment}s. The {@link TimeSliceSetting} is for the configuration of
   * {@link edu.kit.rose.model.roadsystem.measurements.Measurement}s.
   *
   * @param criteriaManager  the applicationDataSystem to use for this GraphRoadSystem.
   * @param timeSliceSetting the time slice setinng to use for this GraphRoadSystem.
   */
  public GraphRoadSystem(CriteriaManager criteriaManager, TimeSliceSetting timeSliceSetting) {

  }

  @Override
  public Box<Element> getElements() {
    return null;
  }

  @Override
  public Box<Element> getElementsByName(String name) {
    return null;
  }

  @Override
  public void createSegment(SegmentType segmentType) {

  }

  @Override
  public void createGroup(Collection<Element> includedElements) {
  }

  @Override
  public void removeElement(Element element) {

  }

  @Override
  public SortedBox<AttributeAccessor<?>> getSharedAttributeAccessors(Collection<Element> elements) {
    return null;
  }

  @Override
  public void connectConnectors(Connector segment1Connector, Connector segment2Connector) {
  }

  @Override
  public void disconnectConnection(Connection connection) {

  }

  @Override
  public void disconnectFromAll(Segment segment) {

  }

  @Override
  public Box<Segment> getAdjacentSegments(Segment segment) {
    return null;
  }

  @Override
  public Box<Element> getRootElements() {
    return null;
  }

  @Override
  public Box<Connection> getConnections(Segment segment) {
    return null;
  }

  @Override
  public Box<Connection> getConnections(Segment segment1, Segment segment2) {
    return null;
  }

  @Override
  public Connection getConnection(Connector connector) {
    return null;
  }


  @Override
  public void moveSegments(Collection<Segment> segments, Movement movement) {

  }

  /**
   * Rotates the given {@link Segment} on its current {@link edu.kit.rose.infrastructure.Position}
   * by 15 degrees.
   * Uses the center of the segment as a center of rotation.
   *
   * @param segment the {@link Segment} to rotate.
   */
  @Override
  public void rotateSegment(Segment segment) {

  }

  /**
   * Rotates the given {@link Segment}s on its current {@link edu.kit.rose.infrastructure.Position}
   * by 15 degrees.
   * Uses the average center of the segments as a center of rotation.
   *
   * @param segments the {@link Segment}s to rotate.
   */
  @Override
  public void rotateSegments(Collection<Segment> segments) {

  }

  @Override
  public TimeSliceSetting getTimeSliceSetting() {
    return null;
  }

  @Override
  public void addSubscriber(DualSetObserver<Element, Connection, RoadSystem> observer) {

  }

  @Override
  public void removeSubscriber(DualSetObserver<Element, Connection, RoadSystem> observer) {

  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public RoadSystem getThis() {
    return this;
  }

  @Override
  public void notifyChange(Connector unit) {

  }
}
