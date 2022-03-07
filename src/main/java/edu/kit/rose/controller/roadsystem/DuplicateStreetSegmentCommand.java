package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ConnectionCopier;
import edu.kit.rose.controller.commons.HierarchyCopier;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.ConnectorType;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

/**
 * Encapsulates the functionality of creating a copy of a segment.
 */
public class DuplicateStreetSegmentCommand implements ChangeCommand {
  private static final int DISTANCE_FROM_ORIGIN_SEGMENT = 1;

  private final Project project;
  private final List<Segment> segmentsToDuplicate;
  private final ReplacementLog replacementLog;
  private final HierarchyCopier copier;
  private final List<Segment> segments;

  /**
   * Constructor.
   *
   * @param replacementLog      the log that stores the replacements of elements
   * @param project             the model facade for project data
   * @param segmentsToDuplicate the segment
   */
  public DuplicateStreetSegmentCommand(ReplacementLog replacementLog, Project project,
                                       List<Segment> segmentsToDuplicate) {
    this.replacementLog = replacementLog;
    this.project = project;
    this.segmentsToDuplicate = segmentsToDuplicate;
    this.copier = new HierarchyCopier(null, this.project.getRoadSystem());
    this.segments = new ArrayList<>();
  }

  @Override
  public void execute() {
    HashMap<Segment, Segment> segmentToDuplicateMappings = duplicateSegments();
    connectDuplicatedSegments(segmentToDuplicateMappings);
  }

  private HashMap<Segment, Segment> duplicateSegments() {
    HashMap<Segment, Segment> segmentToDuplicateMappings = new HashMap<>();

    for (int i = 0; i < this.segmentsToDuplicate.size(); i++) {
      var copy = copier
          .copySegment(this.replacementLog.getCurrentVersion(
              this.segmentsToDuplicate.get(i)));
      if (this.segments.size() <= i) {
        this.segments.add(copy);
        segmentToDuplicateMappings.put(this.segmentsToDuplicate.get(i), copy);
      } else {
        var oldSegment = this.replacementLog.getCurrentVersion(this.segments.get(i));
        this.segments.set(i, copy);
        this.replacementLog.replaceElement(oldSegment, this.segments.get(i));
        segmentToDuplicateMappings.put(this.segmentsToDuplicate.get(i), this.segments.get(i));
      }
      this.segments.get(i)
          .move(new Movement(DISTANCE_FROM_ORIGIN_SEGMENT, DISTANCE_FROM_ORIGIN_SEGMENT));
    }

    return segmentToDuplicateMappings;
  }

  private void connectDuplicatedSegments(HashMap<Segment, Segment> segmentToDuplicateMappings) {
    List<Segment> segmentsToDuplicateKeys = new ArrayList<>(segmentToDuplicateMappings.keySet());
    for (int i = 0; i < segmentsToDuplicateKeys.size(); i++) {
      Segment sourceSegment = segmentsToDuplicateKeys.get(i);
      for (int j = 0; j < i; j++) {
        Segment targetSegment = segmentsToDuplicateKeys.get(j);
        if (sourceSegment != targetSegment) {
          connectDuplicatedSegments(sourceSegment, targetSegment, segmentToDuplicateMappings);
        }
      }
    }
  }

  private void connectDuplicatedSegments(
      Segment sourceSegment, Segment targetSegment,
      HashMap<Segment, Segment> segmentToDuplicateMappings) {
    Segment currentSourceSegment = this.replacementLog.getCurrentVersion(sourceSegment);
    Segment currentTargetSegment = this.replacementLog.getCurrentVersion(targetSegment);

    Box<Connection> connections
        = project.getRoadSystem().getConnections(currentSourceSegment, currentTargetSegment);

    if (connections.getSize() > 0) {
      Connection connection = connections.stream().findFirst().orElse(null);
      if (connection == null) {
        return;
      }

      Connector sourceConnector
          = getSegmentConnectorFromConnection(connection, currentSourceSegment);
      Connector targetConnector
          = getSegmentConnectorFromConnection(connection, currentTargetSegment);

      Connector duplicateSourceConnector = getConnectorByType(
          segmentToDuplicateMappings.get(sourceSegment), sourceConnector.getType());

      Connector duplicateTargetConnector = getConnectorByType(
          segmentToDuplicateMappings.get(targetSegment), targetConnector.getType());

      this.project.getRoadSystem().connectConnectors(
          duplicateSourceConnector, duplicateTargetConnector);
    }
  }


  private Connector getConnectorByType(Segment segment, ConnectorType connectorType) {
    return segment.getConnectors()
        .stream().filter(connector -> connector.getType().equals(connectorType))
        .findFirst()
        .orElse(null);
  }

  private Connector getSegmentConnectorFromConnection(Connection connection, Segment segment) {
    return connection.getConnectors()
        .stream()
        .filter(connector -> segment.getConnectors().contains(connector))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void unexecute() {
    for (var segment : this.segments) {
      this.project.getRoadSystem()
          .removeElement(this.replacementLog.getCurrentVersion(segment));
    }
  }
}
