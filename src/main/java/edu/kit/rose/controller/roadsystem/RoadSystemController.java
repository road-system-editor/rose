package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SetObservable;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;



/**
 * Provides the functionality for managing roadsystems
 * and the street segments.
 *
 */
public interface RoadSystemController extends SetObservable<Segment, RoadSystemController> {

  /**
   * Sets the zoomLevel of the road system editor.
   *
   * @param zoomLevel zoomlevel of the road system editor
   */
  void setZoomLevel(double zoomLevel);

  /**
   * Sets the position of the roadsystem editor on the
   * background surface.
   *
   * @param position the position of the top left corner of the editor
   */
  void setEditorPosition(Position position);

  /**
   * Creates a new Segment of a given {@link SegmentType}.
   *
   * @param segmentType the type of the street segment to create
   */
  void createStreetSegment(SegmentType segmentType);

  /**
   * Creates a new Segment of a given {@link SegmentType}.
   *
   * @param segmentType the type of the street segment to create
   * @param position the position this segment is to be placed at
   */
  void createStreetSegment(SegmentType segmentType, Position position);

  /**
   * Creates a copies of selected segments.
   */
  void duplicateStreetSegment();

  /**
   * Deletes an existing street {@link Segment}.
   *
   * @param segment the street segment to delete
   */
  void deleteStreetSegment(Segment segment); //TODO: remove?

  /**
   * Deletes the currently selected {@link Segment}s.
   */
  void deleteStreetSegments();

  /**
   * Stores the starting {@link Position} of the {@link Segment} which will be dragged.
   *
   * @param segmentPosition the current position of the segment
   */
  void beginDragStreetSegment(Position segmentPosition);

  /**
   * Updates the position of a street segment.
   * This method allows multiple segments to be dragged
   * fluently.
   *
   * @param interimMovement the interim movement
   */
  void dragStreetSegments(Movement interimMovement);

  /**
   * Stores the end {@link Position} of the {@link Segment} which was dragged.
   *
   * @param segmentPosition the final position of the segment
   */
  void endDragStreetSegment(Position segmentPosition);

  /**
   * Stores the end {@link Position} of the {@link Segment} which was dragged.
   * The required connector might connect to other connectors in reach.
   *
   * @param segmentPosition the final position of the segment
   * @param draggedConnector the connector
   */
  void endDragStreetSegment(Position segmentPosition, Connector draggedConnector);

  /**
   * Toggles the selection status of a {@link Segment}.
   *
   * @param segment the segment whose selection status gets toggle
   */
  void toggleSegmentSelection(Segment segment);

  /**
   * selects a segment as selected.
   *
   * @param segment the segment to select
   */
  void addSegmentSelection(Segment segment);

  /**
   * selects a segment as selected. Unselects all currently selected segments.
   *
   * @param segment the segment that is to be selected
   */
  void putSegmentSelection(Segment segment);

  /**
   * selects a segment as unselected.
   *
   * @param segment the segment to unselect
   */
  void removeSegmentSelection(Segment segment);

  /**
   * Unselects all currently selected segments.
   */
  void clearSegmentSelection();

  /**
   * Selects all segments that have a connector inside the rectangle
   * which the given positions determine.
   * The positions have to be located diagonally to each other on the rectangle.
   *
   * @param firstSelectionCorner  a corner position of the drag selection box
   * @param secondSelectionCorner a corner position of the drag selection box
   */
  void selectSegmentsInRectangle(Position firstSelectionCorner, Position secondSelectionCorner);

  /**
   * Begins a drag action for an end of a street segment.
   * The controller remembers the connector of the
   * last method call, until endDragStreetSegment is called.
   *
   * @param connector              connector which represents the end of a street segment
   * @param connectorStartPosition position from where the dragging begins
   */
  void beginDragSegmentEnd(Connector connector, Position connectorStartPosition);

  /**
   * Ends a drag action for an end of a street segment. Instructs the model to execute the dragging.
   * If beginDragSegmentEnd has not been called, this method does nothing.
   *
   * @param connectorEndPosition position where the dragging ends
   */
  void endDragSegmentEnd(Position connectorEndPosition);

  /**
   * Rotates a single selected segment, if only that segment is selected.
   */
  void rotateSegment();

  /**
   * Provides the intersection distance. This distance describes how far two connectors
   * can be apart before they intersect each other.
   * Distance between connectors <= getIntersectionDistance() --> intersection
   *
   * @return the intersection distance
   */
  double getIntersectionDistance();

}