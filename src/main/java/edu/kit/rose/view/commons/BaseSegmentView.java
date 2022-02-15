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
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;

/**
 * A base segment view is the visual representation of a base street segment.
 * It allows dragging its ends independently of each other.
 */
public class BaseSegmentView extends SegmentView<Base> {

  private static final Color SELECTION_EFFECT_COLOR = Color.rgb(0, 150, 130);
  private static final double SELECTION_EFFECT_RADIUS = 5;
  private static final double EFFECT_CURVE_RADIUS = 6;

  private final ConnectorView entryConnectorView;
  private final ConnectorView exitConnectorView;

  private QuadCurve curve;
  private QuadCurve effectCurve;

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

    this.entryConnectorView =
        new ConnectorView(MAIN_STREET_RADIUS, getSegment().getEntry(),
            this::setDraggedConnectorView);
    this.exitConnectorView =
        new ConnectorView(MAIN_STREET_RADIUS, getSegment().getExit(),
            this::setDraggedConnectorView);

    var entryConnectorObserver = new ConnectorObserver<>(segment, segment.getEntry());
    entryConnectorObserver.setOnConnectorPositionChangedCallback(this::draw);

    var exitConnectorObserver = new ConnectorObserver<>(segment, segment.getExit());
    exitConnectorObserver.setOnConnectorPositionChangedCallback(this::draw);

    setupEffectCurve();

    this.getChildren().addAll(this.effectCurve, this.curve,
        this.entryConnectorView, this.exitConnectorView);

    setupConnectorViewDragging(entryConnectorView, this.getSegment().getEntry());
    setupConnectorViewDragging(exitConnectorView, this.getSegment().getExit());
  }

  private void setupCurve() {
    this.curve = new QuadCurve();
    curve.setFill(Color.TRANSPARENT);
    curve.setStroke(Color.BLACK);
    curve.setStrokeWidth(MAIN_STREET_RADIUS * 2);
  }

  private void setupEffectCurve() {
    this.effectCurve = new QuadCurve();
    effectCurve.setFill(Color.TRANSPARENT);
    effectCurve.setStroke(Color.BLACK);
    effectCurve.setStrokeWidth(MAIN_STREET_RADIUS * 2 + EFFECT_CURVE_RADIUS);
    var selectionEffect = new Shadow(BlurType.GAUSSIAN, SELECTION_EFFECT_COLOR,
        SELECTION_EFFECT_RADIUS);
    this.effectCurve.setEffect(selectionEffect);
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
      this.setLayoutX(segmentCenter.getX()
          + leftConnector.getPosition().getX() - MAIN_STREET_RADIUS);
      this.setLayoutY(segmentCenter.getY()
          + leftConnector.getPosition().getY() - MAIN_STREET_RADIUS);
    } else {
      this.setLayoutX(segmentCenter.getX()
          + leftConnector.getPosition().getX() - MAIN_STREET_RADIUS);
      this.setLayoutY(segmentCenter.getY()
          + rightConnector.getPosition().getY() - MAIN_STREET_RADIUS);
    }

    this.setPrefWidth(
        Math.abs(rightConnector.getPosition().getX() - leftConnector.getPosition().getX())
            + 2 * MAIN_STREET_RADIUS);
    this.setPrefHeight(
        Math.abs(rightConnector.getPosition().getY() - leftConnector.getPosition().getY())
            + 2 * MAIN_STREET_RADIUS);
  }

  private void redrawCurve() {
    if (getDrawAsSelected()) {
      effectCurve.setVisible(true);
      redrawCurve(effectCurve);
    } else {
      effectCurve.setVisible(false);
    }
    redrawCurve(curve);
  }

  private void redrawCurve(QuadCurve curve) {
    Position relativeEntryPosition = getSegment().getEntry().getPosition();
    Point2D relativeEntryPointNormal = new Point2D(relativeEntryPosition.getX(),
        relativeEntryPosition.getY()).normalize();
    Position relativeExitPosition = getSegment().getExit().getPosition();
    Point2D relativeExitPointNormal = new Point2D(relativeExitPosition.getX(),
        relativeExitPosition.getY()).normalize();
    Position innerCenter = getInnerCenter();

    curve.setStartX(innerCenter.getX() + relativeEntryPosition.getX()
        - relativeEntryPointNormal.getX() * MAIN_STREET_RADIUS);
    curve.setStartY(innerCenter.getY() + relativeEntryPosition.getY()
        - relativeEntryPointNormal.getY() * MAIN_STREET_RADIUS);
    curve.setControlX(innerCenter.getX());
    curve.setControlY(innerCenter.getY());
    curve.setEndX(innerCenter.getX() + relativeExitPosition.getX()
        - relativeExitPointNormal.getX() * MAIN_STREET_RADIUS);
    curve.setEndY(innerCenter.getY() + relativeExitPosition.getY()
        - relativeExitPointNormal.getY() * MAIN_STREET_RADIUS);
  }

  private Position getInnerCenter() {
    return new Position(
        (this.getWidth() / 2),
        (this.getHeight() / 2));
  }

  @Override
  public void draw() {
    updateBaseSegmentViewBounds();
    updateConnectorViewPositions();
    redrawCurve();
  }

  @Override
  protected void setupDrag() {
    this.curve.setOnDragDetected(this::onDragDetected);
    this.curve.setOnMouseDragged(this::onMouseDragged);
    this.curve.setOnMouseDragReleased(this::onMouseDragReleased);
  }

  @Override
  protected void setupSelection() {
    setupCurve();
    this.curve.setOnMouseClicked(Event::consume);
    this.curve.setOnMousePressed(this::onMousePressed);
  }

  @Override
  public List<ConnectorView> getConnectorViews() {
    return List.of(entryConnectorView, exitConnectorView);
  }

  @Override
  public void notifyChange(Element unit) {
    draw();
  }

  @Override
  public void notifyAddition(Element unit) {
  }

  @Override
  public void notifyRemoval(Element unit) {
  }
}