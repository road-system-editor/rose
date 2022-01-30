package edu.kit.rose.view.panel.roadsystem;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.SegmentView;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Represents a background surface that shows a grid, on which segment views can be drawn.
 */
public class Grid extends Pane {

  private static final int LOWER_GRID_BORDER = 0;
  private static final int HEIGHT = 3000;
  private static final int WIDTH = 3000;
  private static final int HORIZONTAL_LINE_SPACING = 5;
  private static final int VERTICAL_LINE_SPACING = 5;
  private static final Color BACKGROUND_COLOR = Color.gray(0.95);
  private static final Color LINE_COLOR = Color.gray(0.7);
  private static final float LINE_WIDTH = 0.5f;

  private SelectionBox selectionBox;
  private BiConsumer<Position, Position> onAreaSelectedEventHandler;

  /**
   * creates new Grid.
   */
  public Grid() {
    init();
    setEventListeners();
  }

  private void init() {
    setWidth(WIDTH);
    setHeight(HEIGHT);
    setBackground(new Background(new BackgroundFill(
        BACKGROUND_COLOR,
        CornerRadii.EMPTY,
        Insets.EMPTY)));
    getChildren().addAll(getLines());
  }

  private void setEventListeners() {
    this.setOnMouseDragged(this::onMouseDragged);
    this.setOnMouseReleased(this::onMouseDragReleased);
  }

  private void onMouseDragged(MouseEvent mouseDragEvent) {
    if (mouseDragEvent.isControlDown() && mouseDragEvent.isPrimaryButtonDown()) {
      if (this.selectionBox == null) {
        selectionBox = new SelectionBox(
            new Point2D(mouseDragEvent.getX(), mouseDragEvent.getY()));
        this.getChildren().add(selectionBox);
      } else {
        selectionBox.update(new Point2D(mouseDragEvent.getX(), mouseDragEvent.getY()));
      }
      mouseDragEvent.consume();
    }
  }

  private void onMouseDragReleased(MouseEvent mouseEvent) {
    if (selectionBox != null) {

      this.getChildren().remove(selectionBox);
      if (onAreaSelectedEventHandler != null) {

        Position validLastMousePosition = new Position(
            (int) Math.round(
                getCoordinateInBorder(selectionBox.getLastMousePosition().getX(), WIDTH)),
            (int) Math.round(
                getCoordinateInBorder(selectionBox.getLastMousePosition().getY(), HEIGHT)));
        Position startingPosition = new Position(
            (int) Math.round(selectionBox.getStartingPoint().getX()),
            (int) Math.round(selectionBox.getStartingPoint().getY()));

        // Set it to before to the call of onAreaSelectedEventHandler, to ensure selectionBox
        // is null if the event handler throws an exception.
        selectionBox = null;

        onAreaSelectedEventHandler.accept(validLastMousePosition, startingPosition);
      } else {
        selectionBox = null;
      }


    }
  }

  private double getCoordinateInBorder(double coordinate, double upperBorder) {
    if (coordinate < LOWER_GRID_BORDER) {
      return LOWER_GRID_BORDER;
    } else {
      return Math.min(coordinate, upperBorder);
    }
  }

  /**
   * Sets the event handler that gets called when an area of the {@link Grid} is selected.
   *
   * @param eventHandler the area selected event handler
   */
  public void setOnAreaSelected(BiConsumer<Position, Position> eventHandler) {
    this.onAreaSelectedEventHandler = eventHandler;
  }


  /**
   * Adds a segment view and displays it on the grid.
   *
   * @param segmentView the segment view to add
   */
  public void addSegmentView(SegmentView<? extends Segment> segmentView) {
    if (!getChildren().contains(segmentView)) {
      getChildren().add(segmentView);
    }
  }

  /**
   * Removes a segment view from the grid.
   *
   * @param segmentView the segment view to remove
   */
  public void removeSegmentView(SegmentView<? extends Segment> segmentView) {
    getChildren().remove(segmentView);
  }

  private Collection<Line> getLines() {
    var lines = new LinkedList<Line>();
    for (int y = 0; y <= HEIGHT; y += HORIZONTAL_LINE_SPACING) {
      lines.add(getLine(y, Orientation.HORIZONTAL));
    }
    for (int x = 0; x <= WIDTH; x += VERTICAL_LINE_SPACING) {
      lines.add(getLine(x, Orientation.VERTICAL));
    }
    return lines;
  }

  private Line getLine(int offset, Orientation orientation) {
    var line = new Line();
    if (orientation == Orientation.HORIZONTAL) {
      line.setStartX(0);
      line.setStartY(offset);
      line.setEndX(WIDTH);
      line.setEndY(offset);
    } else {
      line.setStartX(offset);
      line.setStartY(0);
      line.setEndX(offset);
      line.setEndY(HEIGHT);
    }
    line.setFill(LINE_COLOR);
    line.setStroke(LINE_COLOR);
    line.setStrokeWidth(LINE_WIDTH);
    return line;
  }

  private enum Orientation {
    HORIZONTAL, VERTICAL
  }
}
