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
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the functionality for managing roadsystems
 * and the street segments.
 */
public class RoseRoadSystemController extends Controller
    implements RoadSystemController,
    SetObserver<Segment, SelectionBuffer> {

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

  private Position initialSegmentDragPosition;
  private Connector dragConnector;
  private Position initialConnectorDragPosition;

  private final List<SetObserver<Segment, RoadSystemController>> observers;

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
    this.project = project;

    observers = new ArrayList<>();
  }

  @Override
  public void setZoomLevel(int zoomLevel) {
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

    changeCommandBuffer.addCommand(createStreetSegmentCommand);
    createStreetSegmentCommand.execute();
  }

  @Override
  public void deleteStreetSegment(Segment segment) {
    DeleteStreetSegmentCommand deleteStreetSegmentCommand
        = new DeleteStreetSegmentCommand(this.project, segment);

    changeCommandBuffer.addCommand(deleteStreetSegmentCommand);
    deleteStreetSegmentCommand.execute();
  }

  @Override
  public void beginDragStreetSegment(Position segmentPosition) {
    this.initialSegmentDragPosition = segmentPosition;
  }

  @Override
  public void endDragStreetSegment(Position segmentPosition) {
    Movement draggingTransition = new Movement(
        segmentPosition.getX() - initialSegmentDragPosition.getX(),
        segmentPosition.getY() - initialSegmentDragPosition.getY());

    DragStreetSegmentCommand dragStreetSegmentCommand
        = new DragStreetSegmentCommand(
        this.project,
        this.selectionBuffer.getSelectedSegments(),
        initialSegmentDragPosition,
        draggingTransition);

    changeCommandBuffer.addCommand(dragStreetSegmentCommand);
    dragStreetSegmentCommand.execute();

    initialSegmentDragPosition = null;
  }

  @Override
  public void toggleSegmentSelection(Segment segment) {
    selectionBuffer.toggleSegmentSelection(segment);
  }

  @Override
  public void selectSegmentsInRectangle(Position firstSelectionCorner,
                                        Position secondSelectionCorner) {
    this.project.getRoadSystem().getElements();
  }

  @Override
  public void beginDragSegmentEnd(Connector connector, Position connectorStartPosition) {
    this.dragConnector = connector;
    this.initialConnectorDragPosition = connectorStartPosition;
  }

  @Override
  public void endDragSegmentEnd(Position connectorEndPosition) {
    Movement draggingTransition = new Movement(
        connectorEndPosition.getX() - initialSegmentDragPosition.getX(),
        connectorEndPosition.getY() - initialSegmentDragPosition.getY());

    DragSegmentEndCommand dragSegmentEndCommand
        = new DragSegmentEndCommand(
        this.project,
        dragConnector,
        initialConnectorDragPosition,
        draggingTransition);

    changeCommandBuffer.addCommand(dragSegmentEndCommand);
    dragSegmentEndCommand.execute();

    dragConnector = null;
    initialConnectorDragPosition = null;
  }


  @Override
  public void addSubscriber(SetObserver<Segment, RoadSystemController> observer) {
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
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
