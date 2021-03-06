package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.MovableConnector;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides the functionality for managing roadsystems
 * and the street segments.
 */
public class RoseRoadSystemController extends Controller
    implements RoadSystemController,
    SetObserver<Segment, SelectionBuffer> {

  private static final int SEGMENTS_ROTATION_ALLOWED_AMOUNT = 1;

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
  private final ReplacementLog replacementLog;

  private final RoadSystem roadSystem;

  private Position initialSegmentDragPosition;
  private Connector dragConnector;
  private Position initialConnectorDragPosition;
  private Set<Connection> draggedSegmentConnections;


  private final Set<SetObserver<Segment, RoadSystemController>> observers;

  /**
   * Creates a new {@link RoseRoadSystemController}.
   *
   * @param changeCommandBuffer the buffer for change commands
   * @param storageLock         the coordinator for controller actions
   * @param navigator           the navigator for the controller
   * @param selectionBuffer     the container that stores selected segments
   * @param project             the model facade for project data
   * @param replacementLog      the log that stores all the replacements of elements
   */
  public RoseRoadSystemController(ChangeCommandBuffer changeCommandBuffer, StorageLock storageLock,
                                  Navigator navigator, SelectionBuffer selectionBuffer,
                                  Project project,
                                  ReplacementLog replacementLog) {
    super(storageLock, navigator);
    this.changeCommandBuffer = changeCommandBuffer;
    this.selectionBuffer = selectionBuffer;
    selectionBuffer.addSubscriber(this);
    this.project = project;
    this.roadSystem = project.getRoadSystem();
    this.replacementLog = replacementLog;
    this.draggedSegmentConnections = new HashSet<>();

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
        = new CreateStreetSegmentCommand(this.replacementLog, this.project, segmentType);

    changeCommandBuffer.addAndExecuteCommand(createStreetSegmentCommand);
  }

  @Override
  public void createStreetSegment(SegmentType segmentType, Position position) {
    CreateStreetSegmentCommand createStreetSegmentCommand
        = new CreateStreetSegmentCommand(this.replacementLog, this.project, segmentType, position);

    changeCommandBuffer.addAndExecuteCommand(createStreetSegmentCommand);
  }

  @Override
  public void duplicateStreetSegment() {
    ArrayList<Segment> toDuplicateSegments
            = new ArrayList<>(this.selectionBuffer.getSelectedSegments());
    DuplicateStreetSegmentCommand duplicateStreetSegmentCommand = new DuplicateStreetSegmentCommand(
            this.replacementLog, this.project, toDuplicateSegments);
    changeCommandBuffer.addAndExecuteCommand(duplicateStreetSegmentCommand);
  }

  @Override
  public void deleteStreetSegment(Segment segment) {
    List<Segment> segments = List.of(segment);
    DeleteStreetSegmentCommand deleteStreetSegmentCommand
        = new DeleteStreetSegmentCommand(this.replacementLog, this.project, segments);

    selectionBuffer.removeSegmentSelection(segment);
    changeCommandBuffer.addAndExecuteCommand(deleteStreetSegmentCommand);
  }

  @Override
  public void deleteStreetSegments() {
    DeleteStreetSegmentCommand deleteStreetSegmentCommand = new DeleteStreetSegmentCommand(
        this.replacementLog, this.project, selectionBuffer.getSelectedSegments());

    selectionBuffer.removeAllSelections();
    changeCommandBuffer.addAndExecuteCommand(deleteStreetSegmentCommand);
  }

  @Override
  public void beginDragStreetSegment(Position segmentPosition) {
    this.initialSegmentDragPosition = segmentPosition;

    saveSelectedSegmentsConnections();
  }

  private void saveSelectedSegmentsConnections() {
    for (Segment segment : this.selectionBuffer.getSelectedSegments()) {
      for (Segment adjacentSegment : this.project.getRoadSystem().getAdjacentSegments(segment)) {
        Box<Connection> connections
            = this.project.getRoadSystem().getConnections(segment, adjacentSegment);
        connections.forEach(connection -> this.draggedSegmentConnections.add(connection));
      }
    }
  }

  @Override
  public void dragStreetSegments(Movement interimMovement) {
    this.roadSystem.moveSegments(selectionBuffer.getSelectedSegments(), interimMovement);
  }

  @Override
  public void endDragStreetSegment(Position segmentPosition) {
    endDragStreetSegment(segmentPosition, null);
  }

  @Override
  public void endDragStreetSegment(Position segmentPosition, Connector draggedConnector) {
    if (initialSegmentDragPosition == null) {
      //TODO: remove after implementing hit box event translucency
      return;
    }

    Movement draggingTransition = new Movement(
        segmentPosition.getX() - initialSegmentDragPosition.getX(),
        segmentPosition.getY() - initialSegmentDragPosition.getY());

    DragStreetSegmentsCommand dragStreetSegmentsCommand = new DragStreetSegmentsCommand(
        this.replacementLog,
        this.project,
        this.selectionBuffer.getSelectedSegments(),
        draggingTransition,
        this.draggedSegmentConnections,
        draggedConnector);

    dragStreetSegmentsCommand.unexecute();
    changeCommandBuffer.addAndExecuteCommand(dragStreetSegmentsCommand);

    this.initialSegmentDragPosition = null;
    this.draggedSegmentConnections.clear();
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
        connectorEndPosition.getX() - initialConnectorDragPosition.getX(),
        connectorEndPosition.getY() - initialConnectorDragPosition.getY());

    DragSegmentEndCommand dragSegmentEndCommand = new DragSegmentEndCommand(
        this.roadSystem, this.replacementLog, (MovableConnector) dragConnector, draggingTransition);

    // makes sure that the position is first reset to the state before the drag
    dragSegmentEndCommand.unexecute();
    changeCommandBuffer.addAndExecuteCommand(dragSegmentEndCommand);

    dragConnector = null;
    initialConnectorDragPosition = null;
  }

  @Override
  public void rotateSegment() {
    if (selectionBuffer.getSelectedSegments().size() == SEGMENTS_ROTATION_ALLOWED_AMOUNT) {
      this.project.getRoadSystem().rotateSegment(selectionBuffer.getSelectedSegments().get(0), 15);
    }
  }

  @Override
  public double getIntersectionDistance() {
    return ConnectionBuilder.INTERSECTION_DISTANCE;
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


}