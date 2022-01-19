package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SetObservable;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;

/**
 * Provides the functionality for managing roadsystems
 * and the street segments.
 *
 * @author ROSE Team
 */
public class RoseRoadSystemController extends Controller
    implements RoadSystemController,
    SetObserver<Segment, SelectionBuffer> {

  /**
   * The container for selected segments.
   */
  private SelectionBuffer selectionBuffer;

  /**
   * Creates a new {@link RoseRoadSystemController}.
   *

   * @param storageLock         the coordinator for controller actions
   * @param selectionBuffer     the container that stores selected segments
   * @param project             the model facade for project data
   */
  public RoseRoadSystemController(StorageLock storageLock, SelectionBuffer selectionBuffer,
                                  Project project) {
    super(storageLock);
    this.selectionBuffer = selectionBuffer;
  }

  @Override
  public void setZoomLevel(int zoomLevel) {

  }

  @Override
  public void setEditorPosition(Position position) {

  }

  @Override
  public void createStreetSegment(SegmentType segmentType) {

  }

  @Override
  public void deleteStreetSegment(Segment segment) {

  }

  @Override
  public void beginDragStreetSegment(Position segmentPosition) {

  }

  @Override
  public void endDragStreetSegment(Position segmentPosition) {

  }

  @Override
  public void toggleSegmentSelection(Segment segment) {

  }

  @Override
  public void selectSegmentsInRectangle(Position firstSelectionCorner,
                                        Position secondSelectionCorner) {

  }

  @Override
  public void beginDragSegmentEnd(Connector connector, Position connectorStartPosition) {

  }

  @Override
  public void endDragSegmentEnd(Position connectorEndPosition) {

  }


  @Override
  public void addSubscriber(SetObserver<Segment, RoadSystemController> observer) {

  }

  @Override
  public void removeSubscriber(SetObserver<Segment, RoadSystemController> observer) {

  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public void notifyAddition(Segment unit) {

  }

  @Override
  public void notifyRemoval(Segment unit) {

  }

  @Override
  public void notifyChange(SelectionBuffer unit) {

  }
}
