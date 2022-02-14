package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.ZoomSetting;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * The zoomable ScrollPane is a ScrollPane that adds pan and zoom gesture support to its content.
 * Inspired by https://stackoverflow.com/a/44314455
 */
public class ZoomableScrollPane extends ScrollPane implements UnitObserver<ZoomSetting> {

  private static final double ZOOM_SPEED = .02;
  private static final double BASE_MOVE_SPEED = 0.1;
  private static final int MAX_ZOOM_IN = 3;
  private static final int MAX_ZOOM_OUT = 1;
  private static final int BUTTON_ZOOM_STRENGTH = 1;
  private static final double POS_APPROXIMATION = 25;

  private Grid grid;
  private Group gridGroup;
  private VBox gridBox;

  private double zoomLevel = 1;

  @Inject
  private RoadSystemController roadSystemController;


  /**
   * Creates a new ZoomableScrollPane.
   */
  public ZoomableScrollPane() {
    super();
  }

  /**
   * Initializes this ZoomableScrollPane. Do not use this pane unless this method was called first.
   *
   * @param injector the injector
   */
  public void init(Injector injector) {
    injector.injectMembers(this);
    initGrid();
    setupGridGroup();
    setupGridBox();
    setContent(gridBox);
    setPannable(true);
    setPadding(Insets.EMPTY);
    setHbarPolicy(ScrollBarPolicy.NEVER);
    setVbarPolicy(ScrollBarPolicy.NEVER);
    setFitToHeight(true);
    setFitToWidth(true);
    setupKeyboardControl();
    hvalueProperty().addListener((observable, oldValue, newValue) ->
        Platform.runLater(() ->
          roadSystemController.setEditorPosition(getCenterOfViewPos())));
    vvalueProperty().addListener((observable, oldValue, newValue) ->
        Platform.runLater(() ->
            roadSystemController.setEditorPosition(getCenterOfViewPos())));
  }

  private void initGrid() {
    this.grid = new Grid(roadSystemController);
  }

  private void setupGridGroup() {
    this.gridGroup = new Group(this.grid);
  }

  private void setupGridBox() {
    this.gridBox = centeredBox(gridGroup);
    this.gridBox.setOnScroll(event -> {
      if (event.isControlDown()) {
        event.consume();
        var deltaY = event.getDeltaY();
        if (canZoomIn(deltaY) || canZoomOut(deltaY)) {
          zoom(Math.signum(deltaY), new Point2D(event.getX(), event.getY()));
        }
      }
    });

  }

  private VBox centeredBox(Group group) {
    var box = new VBox(group);
    box.setAlignment(Pos.CENTER);
    return box;
  }

  private void zoom(double strength, Point2D mousePos) {
    final double zoomFactor = Math.exp(strength * ZOOM_SPEED);

    var innerBounds = gridGroup.getLayoutBounds();
    var viewportBounds = getViewportBounds();

    final double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
    final double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());
    this.zoomLevel *= zoomFactor;
    updateScale();
    this.layout();
    var posInZoomTarget = grid.parentToLocal(gridGroup.parentToLocal(mousePos));
    var adjustment = grid.getLocalToParentTransform().deltaTransform(
        posInZoomTarget.multiply(zoomFactor - 1));
    var updatedInnerBounds = gridGroup.getBoundsInLocal();
    this.setHvalue((valX + adjustment.getX())
        / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
    this.setVvalue((valY + adjustment.getY())
        / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    roadSystemController.setZoomLevel(zoomLevel);
    roadSystemController.setEditorPosition(getCenterOfViewPos());
  }

  private void updateScale() {
    grid.setScaleX(zoomLevel);
    grid.setScaleY(zoomLevel);
  }

  private boolean canZoomOut(double zoomStrength) {
    return zoomStrength < 0 && zoomLevel >= MAX_ZOOM_OUT;
  }

  private boolean canZoomIn(double zoomStrength) {
    return zoomStrength > 0 && zoomLevel <= MAX_ZOOM_IN;
  }

  /**
   * Zoom in by set amount.
   */
  public void zoomIn() {
    if (canZoomIn(BUTTON_ZOOM_STRENGTH)) {
      zoom(BUTTON_ZOOM_STRENGTH, getCenterOfViewPoint());
    }
  }

  /**
   * Zoom in by set amount.
   */
  public void zoomOut() {
    if (canZoomOut(-BUTTON_ZOOM_STRENGTH)) {
      zoom(-BUTTON_ZOOM_STRENGTH, getCenterOfViewPoint());
    }
  }

  /**
   * Returns the content grid of the {@link ZoomableScrollPane}.
   *
   * @return content of the {@link ZoomableScrollPane}
   */
  public Grid getGrid() {
    return grid;
  }

  /**
   * Move scrollbar up by set amount.
   */
  public void moveUp() {
    moveV(-getCurrentMoveSpeed());
  }

  /**
   * Move scrollbar down by set amount.
   */
  public void moveDown() {
    moveV(getCurrentMoveSpeed());
  }

  /**
   * Move scrollbar left by set amount.
   */
  public void moveLeft() {
    moveH(-getCurrentMoveSpeed());
  }

  /**
   * Move scrollbar right by set amount.
   */
  public void moveRight() {
    moveH(getCurrentMoveSpeed());
  }

  /**
   * Centers the currently visible space on the position provided.
   *
   * @param position the position.
   */
  public void setCenterOfViewPos(Position position) {
    if (position.getX() < 0 || position.getY() < 0) {
      throw new IllegalArgumentException("can not jump to negative positions.");
    }
    var h = position.getX() / grid.getWidth();
    var v = position.getY() / grid.getHeight();
    setHvalue(h);
    setVvalue(v);
  }

  private void moveH(double amount) {
    setHvalue(getHvalue() + amount);
    roadSystemController.setEditorPosition(getCenterOfViewPos());
  }

  private void moveV(double amount) {
    setVvalue(getVvalue() + amount);
    roadSystemController.setEditorPosition(getCenterOfViewPos());
  }

  private double getCurrentMoveSpeed() {
    return BASE_MOVE_SPEED / zoomLevel;
  }

  private Point2D getCenterOfViewPoint() {
    return grid.parentToLocal(
        gridGroup.parentToLocal(
            gridBox.getParent().parentToLocal(getWidth() / 2, getHeight() / 2)
        )
    );
  }

  private Position getCenterOfViewPos() {
    var centerOfViewPoint = getCenterOfViewPoint();
    return new Position(centerOfViewPoint.getX(), centerOfViewPoint.getY());
  }

  private void setupKeyboardControl() {
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

  @Override
  public void notifyChange(ZoomSetting unit) {
    var currentCenter = getCenterOfViewPos();
    var newCenter = unit.getCenterOfView();
    var horizontalDistance = Math.abs(currentCenter.getX() - newCenter.getX());
    var verticalDistance = Math.abs(currentCenter.getY() - newCenter.getY());
    if (horizontalDistance > POS_APPROXIMATION || verticalDistance > POS_APPROXIMATION) {
      setCenterOfViewPos(newCenter);
    }
  }
}
