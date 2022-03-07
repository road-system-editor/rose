package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.ConnectorType;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  A ConnectorCriterion checks if the Connector Types are compatible with each other.
 */
public class ConnectorCriterion extends AbstractCompatibilityCriterion {

  private static final Map<ConnectorType, Set<ConnectorType>> COMPATIBLE_CONNECTOR_TYPE_MAP =
      Map.of(ConnectorType.ENTRY, Set.of(ConnectorType.EXIT, ConnectorType.RAMP_EXIT),
          ConnectorType.EXIT, Set.of(ConnectorType.ENTRY, ConnectorType.RAMP_ENTRY),
          ConnectorType.RAMP_ENTRY, Set.of(ConnectorType.EXIT, ConnectorType.RAMP_EXIT),
          ConnectorType.RAMP_EXIT, Set.of(ConnectorType.ENTRY, ConnectorType.RAMP_ENTRY));

  /**
   * Creates a new connector criterion with default settings.
   *
   * @param roadSystem the road system this criterion applies to. This may be {@code null} but it
   *     must be set before this criterion is able to receive notifications.
   * @param violationManager manager to which violations will be added. This may be {@code null} but
   *     it must be set before this criterion is able to receive notifications.
   */
  public ConnectorCriterion(RoadSystem roadSystem, ViolationManager violationManager) {
    super(roadSystem, violationManager);
    this.segmentTypes.addAll(List.of(SegmentType.values()));
  }

  @Override
  public PlausibilityCriterion getThis() {
    return this;
  }



  @Override
  protected void checkCriterion(Segment segment) {

    Box<Connection> connections = roadSystem.getConnections(segment);
    for (Connection connection : connections) {
      if (!checkConnection(connection)) {
        List<Segment> invalidSegments = getSegmentsOfConnection(connection);
        invalidSegments.remove(segment);
        updateViolations(invalidSegments, segment);
      }
    }
  }

  private boolean checkConnection(Connection connection) {
    Box<Connector> connectorBox = connection.getConnectors();
    boolean isCompatible = true;
    for (Connector connector : connectorBox) {
      if (!COMPATIBLE_CONNECTOR_TYPE_MAP.get(connector.getType())
          .contains(connection.getOther(connector).getType())) {
        isCompatible = false;
      }
    }
    return isCompatible;
  }

  private List<Segment> getSegmentsOfConnection(Connection connection) {
    ArrayList<Segment> segments = new ArrayList<>();
    for (Element element : roadSystem.getElements()) {
      if (!element.isContainer()) {
        for (Connection currentConnection : roadSystem.getConnections((Segment) element)) {
          if (currentConnection == connection) {
            segments.add((Segment) element);
          }
        }
      }
    }
    return segments;
  }

  protected void checkAll() {
    if (this.roadSystem != null) {
      roadSystem.getElements().forEach(this::notifyChange);
    }
  }

  @Override
  public PlausibilityCriterionType getType() {
    return PlausibilityCriterionType.CONNECTOR;
  }
}
