package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.event.Event;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

/**
 * The {@link SegmentView} is the base class for the visual representation of a street segment.
 * It is responsible for drawing itself and receiving drag and drop operations.
 *
 * @param <T> the segment type the segment view represents.
 * @implNote A {@link SegmentView} is supposed to be placed on a managed pane.
 *     It then draws itself on that pane.
 */
public abstract class SegmentView<T extends Segment> extends Pane
    implements SetObserver<Element, Element> {

  /**
   * The segment which this segment view represents.
   */
  private final T segment;

  /**
   * Determines if segment view is drawn with a selection indicator.
   */
  private boolean isSelected;

  /**
   * Determines if the segment can drag itself.
   */
  private boolean isDraggable;

  /**
   * The roadSystemController to notify about drag movements.
   */
  private final RoadSystemController controller;

  /**
   * The text provider for translated string.
   */
  private final LocalizedTextProvider translator;

  private ConnectorView draggedConnectorView;

  private Consumer<ConnectorView> onConnectorViewDragged;
  private Consumer<ConnectorView> onConnectorViewDragEnd;

  /**
   * Creates a new segment view that acts as visual representation of a given segment.
   *
   * @param segment the segment to create a visual representation for
   */
  protected SegmentView(T segment, RoadSystemController controller,
                        LocalizedTextProvider translator) {
    this.segment = segment;
    this.isDraggable = true;
    this.controller = controller;
    this.translator = translator;

    this.widthProperty().addListener(e -> draw());
    this.heightProperty().addListener(e -> draw());

    //TODO: fix for base segments
    this.setOnMousePressed(mouseEvent -> {
      startPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
      controller.putSegmentSelection(this.getSegment());
    });

    this.setOnDragDetected(mouseEvent -> startFullDrag());

    this.setOnMouseDragged(mouseEvent -> {
      var currentPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
      var movement = new Movement(currentPos.getX() - startPos.getX(),
          currentPos.getY() - startPos.getY());
      if (canBeMoved(movement)) {
        segment.move(movement);
        startPos = currentPos;
      }
      if (this.draggedConnectorView != null) {
        if (this.onConnectorViewDragged != null) {
          this.onConnectorViewDragged.accept(draggedConnectorView);
        }
      }
      mouseEvent.consume();
    });

    this.setOnMouseDragReleased(mouseEvent -> {
      if (this.draggedConnectorView != null) {
        if (this.onConnectorViewDragEnd != null) {
          onConnectorViewDragEnd.accept(draggedConnectorView);
        }
      }
      this.draggedConnectorView = null;
    });

    this.setOnMouseClicked(Event::consume);
  }

  private boolean canBeMoved(Movement movement) {
    final var bounds = getLayoutBounds();
    final var topLeft = localToParent(bounds.getMinX(), bounds.getMinY());
    final var topRight = localToParent(bounds.getMaxX(), bounds.getMinY());
    final var bottomLeft = localToParent(bounds.getMinX(), bounds.getMaxY());
    final var bottomRight = localToParent(bounds.getMinX(), bounds.getMinY());
    final var movementVector = new Point2D(movement.getX(), movement.getY());
    final var gridBounds = getParent().getLayoutBounds();
    List<Supplier<Boolean>> checks = List.of(
        () -> gridBounds.contains(topLeft.add(movementVector)),
        () -> gridBounds.contains(topRight.add(movementVector)),
        () -> gridBounds.contains(bottomLeft.add(movementVector)),
        () -> gridBounds.contains(bottomRight.add(movementVector))
    );
    return checks.stream().allMatch(Supplier::get);
  }

  private double posOnSourceX;
  private double posOnSourceY;
  private Point2D startPos;

  /**
   * Returns the segment that is represented by the segment view.
   *
   * @return the segment that is represented by the segment view.
   */
  public T getSegment() {
    return segment;
  }

  /**
   * Draws the segment.
   */
  public void draw() {
    redraw();
  }

  /**
   * Returns whether the segment view is drawn with a selection indicator.
   *
   * @return whether the segment view is drawn with a selection indicator.
   */
  public boolean getDrawAsSelected() {
    return isSelected;
  }

  /**
   * Sets if the segment view is drawn with a selection indicator.
   *
   * @param drawAsSelected determines if selection indicator is visible
   */
  public void setDrawAsSelected(boolean drawAsSelected) {
    this.isSelected = drawAsSelected;
  }

  /**
   * Returns whether the segment view can drag itself.
   *
   * @return whether the segment view can drag itself.
   */
  public boolean getIsDraggable() {
    return isDraggable;
  }

  /**
   * Sets whether the segment view can drag itself.
   *
   * @param isDraggable whether the segment view can drag itself.
   */
  public void setIsDraggable(boolean isDraggable) {
    this.isDraggable = isDraggable;
  }

  public void setOnConnectorViewDragged(Consumer<ConnectorView> onConnectorViewDragged) {
    this.onConnectorViewDragged = onConnectorViewDragged;
  }

  public void setOnConnectorViewDragEnd(Consumer<ConnectorView> onConnectorViewDragEnd) {
    this.onConnectorViewDragEnd = onConnectorViewDragEnd;
  }

  /**
   * Returns the localized text provider instance of the segment view.
   *
   * @return the localized text provider instance of the segment view.
   */
  protected LocalizedTextProvider getTranslator() {
    return translator;
  }

  protected void setDraggedConnectorView(ConnectorView connectorView) {
    this.draggedConnectorView = connectorView;
  }

  /**
   * Draws the segment on a given graphical context.
   *
   */
  protected abstract void redraw();

  /**
   * Provides the {@link ConnectorView}s of this segment view.
   *
   * @return the connector views
   */
  public abstract List<ConnectorView> getConnectorViews();
}
