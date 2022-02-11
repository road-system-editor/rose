package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.MovableConnector;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;

/**
 * A base segment view is the visual representation of a base street segment.
 * It allows dragging its ends independently of each other.
 */
public class BaseSegmentView extends SegmentView<Base> {

  private static final double STREET_RADIUS = 15;

  private final ConnectorObserver<Base> entryConnectorObserver;
  private final ConnectorObserver<Base> exitConnectorObserver;

  private final ConnectorView entryConnectorView;
  private final ConnectorView exitConnectorView;

  private double startDragX;
  private double startDragY;

  private final RoadSystemController roadSystemController;

  private QuadCurve curve;

  private Position initialPos;
  private Point2D startPoint;

  /**
   * Creates a new base segment view for a given base {@code segment}.
   *
   * @param segment    the segment to display.
   * @param controller the controller to use for handling position updates.
   * @param translator the translator to use for localizing label.
   */
  public BaseSegmentView(Base segment, RoadSystemController controller,
                         LocalizedTextProvider translator) {
    super(segment, controller, translator);

    getSegment().addSubscriber(this);

    this.roadSystemController = controller;

    this.entryConnectorView =
        new ConnectorView(STREET_RADIUS, getSegment().getEntry(),
            this::setDraggedConnectorView);
    this.exitConnectorView =
        new ConnectorView(STREET_RADIUS, getSegment().getExit(),
            this::setDraggedConnectorView);

    entryConnectorObserver = new ConnectorObserver<>(segment, segment.getEntry());
    entryConnectorObserver.setOnConnectorPositionChangedCallback(this::draw);

    exitConnectorObserver = new ConnectorObserver<>(segment, segment.getExit());
    exitConnectorObserver.setOnConnectorPositionChangedCallback(this::draw);

    this.getChildren().addAll(this.curve, this.entryConnectorView, this.exitConnectorView);

    setupConnectorViewDragging(entryConnectorView, this.getSegment().getEntry());
    setupConnectorViewDragging(exitConnectorView, this.getSegment().getExit());
  }

  private void setupCurve() {
    this.curve = new QuadCurve();
    curve.setFill(Color.TRANSPARENT);
    curve.setStroke(Color.BLACK);
    curve.setStrokeWidth(STREET_RADIUS * 2);
  }

  private void setupConnectorViewDragging(ConnectorView targetView, Connector targetConnector) {
    targetView.setOnMousePressed(mouseEvent -> {
      startPoint = localToParent(mouseEvent.getX(), mouseEvent.getY());
      mouseEvent.consume();
    });

    targetView.setOnDragDetected(mouseEvent -> {
      controller.beginDragSegmentEnd(targetView.getConnector(),
          new Position(startPoint.getX(), startPoint.getY()));
      setDraggedConnectorView(targetView);
      startFullDrag();
      mouseEvent.consume();
    });

    targetView.setOnMouseDragged(mouseEvent -> {
      Point2D currentPosition = localToParent(mouseEvent.getX(), mouseEvent.getY());
      Movement movement = new Movement(currentPosition.getX() - startPoint.getX(),
          currentPosition.getY() - startPoint.getY());

      if (this.canBeMoved(targetView, movement)) {
        startPoint = currentPosition;
        MovableConnector movableConnector = (MovableConnector) targetConnector;
        movableConnector.move(movement);
      }

      if (this.draggedConnectorView != null) {
        if (this.onConnectorViewDragged != null) {
          this.onConnectorViewDragged.accept(draggedConnectorView);
        }
      }

      mouseEvent.consume();
    });

    targetView.setOnMouseDragReleased(mouseEvent -> {
      var releasePoint = localToParent(mouseEvent.getX(), mouseEvent.getY());
      var releasePosition = new Position(releasePoint.getX(), releasePoint.getY());
      if (this.draggedConnectorView != null) {
        controller.endDragSegmentEnd(releasePosition);
        if (this.onConnectorViewDragEnd != null) {
          onConnectorViewDragEnd.accept(draggedConnectorView);
        }
      }
      this.draggedConnectorView = null;
      mouseEvent.consume();
    });
  }


  @Override
  protected void redraw() {
    updateBaseSegmentViewBounds();
    updateConnectorViewPositions();
    redrawCurve();
  }

  @Override
  protected void setupDrag() {
    setupCurve();
    this.curve.setOnDragDetected(this::onDragDetected);
    this.curve.setOnMouseDragged(this::onMouseDragged);
  }

  @Override
  public List<ConnectorView> getConnectorViews() {
    return List.of(entryConnectorView, exitConnectorView);
  }

  private void updateConnectorViewPositions() {
    Position innerCenter = getInnerCenter();
    this.entryConnectorView.setPosition(new Position(
        innerCenter.getX() + getSegment().getEntry().getPosition().getX(),
        innerCenter.getY() + getSegment().getEntry().getPosition().getY()));
    this.exitConnectorView.setPosition(new Position(
        innerCenter.getX() + getSegment().getExit().getPosition().getX(),
        innerCenter.getY() + getSegment().getExit().getPosition().getY()));
  }

  private void updateBaseSegmentViewBounds() {
    Connector leftConnector;
    Connector rightConnector;

    if (getSegment().getEntry().getPosition().getX()
        <= getSegment().getExit().getPosition().getX()) {
      leftConnector = getSegment().getEntry();
      rightConnector = getSegment().getExit();
    } else {
      leftConnector = getSegment().getExit();
      rightConnector = getSegment().getEntry();
    }

    Position segmentCenter = getSegment().getCenter();
    if (leftConnector.getPosition().getY() <= rightConnector.getPosition().getY()) {
      this.setLayoutX(segmentCenter.getX() + leftConnector.getPosition().getX() - STREET_RADIUS);
      this.setLayoutY(segmentCenter.getY() + leftConnector.getPosition().getY() - STREET_RADIUS);
    } else {
      this.setLayoutX(segmentCenter.getX() + leftConnector.getPosition().getX() - STREET_RADIUS);
      this.setLayoutY(segmentCenter.getY() + rightConnector.getPosition().getY() - STREET_RADIUS);
    }

    this.setPrefWidth(
        Math.abs(rightConnector.getPosition().getX() - leftConnector.getPosition().getX())
            + 2 * STREET_RADIUS);
    this.setPrefHeight(
        Math.abs(rightConnector.getPosition().getY() - leftConnector.getPosition().getY())
            + 2 * STREET_RADIUS);
  }

  private void redrawCurve() {
    Position relativeEntryPosition = getSegment().getEntry().getPosition();
    Point2D relativeEntryPointNormal = new Point2D(relativeEntryPosition.getX(),
        relativeEntryPosition.getY()).normalize();
    Position relativeExitPosition = getSegment().getExit().getPosition();
    Point2D relativeExitPointNormal = new Point2D(relativeExitPosition.getX(),
        relativeExitPosition.getY()).normalize();
    Position innerCenter = getInnerCenter();

    curve.setStartX(innerCenter.getX() + relativeEntryPosition.getX()
        - relativeEntryPointNormal.getX() * STREET_RADIUS);
    curve.setStartY(innerCenter.getY() + relativeEntryPosition.getY()
        - relativeEntryPointNormal.getY() * STREET_RADIUS);
    curve.setControlX(innerCenter.getX());
    curve.setControlY(innerCenter.getY());
    curve.setEndX(innerCenter.getX() + relativeExitPosition.getX()
        - relativeExitPointNormal.getX() * STREET_RADIUS);
    curve.setEndY(innerCenter.getY() + relativeExitPosition.getY()
        - relativeExitPointNormal.getY() * STREET_RADIUS);
  }

  @Override
  public void notifyChange(Element unit) {
    draw();
  }

  private Position getInnerCenter() {
    return new Position(
        (this.getWidth() / 2),
        (this.getHeight() / 2));
  }

  @Override
  public void notifyAddition(Element unit) {

  }

  @Override
  public void notifyRemoval(Element unit) {

  }
}