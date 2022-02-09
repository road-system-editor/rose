package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.MovableConnector;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Point2D;

/**
 * Provides the functionality for managing roadsystems
 * and the street segments.
 */
public class RoseRoadSystemController extends Controller
    implements RoadSystemController,
    SetObserver<Segment, SelectionBuffer> {

  private static final int SEGMENTS_ROTATION_ALLOWED_AMOUNT = 1;
  private static final double INTERSECT_DISTANCE = 30;

  /**
   * The container for selected segments.
   */
  private final SelectionBuffer selectionBuffer;

  /**
   * The container for commands.
   */
  private final ChangeCommandBuffer changeCommandBuffer;

  /**
   * The model facade for project specific data.
   */
  private final Project project;

  private final RoadSystem roadSystem;

  private Position initialSegmentDragPosition;
  private Connector dragConnector;
  private Position initialConnectorDragPosition;

  private final Set<SetObserver<Segment, RoadSystemController>> observers;

  /**
   * Creates a new {@link RoseRoadSystemController}.
   *
   * @param changeCommandBuffer the buffer for change commands
   * @param storageLock         the coordinator for controller actions
   * @param navigator           the navigator for the controller
   * @param selectionBuffer     the container that stores selected segments
   * @param project             the model facade for project data
   */
  public RoseRoadSystemController(ChangeCommandBuffer changeCommandBuffer, StorageLock storageLock,
                                  Navigator navigator, SelectionBuffer selectionBuffer,
                                  Project project) {
    super(storageLock, navigator);
    this.changeCommandBuffer = changeCommandBuffer;
    this.selectionBuffer = selectionBuffer;
    selectionBuffer.addSubscriber(this);
    this.project = project;
    this.roadSystem = project.getRoadSystem();

    observers = new HashSet<>();
  }

  @Override
  public void setZoomLevel(double zoomLevel) {
    this.project.getZoomSetting().setZoomLevel(zoomLevel);
  }

  @Override
  public void setEditorPosition(Position position) {
    this.project.getZoomSetting().setCenterOfView(position);
  }

  @Override
  public void createStreetSegment(SegmentType segmentType) {
    CreateStreetSegmentCommand createStreetSegmentCommand
        = new CreateStreetSegmentCommand(this.project, segmentType);

    changeCommandBuffer.addAndExecuteCommand(createStreetSegmentCommand);
  }

  @Override
  public void deleteStreetSegment(Segment segment) {
    DeleteStreetSegmentCommand deleteStreetSegmentCommand
        = new DeleteStreetSegmentCommand(this.project, segment);

    selectionBuffer.removeSegmentSelection(segment);
    changeCommandBuffer.addAndExecuteCommand(deleteStreetSegmentCommand);
  }

  @Override
  public void deleteStreetSegment() { //TODO: can only delete on segment at a time
    var selectedSegments = selectionBuffer.getSelectedSegments();
    if (selectedSegments.size() == 1) {
      var segment = selectedSegments.get(0);
      deleteStreetSegment(segment);
    }
  }

  @Override
  public void beginDragStreetSegment(Position segmentPosition) {
    this.initialSegmentDragPosition = segmentPosition;
  }

  @Override
  public void dragStreetSegments(Movement interimMovement) {
    this.selectionBuffer.getSelectedSegments().forEach(segment -> segment.move(interimMovement));
  }


  @Override
  public void endDragStreetSegment(Position segmentPosition) {
    Movement draggingTransition = new Movement(
        segmentPosition.getX() - initialSegmentDragPosition.getX(),
        segmentPosition.getY() - initialSegmentDragPosition.getY());

    DragStreetSegmentsCommand dragStreetSegmentsCommand
        = new DragStreetSegmentsCommand(
        this.project,
        this.selectionBuffer.getSelectedSegments(),
        draggingTransition);

    dragStreetSegmentsCommand.unexecute();
    changeCommandBuffer.addAndExecuteCommand(dragStreetSegmentsCommand);

    initialSegmentDragPosition = null;
  }

  @Override
  public void endDragStreetSegment(Position segmentPosition, Connector draggedConnector) {
    endDragStreetSegment(segmentPosition);
    buildConnection(draggedConnector);
  }

  @Override
  public void toggleSegmentSelection(Segment segment) {
    selectionBuffer.toggleSegmentSelection(segment);
  }

  @Override
  public void addSegmentSelection(Segment segment) {
    this.selectionBuffer.addSegmentSelection(segment);
  }

  @Override
  public void putSegmentSelection(Segment segment) {
    this.selectionBuffer.removeAllSelections();
    this.selectionBuffer.addSegmentSelection(segment);
  }

  @Override
  public void removeSegmentSelection(Segment segment) {
    this.selectionBuffer.removeSegmentSelection(segment);
  }

  @Override
  public void clearSegmentSelection() {
    this.selectionBuffer.removeAllSelections();
  }


  @Override
  public void selectSegmentsInRectangle(Position firstSelectionCorner,
                                        Position secondSelectionCorner) {

    this.project.getRoadSystem().getElements().forEach(element -> {
      if (element.isContainer()) {
        return;
      }
      Segment segment = (Segment) element;
      if (isInRectangle(segment.getCenter(), firstSelectionCorner, secondSelectionCorner)) {
        selectionBuffer.addSegmentSelection(segment);
      } else {
        for (Connector connector : segment.getConnectors()) {
          if (isInRectangle(connector.getPosition(), firstSelectionCorner, secondSelectionCorner)) {
            selectionBuffer.addSegmentSelection(segment);
            break;
          }
        }
      }
    });
  }

  private static boolean isInRectangle(Position target,
                                       Position firstSelectionCorner,
                                       Position secondSelectionCorner) {

    double maximumX = Math.max(firstSelectionCorner.getX(), secondSelectionCorner.getX());
    double minimumX = Math.min(firstSelectionCorner.getX(), secondSelectionCorner.getX());

    double maximumY = Math.max(firstSelectionCorner.getY(), secondSelectionCorner.getY());
    double minimumY = Math.min(firstSelectionCorner.getY(), secondSelectionCorner.getY());

    return isInInterval(target.getX(), minimumX, maximumX)
        && isInInterval(target.getY(), minimumY, maximumY);
  }

  private static boolean isInInterval(double value, double lowerIntervalBorder,
                                      double upperIntervalBorder) {
    return lowerIntervalBorder <= value && value <= upperIntervalBorder;
  }

  @Override
  public void beginDragSegmentEnd(Connector connector, Position connectorStartPosition) {
    this.dragConnector = connector;
    this.initialConnectorDragPosition = connectorStartPosition;
  }

  @Override
  public void endDragSegmentEnd(Position connectorEndPosition) {
    if (this.dragConnector == null) {
      return;
    }

    Movement draggingTransition = new Movement(
        connectorEndPosition.getX() - initialSegmentDragPosition.getX(),
        connectorEndPosition.getY() - initialSegmentDragPosition.getY());

    DragSegmentEndCommand dragSegmentEndCommand
        = new DragSegmentEndCommand((MovableConnector) dragConnector, draggingTransition);

    changeCommandBuffer.addAndExecuteCommand(dragSegmentEndCommand);

    dragConnector = null;
    initialConnectorDragPosition = null;

    buildConnection(dragConnector);
  }

  @Override
  public void rotateSegment() {
    if (selectionBuffer.getSelectedSegments().size() == SEGMENTS_ROTATION_ALLOWED_AMOUNT) {
      this.project.getRoadSystem().rotateSegment(selectionBuffer.getSelectedSegments().get(0), 15);
    }
  }

  @Override
  public double getIntersectionDistance() {
    return INTERSECT_DISTANCE;
  }


  @Override
  public void addSubscriber(SetObserver<Segment, RoadSystemController> observer) {
    observers.add(observer);
  }

  @Override
  public void removeSubscriber(SetObserver<Segment, RoadSystemController> observer) {
    observers.remove(observer);
  }

  @Override
  public void notifySubscribers() {
    observers.forEach(observer -> observer.notifyChange(this));
  }

  @Override
  public RoadSystemController getThis() {
    return this;
  }

  @Override
  public void notifyAddition(Segment unit) {
    observers.forEach(observer -> observer.notifyAddition(unit));
  }

  @Override
  public void notifyRemoval(Segment unit) {
    observers.forEach(observer -> observer.notifyRemoval(unit));
  }

  @Override
  public void notifyChange(SelectionBuffer unit) {
    notifySubscribers();
  }

  private void buildConnection(Connector draggedConnector) {
    var connectorSegmentMap = getConnectorSegmentMap();
    var draggedConnectorPosPoint =
        getConnectorPosPoint(draggedConnector, connectorSegmentMap.get(draggedConnector));
    var intersectingConnectors =
        getIntersectingConnectors(draggedConnectorPosPoint, connectorSegmentMap);
    intersectingConnectors.remove(draggedConnector);
    if (!intersectingConnectors.isEmpty()) {
      var closestConnector =
          getClosestConnectorToPoint(intersectingConnectors, draggedConnectorPosPoint,
              connectorSegmentMap);
      this.roadSystem.connectConnectors(draggedConnector, closestConnector);
    }
  }

  private Connector getClosestConnectorToPoint(List<Connector> connectors, Point2D point,
                                     Map<Connector, Segment> connectorSegmentMap) {
    var connectorList = new LinkedList<>(connectors);
    connectorList.sort((connector1, connector2) -> {
      Double distance1 = getDistanceFromConnectorToPoint(connector1, point, connectorSegmentMap);
      Double distance2 = getDistanceFromConnectorToPoint(connector2, point, connectorSegmentMap);
      return distance1.compareTo(distance2);
    });
    return connectorList.get(0);
  }

  private List<Connector> getIntersectingConnectors(Point2D draggedConnectorPosPoint,
                            Map<Connector, Segment> connectorSegmentMap) {
    return connectorSegmentMap.keySet().stream()
        .filter(connector -> {
          var connectorPosPoint =
              getConnectorPosPoint(connector, connectorSegmentMap.get(connector));
          return draggedConnectorPosPoint.distance(connectorPosPoint) <= INTERSECT_DISTANCE;
        }).collect(Collectors.toList());
  }

  private Map<Connector, Segment> getConnectorSegmentMap() {
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

  private Point2D getConnectorPosPoint(Connector connector, Segment segment) {
    var connectorPos = segment.getRotatedConnectorPosition(connector);
    return new Point2D(connectorPos.getX(), connectorPos.getY());
  }

  private double getDistanceFromConnectorToPoint(Connector connector, Point2D point,
                                                 Map<Connector, Segment> connectorSegmentMap) {
    return getConnectorPosPoint(connector, connectorSegmentMap.get(connector))
        .distance(point);
  }
}