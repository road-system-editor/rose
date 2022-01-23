package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseDualSetObservable;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;

/**
 * A Standard implementation of a {@link RoadSystem}
 * using a Graph for holding the connections between the {@link Segment}s.
 */
class GraphRoadSystem extends RoseDualSetObservable<Element, Connection, RoadSystem>
        implements RoadSystem {

  private final CriteriaManager criteriaManager;
  private final TimeSliceSetting timeSliceSetting;
  private final Graph<Segment, Connection> segmentConnectionGraph;
  private final List<Group> groups;
  private final List<Element> elements;

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
    this.criteriaManager = criteriaManager;
    this.timeSliceSetting = timeSliceSetting;
    this.segmentConnectionGraph = new DefaultUndirectedGraph<>(Connection.class);
    this.groups = new LinkedList<>();
    this.elements = new LinkedList<>();
  }

  @Override
  public Box<Element> getElements() {
    return new RoseBox<>(elements);
  }

  @Override
  public Box<Element> getElementsByName(String name) {
    return new RoseBox<>(
        elements.stream().filter(e -> e.getName().startsWith(name)).collect(Collectors.toList()));
  }

  @Override
  public Segment createSegment(SegmentType segmentType) {
    var segment = SegmentFactory.createSegment(segmentType);
    elements.add(segment);
    segmentConnectionGraph.addVertex(segment);
    subscribers.forEach(s -> s.notifyAddition(segment));
    criteriaManager.getCriteria().forEach(segment::addSubscriber);
    segment.notifySubscribers(); //get segment checked by criteria
    return segment;
  }

  @Override
  public Group createGroup(Set<Element> includedElements) {
    var group = new Group(includedElements);
    elements.add(group);
    groups.add(group);
    subscribers.forEach(s -> s.notifyAddition(group));
    return group;
  }

  @Override
  public void removeElement(Element element) {
    elements.remove(element);
    if (element.isContainer()) {
      removeGroup((Group) element);
    } else {
      removeSegment((Segment) element);
    }
    subscribers.forEach(s -> s.notifyRemoval(element));
  }

  private void removeSegment(Segment segment) {
    var connectionsToSegment = segmentConnectionGraph.edgesOf(segment);
    segmentConnectionGraph.removeVertex(segment);
    subscribers.forEach(s -> connectionsToSegment.forEach(s::notifyRemovalSecond));
    criteriaManager.getCriteria().forEach(segment::removeSubscriber);
  }

  private void removeGroup(Group group) {
    groups.remove(group);
    group.getElements().forEach(this::removeElement);
  }

  @Override
  public SortedBox<AttributeAccessor<?>> getSharedAttributeAccessors(Collection<Element> elements) {
    if (elements.isEmpty()) {
      return new RoseSortedBox<>(List.of());
    } else {
      // O(#types)
      List<AttributeType> types = new ArrayList<>();
      for (var accessor : elements.stream().findAny().get().getAttributeAccessors()) {
        types.add(accessor.getAttributeType());
      }

      // O(#elements * #types^2)
      types.removeIf(type -> !type.isBulkable()
          || !elements.stream().allMatch(element -> getAccessorForType(element, type) != null));

      List<AttributeAccessor<?>> bulkAccessors = types.stream()
          .map(type -> switch (type) {
            case NAME, COMMENT -> bulkAccessor(elements, type, String.class);
            case LENGTH, LANE_COUNT, LANE_COUNT_RAMP, MAX_SPEED, MAX_SPEED_RAMP -> bulkAccessor(
                elements,
                type,
                Integer.class);
            case SLOPE -> bulkAccessor(elements, type, Double.class);
            case CONURBATION -> bulkAccessor(elements, type, Boolean.class);
          }).collect(Collectors.toList());

      return new RoseSortedBox<>(bulkAccessors);
    }
  }

  private static <T> AttributeAccessor<T> bulkAccessor(Collection<Element> elements,
                                                       AttributeType type, Class<T> clazz) {
    @SuppressWarnings("unchecked")
    List<AttributeAccessor<T>> accessors =
        elements.stream()
            .map(element -> (AttributeAccessor<T>) getAccessorForType(element, type))
            .toList();

    return new AttributeAccessor<>(type, () -> bulkGet(accessors), value -> bulkSet(accessors,
        value));
  }

  private static <T> T bulkGet(List<AttributeAccessor<T>> containedAccessors) {
    T value = containedAccessors.stream().findAny().get().getValue();

    for (var accessor : containedAccessors) {
      if (!Objects.equals(accessor.getValue(), value)) {
        return null;
      }
    }
    return value;
  }

  private static <T> void bulkSet(List<AttributeAccessor<T>> containedAccessors, T value) {
    for (var accessor : containedAccessors) {
      accessor.setValue(value);
    }
  }

  private static AttributeAccessor<?> getAccessorForType(Element element, AttributeType type) {
    for (var accessor : element.getAttributeAccessors()) {
      if (accessor.getAttributeType() == type) {
        return accessor;
      }
    }
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
