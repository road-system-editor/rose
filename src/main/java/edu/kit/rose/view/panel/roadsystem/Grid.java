package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Injector;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.ConnectorView;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.segment.SegmentEditorPanel;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Represents a background surface that shows a grid, on which segment views can be drawn.
 */
public class Grid extends Pane implements SetObserver<Segment, RoadSystemController> {

  private static final int HEIGHT = 3000;
  private static final int WIDTH = 3000;
  private static final int HORIZONTAL_LINE_SPACING = 5;
  private static final int VERTICAL_LINE_SPACING = 5;
  private static final Color BACKGROUND_COLOR = Color.gray(0.95);
  private static final Color LINE_COLOR = Color.gray(0.7);
  private static final float LINE_WIDTH = 0.5f;
  private static final int DOUBLE_CLICK = 2;

  private final Injector injector;
  private final RoadSystemController controller;
  private final List<SegmentView<?>> segmentViews = new LinkedList<>();
  private final Map<Segment, SegmentView<?>> segmentViewMap = new HashMap<>();
  private final List<ConnectorView> connectorViews = new LinkedList<>();
  private final List<SegmentEditorPanel> editors = new LinkedList<>();
  private SelectionBox selectionBox;
  private BiConsumer<Position, Position> onAreaSelectedEventHandler;
  private boolean dragInProgress = false;


  /**
   * creates new Grid.
   */
  public Grid(RoadSystemController controller, Injector injector) {
    init();
    this.injector = injector;
    this.controller = controller;
    setEventListeners();
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
    if (!segmentViews.contains(segmentView)) {
      segmentViews.add(segmentView);
      segmentViewMap.put(segmentView.getSegment(), segmentView);
      getChildren().add(segmentView);
      this.connectorViews.addAll(segmentView.getConnectorViews());
      segmentView.setOnConnectorViewDragged(this::onConnectorViewDragged);
      segmentView.setOnConnectorViewDragEnd(this::onConnectorViewDragEnd);
      segmentView.setOnMouseClicked(event -> {
        event.consume();
        if (event.getClickCount() == DOUBLE_CLICK) {
          buildEditorOnSegment(segmentView.getSegment());
        }
      });
    }
  }

  private void buildEditorOnSegment(Segment segment) {
    getChildren().removeAll(editors);
    editors.clear();
    SegmentEditorPanel editor = new SegmentEditorPanel();
    getChildren().add(editor);
    editor.init(injector);
    editor.setSegment(segment);
    Platform.runLater(() -> {
      var center = segment.getCenter();
      var width = editor.getWidth();
      var height = editor.getHeight();
      editor.relocate(center.getX() - width / 2, center.getY() - height);
    });
    editors.add(editor);
  }

  /**
   * Removes a segment view from the grid.
   *
   * @param segmentView the segment view to remove
   */
  public void removeSegmentView(SegmentView<? extends Segment> segmentView) {
    segmentView.getSegment().removeSubscriber(segmentView);
    segmentViews.remove(segmentView);
    segmentViewMap.remove(segmentView.getSegment(), segmentView);
    getChildren().remove(segmentView);
    this.connectorViews.removeAll(segmentView.getConnectorViews());
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
    this.setOnMouseClicked(mouseEvent -> {
      if (!dragInProgress) {
        controller.clearSegmentSelection();
      }
      dragInProgress = false;
      getChildren().removeAll(editors);
      editors.clear();
    });
  }

  private void onMouseDragged(MouseEvent mouseDragEvent) {
    if (mouseDragEvent.isControlDown() && mouseDragEvent.isPrimaryButtonDown()) {
      this.dragInProgress = true;
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

        var startingPosition = new Position(selectionBox.getStartingPoint().getX(),
            selectionBox.getStartingPoint().getY());
        var lastMousePosition = new Position(selectionBox.getLastMousePosition().getX(),
            selectionBox.getLastMousePosition().getY());

        controller.selectSegmentsInRectangle(startingPosition, lastMousePosition);
      }
      selectionBox = null;
    }
    mouseEvent.consume();
  }

  private void onConnectorViewDragged(ConnectorView connectorView) {
    connectorView.setDragMode(true);
    var intersectingConnectorViews =
        getIntersectingConnectorViews(connectorView);
    connectorViews.stream()
        .filter(c -> c != connectorView)
        .forEach(c -> c.setConnectMode(intersectingConnectorViews.contains(c)));
    connectorView.setConnectMode(!intersectingConnectorViews.isEmpty());
    connectorView.setDragMode(false);
  }

  private void onConnectorViewDragEnd(ConnectorView connectorView) {
    connectorViews.forEach(c -> c.setConnectMode(false));
  }

  private List<ConnectorView> getIntersectingConnectorViews(ConnectorView connectorView) {
    return this.connectorViews.stream()
            .filter(c -> c != connectorView)
            .filter(c -> intersect(connectorView, c))
            .collect(Collectors.toList());
  }

  private boolean intersect(ConnectorView connectorView1, ConnectorView connectorView2) {
    var connectorViewPos1 = getConnectorViewPositionOnGrid(connectorView1);
    var connectorViewPos2 = getConnectorViewPositionOnGrid(connectorView2);
    return connectorViewPos1.distance(connectorViewPos2) <= controller.getIntersectionDistance();
  }

  private Point2D getConnectorViewPositionOnGrid(ConnectorView connectorView) {
    assert (connectorView.getParent().getParent() == this);
    return connectorView.getParent().localToParent(
        connectorView.getCenterX(), connectorView.getCenterY());
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

  @Override
  public void notifyAddition(Segment unit) {
    if (!segmentViewMap.containsKey(unit)) {
      throw new IllegalArgumentException("unknown segment");
    }
    var segmentView = segmentViewMap.get(unit);
    segmentView.setDrawAsSelected(true);
  }

  @Override
  public void notifyRemoval(Segment unit) {
    if (!segmentViewMap.containsKey(unit)) {
      throw new IllegalArgumentException("unknown segment");
    }
    var segmentView = segmentViewMap.get(unit);
    segmentView.setDrawAsSelected(false);
  }

  @Override
  public void notifyChange(RoadSystemController unit) {
  }

  private enum Orientation {
    HORIZONTAL, VERTICAL
  }
}
