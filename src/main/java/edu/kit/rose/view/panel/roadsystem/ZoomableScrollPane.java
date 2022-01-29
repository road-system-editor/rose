package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Inject;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Position;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;

/**
 * The zoomable ScrollPane is a ScrollPane that adds pan and zoom gesture support to its content.
 */
public class ZoomableScrollPane extends ScrollPane {

  private static final double ZOOM_SPEED = 1.2;
  private static final double MOVE_SPEED = 0.1;

  private final Grid grid = new Grid();

  private double zoomLevel = 1;

  @Inject
  private RoadSystemController roadSystemController;


  /**
   * Creates a new ZoomableScrollPane.
   */
  public ZoomableScrollPane() {
    setContent(grid);
    setVvalue(0.5);
    setHvalue(0.5);
    setPannable(true);
    setPadding(Insets.EMPTY);
    setHbarPolicy(ScrollBarPolicy.NEVER);
    setVbarPolicy(ScrollBarPolicy.NEVER);
    setup();
  }

  /**
   * Move scrollbar up by set amount.
   */
  public void moveUp() {
    setVvalue(getVvalue() - MOVE_SPEED / zoomLevel);
  }

  /**
   * Move scrollbar down by set amount.
   */
  public void moveDown() {
    setVvalue(getVvalue() + MOVE_SPEED / zoomLevel);
  }

  /**
   * Move scrollbar left by set amount.
   */
  public void moveLeft() {
    setHvalue(getHvalue() - MOVE_SPEED / zoomLevel);
  }

  /**
   * Move scrollbar right by set amount.
   */
  public void moveRight() {
    setHvalue(getHvalue() + MOVE_SPEED / zoomLevel);
  }

  /**
   * Zoom in by set amount.
   */
  public void zoomIn() {
    if (grid.getScaleX() < 3) {
      var zoomLevel = ZOOM_SPEED * grid.getScaleX();
      grid.setScaleX(zoomLevel);
      grid.setScaleY(zoomLevel);
    }
  }

  /**
   * Zoom in by set amount.
   */
  public void zoomOut() {
    if (grid.getScaleX() > 1) {
      var zoomLevel =  grid.getScaleX() / ZOOM_SPEED;
      grid.setScaleX(zoomLevel);
      grid.setScaleY(zoomLevel);
    }
  }

  private void setup() {
    setupZoom();
    setupDrag();
    setupScroll();
    setupButtons();
  }

  private void setupZoom() {
    getContent().setOnScroll(event -> {
      if (event.isControlDown()) {
        event.consume();
        var deltaY = event.getDeltaY();
        if (deltaY == 0
            || (deltaY > 0 && grid.getScaleX() >= 3)
            || (deltaY < 0 && grid.getScaleX() <= 1)) {
          return;
        }
        var zoomFactor = event.getDeltaY() > 0 ? ZOOM_SPEED : 1 / ZOOM_SPEED;
        var zoomLevel = zoomFactor * grid.getScaleX();
        grid.setScaleX(zoomLevel);
        grid.setScaleY(zoomLevel);
        roadSystemController.setZoomLevel(zoomLevel);
        this.zoomLevel = zoomLevel;
      }
    });
  }

  private void setupDrag() {
    setOnDragDetected(event -> startFullDrag());

    setOnMouseDragReleased(event -> updatePosition());
  }

  private void setupScroll() {
    setOnScroll(event -> updatePosition());
  }

  private void setupButtons() {
    setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case UP -> moveUp();
        case DOWN -> moveDown();
        case LEFT -> moveLeft();
        case RIGHT -> moveRight();
        case PLUS -> zoomIn();
        case MINUS -> zoomOut();
        default -> { }
      }
    });
  }

  private void updatePosition() {
    var newPosition = new Position((int) (getHvalue() * grid.getWidth()),
            (int) (getVvalue() * grid.getHeight()));
    roadSystemController.setEditorPosition(newPosition);
  }

}
