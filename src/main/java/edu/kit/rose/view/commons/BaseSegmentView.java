package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.MovableConnector;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;

/**
 * A base segment view is the visual representation of a base street segment.
 * It allows dragging its ends independently of each other.
 */
public class BaseSegmentView extends SegmentView<Base> {

  private static final double SEGMENT_WIDTH = 20;

  private final ConnectorObserver<Base> entryConnectorObserver;
  private final ConnectorObserver<Base> exitConnectorObserver;

  private final ConnectorView entryConnectorView;
  private final ConnectorView exitConnectorView;

  private double startDragX;
  private double startDragY;

  private final RoadSystemController roadSystemController;

  private final QuadCurve curve;

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

    this.roadSystemController = controller;

    this.entryConnectorView =
        new ConnectorView(SEGMENT_WIDTH, getSegment().getEntry().getPosition());
    this.exitConnectorView =
        new ConnectorView(SEGMENT_WIDTH, getSegment().getExit().getPosition());

    entryConnectorObserver = new ConnectorObserver<>(segment, segment.getEntry());
    entryConnectorObserver.setOnConnectorPositionChangedCallback(this::updateCenterAndRedraw);

    exitConnectorObserver = new ConnectorObserver<>(segment, segment.getExit());
    exitConnectorObserver.setOnConnectorPositionChangedCallback(this::updateCenterAndRedraw);

    curve = new QuadCurve();
    curve.setFill(Color.TRANSPARENT);
    curve.setStroke(Color.BLACK);
    curve.setStrokeWidth(SEGMENT_WIDTH);

    this.getChildren().addAll(
        this.curve,
        this.entryConnectorView,
        this.exitConnectorView);

    setupSegmentDragging();
    setupConnectorViewDragging(entryConnectorView, this.getSegment().getEntry());
    setupConnectorViewDragging(exitConnectorView, this.getSegment().getExit());
  }

  private Position getCenterBetweenPoints(Position position1, Position position2) {
    double diffX = position1.getX() - position2.getX();
    double diffY = position1.getY() - position2.getY();

    return new Position(position2.getX() + diffX / 2.0,
        position2.getY() + diffY / 2.0);
  }

  private void updateCenterAndRedraw() {
    Position newCenterPosition = getCenterBetweenPoints(
        getSegment().getEntry().getPosition(),
        getSegment().getExit().getPosition());

    getSegment().updateCenter(newCenterPosition);
    draw();
  }

  private void setupSegmentDragging() {
    this.setOnMousePressed(mouseEvent -> {
      startDragX = mouseEvent.getSceneX();
      startDragY = mouseEvent.getSceneY();
      /*this.roadSystemController.beginDragStreetSegment(
          new Position(
              (mouseEvent.getSceneX()),
              (mouseEvent.getSceneY())));*/

      mouseEvent.consume();
    });

    this.setOnDragDetected(mouseEvent -> {
      startFullDrag();
      mouseEvent.consume();
    });

    this.setOnMouseDragged(mouseEvent -> {
      getSegment().move(new Movement(
          mouseEvent.getSceneX() - startDragX,
          mouseEvent.getSceneY() - startDragY));
      startDragX = mouseEvent.getSceneX();
      startDragY = mouseEvent.getSceneY();

      mouseEvent.consume();
    });

    this.setOnMouseDragReleased(mouseEvent -> {
      /*Position position = new Position(
          (mouseEvent.getX()),
          (mouseEvent.getY()));*/
      //roadSystemController.endDragStreetSegment(position);
      mouseEvent.consume();
    });
  }

  private void setupConnectorViewDragging(ConnectorView targetView, Connector targetConnector) {
    targetView.setOnMousePressed(mouseEvent -> {
      startDragX = mouseEvent.getSceneX();
      startDragY = mouseEvent.getSceneY();
      /*this.roadSystemController.beginDragSegmentEnd(targetConnector, new Position(
              (int) Math.round(mouseEvent.getSceneX()),
              (int) Math.round(mouseEvent.getSceneY())));*/
      mouseEvent.consume();
    });

    targetView.setOnDragDetected(mouseEvent -> {
      startFullDrag();
      mouseEvent.consume();
    });

    targetView.setOnMouseDragged(mouseEvent -> {
      MovableConnector mv = (MovableConnector) targetConnector;

      mv.move(new Movement(
          mouseEvent.getSceneX() - startDragX,
          mouseEvent.getSceneY() - startDragY));
      startDragX = mouseEvent.getSceneX();
      startDragY = mouseEvent.getSceneY();
      mouseEvent.consume();
    });

    targetView.setOnMouseDragReleased(mouseEvent -> {
      /*Position position = new Position(
          (mouseEvent.getX()),
          (mouseEvent.getY()));*/
      mouseEvent.consume();
    });
  }

  @Override
  protected void redraw() {
    updateConnectorViewPositions();
    updateBaseSegmentViewBounds();
    redrawCurve();


  }

  private void updateConnectorViewPositions() {
    this.entryConnectorView.setPosition(getSegment().getEntry().getPosition());
    this.exitConnectorView.setPosition(getSegment().getExit().getPosition());
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

    if (leftConnector.getPosition().getY() <= rightConnector.getPosition().getY()) {
      this.setLayoutX(leftConnector.getPosition().getX() - SEGMENT_WIDTH);
      this.setLayoutY(leftConnector.getPosition().getY() - SEGMENT_WIDTH);
    } else {
      this.setLayoutX(leftConnector.getPosition().getX() - SEGMENT_WIDTH);
      this.setLayoutY(rightConnector.getPosition().getY() - SEGMENT_WIDTH);
    }

    this.setPrefWidth(
        Math.abs(rightConnector.getPosition().getX() - leftConnector.getPosition().getX())
            + 2 * SEGMENT_WIDTH);
    this.setPrefHeight(
        Math.abs(rightConnector.getPosition().getY() - leftConnector.getPosition().getY())
            + 2 * SEGMENT_WIDTH);
  }

  private void redrawCurve() {
    var entryPos = getSegment().getEntry().getPosition();
    var exitPos = getSegment().getExit().getPosition();
    curve.setStartX(entryPos.getX());
    curve.setStartY(entryPos.getY());
    curve.setControlX(getCenter().getX());
    curve.setControlY(getCenter().getY());
    curve.setEndX(exitPos.getX());
    curve.setEndY(exitPos.getY());
  }


  @Override
  public void notifyChange(Element unit) {
    draw();
  }

  private Position getCenter() {
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