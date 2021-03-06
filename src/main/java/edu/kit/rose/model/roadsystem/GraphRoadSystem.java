package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseDualSetObservable;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentFactory;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.Pseudograph;


/**
 * A Standard implementation of a {@link RoadSystem}
 * using a Graph for holding the connections between the {@link Segment}s.
 */
public class GraphRoadSystem extends RoseDualSetObservable<Element, Connection, RoadSystem>
    implements RoadSystem {

  private final CriteriaManager criteriaManager;
  private final TimeSliceSetting timeSliceSetting;
  private final Graph<Segment, Connection> segmentConnectionGraph;
  private final List<Group> groups; //all groups.
  private final Group rootGroup;

  // stored for easy and performant access.
  private final List<Element> elements; //all elements (including groups).
  private final Map<Connector, Segment> connectorSegmentMap;
  private final Map<Connector, Connection> connectorConnectionMap;

  //weather or not to break connections when connectors are moved.
  private boolean breakOnMove = true;

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
    this.segmentConnectionGraph = new Pseudograph<>(Connection.class);
    this.groups = new LinkedList<>();
    this.elements = new LinkedList<>();
    this.connectorSegmentMap = new HashMap<>();
    this.connectorConnectionMap = new HashMap<>();
    this.rootGroup = new Group();
  }

  @Override
  public Box<Element> getElements() {
    return new RoseBox<>(elements);
  }

  @Override
  public Segment createSegment(SegmentType segmentType) {
    var segment = SegmentFactory.createSegment(segmentType);
    elements.add(segment);
    segment.getConnectors().forEach(c -> connectorSegmentMap.put(c, segment));
    segmentConnectionGraph.addVertex(segment);
    subscribers.forEach(s -> s.notifyAddition(segment));
    criteriaManager.getCriteria().forEach(segment::addSubscriber);
    segment.notifySubscribers(); //get segment checked by criteria
    segment.getConnectors().forEach(c -> {
      connectorConnectionMap.put(c, null);
      c.addSubscriber(this);
    });
    this.rootGroup.addElement(segment);
    return segment;
  }

  @Override
  public Group createGroup(Set<Element> includedElements) {
    if (includedElements.stream().anyMatch(e -> !elements.contains(e))) {
      throw new IllegalArgumentException(
          "can not create group from elements outside this roadSystem");
    }
    var group = new Group(new LinkedList<>(includedElements));
    elements.add(group);
    groups.add(group);
    subscribers.forEach(s -> s.notifyAddition(group));
    this.rootGroup.addElement(group);
    return group;
  }

  @Override
  public void removeElement(Element element) {
    if (!elements.contains(element)) {
      throw new IllegalArgumentException("unknown element");
    }
    if (element.isContainer()) {
      removeGroup((Group) element);
    } else {
      removeSegment((Segment) element);
    }
  }

  private void removeSegment(Segment segment) {
    disconnectFromAll(segment);
    elements.remove(segment);
    segment.getConnectors().forEach(connectorSegmentMap::remove);
    segmentConnectionGraph.removeVertex(segment);
    criteriaManager.setRoadSystem(this);
    criteriaManager.getCriteria().forEach(segment::removeSubscriber);
    subscribers.forEach(s -> s.notifyRemoval(segment));
    segment.getConnectors().forEach(c -> {
      connectorConnectionMap.remove(c);
      c.removeSubscriber(this);
    });
  }

  private void removeGroup(Group group) {
    if (rootGroup.equals(group)) {
      throw new IllegalArgumentException("Can not remove root group from road-system.");
    }
    group.getElements().forEach(this::removeElement);
    elements.remove(group);
    groups.remove(group);
    subscribers.forEach(s -> s.notifyRemoval(group));
  }

  @Override
  public Connection connectConnectors(Connector segment1Connector, Connector segment2Connector) {
    if (!connectorConnectionMap.containsKey(segment1Connector)
        || !connectorConnectionMap.containsKey(segment2Connector)) {
      throw new IllegalArgumentException("can not connect connectors outside this roadSystem");
    }
    disconnectConnection(connectorConnectionMap.get(segment1Connector));
    disconnectConnection(connectorConnectionMap.get(segment2Connector));
    var connection = new Connection(segment1Connector, segment2Connector,
        getConnectionCenter(segment1Connector, segment2Connector));
    var segment1 = connectorSegmentMap.get(segment1Connector);
    var segment2 = connectorSegmentMap.get(segment2Connector);
    segmentConnectionGraph.addEdge(segment1, segment2, connection);
    connectorConnectionMap.put(segment1Connector, connection);
    connectorConnectionMap.put(segment2Connector, connection);
    subscribers.forEach(s -> s.notifyAdditionSecond(connection));
    segment1.notifySubscribers();
    segment2.notifySubscribers();
    return connection;
  }

  private Position getConnectionCenter(Connector connector1, Connector connector2) {
    var connector1Pos = connectorSegmentMap.get(connector1)
        .getAbsoluteConnectorPosition(connector1);
    var connector2Pos = connectorSegmentMap.get(connector2)
        .getAbsoluteConnectorPosition(connector2);
    return new Position(
        (connector1Pos.getX() + connector2Pos.getX()) / 2,
        (connector1Pos.getY() + connector2Pos.getY()) / 2
    );
  }

  @Override
  public void disconnectConnection(Connection connection) {
    if (!connectorConnectionMap.containsValue(connection)) {
      throw new IllegalArgumentException("unknown connection");
    }
    if (connection != null) {
      var segment1 = connectorSegmentMap.get(connection.getConnectors().get(0));
      var segment2 = connectorSegmentMap.get(connection.getConnectors().get(1));
      segmentConnectionGraph.removeEdge(connection);
      segment1.notifySubscribers();
      segment2.notifySubscribers();
      connection.getConnectors().forEach(c -> connectorConnectionMap.put(c, null));
      subscribers.forEach(s -> s.notifyRemovalSecond(connection));
    }
  }

  @Override
  public void disconnectFromAll(Segment segment) {
    if (!elements.contains(segment)) {
      throw new IllegalArgumentException("unknown segment");
    }
    getConnections(segment).forEach(this::disconnectConnection);
  }

  @Override
  public Box<Segment> getAdjacentSegments(Segment segment) {
    if (!elements.contains(segment)) {
      throw new IllegalArgumentException("unknown segment");
    }
    return new RoseBox<>(Graphs.neighborListOf(segmentConnectionGraph, segment));
  }

  @Override
  public Box<Connection> getConnections(Segment segment) {
    if (!elements.contains(segment)) {
      throw new IllegalArgumentException("unknown segment");
    }
    return new RoseBox<>(getConnectionsSet(segment));
  }


  @Override
  public Box<Connection> getConnections(Segment segment1, Segment segment2) {
    if (!elements.contains(segment1) || !elements.contains(segment2)) {
      throw new IllegalArgumentException("unknown segment");
    }
    return new RoseBox<>(segmentConnectionGraph.getAllEdges(segment1, segment2));
  }

  private Set<Connection> getConnectionsSet(Segment segment) {
    if (!elements.contains(segment)) {
      throw new IllegalArgumentException("unknown segment");
    }
    return segmentConnectionGraph.edgesOf(segment);
  }

  @Override
  public Connection getConnection(Connector connector) {
    if (!connectorConnectionMap.containsKey(connector)) {
      throw new IllegalArgumentException("unknown connector");
    }
    return connectorConnectionMap.get(connector);
  }


  @Override
  public void moveSegments(Collection<Segment> segments, Movement movement) {
    if (segments.stream().anyMatch(e -> !elements.contains(e))) {
      throw new IllegalArgumentException("can not move unknown segments");
    }
    var outSideConnections = getOutSideConnections(segments);
    var inSideConnections = getInsideConnections(segments);
    inNoBreakMode(() -> segments.forEach(s -> s.move(movement)));
    inSideConnections.forEach(connection -> connection.move(movement));
    outSideConnections.forEach(this::disconnectConnection);
  }


  /**
   * Rotates the given {@link Segment} on its current {@link edu.kit.rose.infrastructure.Position}
   * by a given amount of degrees.
   * Disconnects the segment.
   *
   * @param segment the {@link Segment} to rotate.
   * @param degrees the degrees the segment is to be rotated by.
   */
  @Override
  public void rotateSegment(Segment segment, int degrees) {
    if (!elements.contains(segment)) {
      throw new IllegalArgumentException("unknown segment");
    }
    segment.rotate(degrees);
    disconnectFromAll(segment);
  }

  //runs a piece of code while not breaking connections due to movements
  private void inNoBreakMode(Runnable runThis) {
    breakOnMove = false;
    runThis.run();
    breakOnMove = true;
  }

  //returns all connections that connect the given segments to segments outside the collection
  private List<Connection> getOutSideConnections(Collection<Segment> segments) {
    var connectors = getConnectors(segments);

    return segments.stream()
        .flatMap(s -> Graphs.neighborListOf(segmentConnectionGraph, s).stream())
        .distinct()
        .filter(s -> !segments.contains(s))
        .flatMap(s -> getConnectionsSet(s).stream()
            .filter(c -> c.getConnectors().stream().anyMatch(connectors::contains)))
        .toList();
  }

  //returns all connections that connect the given segments
  private List<Connection> getInsideConnections(Collection<Segment> segments) {
    var connectors = getConnectors(segments);

    return segmentConnectionGraph.edgeSet().stream()
        .filter(connection -> connectors.containsAll(connection.getConnectors().stream().toList()))
        .toList();
  }

  private List<Connector> getConnectors(Collection<Segment> segments) {
    return segments.stream()
        .flatMap(segment -> segment.getConnectors().stream()).toList();
  }

  @Override
  public TimeSliceSetting getTimeSliceSetting() {
    return this.timeSliceSetting;
  }

  @Override
  public void clear() {
    var roots = new LinkedList<Element>();
    this.rootGroup.getElements().forEach(roots::add);
    roots.forEach(this.rootGroup::removeElement);
    roots.forEach(this::removeElement);
    this.timeSliceSetting.reset();
  }

  @Override
  public Group getRootGroup() {
    return rootGroup;
  }

  @Override
  public RoadSystem getThis() {
    return this;
  }

  @Override
  public void notifyChange(Connector unit) {
    if (!connectorConnectionMap.containsKey(unit)) {
      throw new IllegalArgumentException("unknown connector");
    }
    var connection = connectorConnectionMap.get(unit);
    if (breakOnMove && connection != null) {
      disconnectConnection(connection);
    }
  }
}
