package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class encapsulates the building process of {@link Connection}s between
 * {@link edu.kit.rose.model.roadsystem.elements.Segment}s when segment are dragged.
 */
class ConnectionBuilder {

  static final double INTERSECTION_DISTANCE = 30;

  static Connection buildConnection(RoadSystem roadSystem, Connector draggedConnector) {
    var connectorSegmentMap = getConnectorSegmentMap(roadSystem);
    var draggedConnectorPos = connectorSegmentMap.get(draggedConnector)
        .getAbsoluteConnectorPosition(draggedConnector);
    var intersectingConnectors =
        getIntersectingConnectors(draggedConnectorPos, connectorSegmentMap);
    intersectingConnectors.remove(draggedConnector);
    if (!intersectingConnectors.isEmpty()) {
      var closestConnector =
          getClosestConnectorToPoint(intersectingConnectors, draggedConnectorPos,
              connectorSegmentMap);
      return roadSystem.connectConnectors(draggedConnector, closestConnector);
    } else {
      return null;
    }
  }

  private static Connector getClosestConnectorToPoint(List<Connector> connectors, Position position,
                                               Map<Connector, Segment> connectorSegmentMap) {
    var connectorList = new LinkedList<>(connectors);
    connectorList.sort((connector1, connector2) -> {
      Double distance1 = getDistanceFromConnectorToPosition(connector1,
          position, connectorSegmentMap);
      Double distance2 = getDistanceFromConnectorToPosition(connector2,
          position, connectorSegmentMap);
      return distance1.compareTo(distance2);
    });
    return connectorList.get(0);
  }

  private static List<Connector> getIntersectingConnectors(Position draggedConnectorPos,
                                                    Map<Connector, Segment> connectorSegmentMap) {
    return connectorSegmentMap.keySet().stream()
        .filter(connector -> {
          var connectorPos = connectorSegmentMap.get(connector)
              .getAbsoluteConnectorPosition(connector);
          return draggedConnectorPos.distanceTo(connectorPos) <= INTERSECTION_DISTANCE;
        }).collect(Collectors.toList());
  }

  private static Map<Connector, Segment> getConnectorSegmentMap(RoadSystem roadSystem) {
    var connectorSegmentMap = new HashMap<Connector, Segment>();
    roadSystem.getElements().stream()
        .filter(element -> !element.isContainer())
        .map(element -> (Segment) element)
        .forEach(segment -> {
          var segmentConnectors = segment.getConnectors();
          segmentConnectors.forEach(c -> connectorSegmentMap.put(c, segment));
        });
    return connectorSegmentMap;
  }

  private static double getDistanceFromConnectorToPosition(Connector connector, Position position,
                                                    Map<Connector, Segment> connectorSegmentMap) {
    return connectorSegmentMap.get(connector).getAbsoluteConnectorPosition(connector)
        .distanceTo(position);
  }
}
