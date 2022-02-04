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

  private final ConnectorWrapper<Base> entryConnectorWrapper;
  private final ConnectorWrapper<Base> exitConnectorWrapper;

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

    this.entryConnectorView = new ConnectorView(SEGMENT_WIDTH);
    this.exitConnectorView = new ConnectorView(SEGMENT_WIDTH);

    entryConnectorWrapper = new ConnectorWrapper<>(segment, segment.getEntry());
    entryConnectorWrapper.setOnConnectorPositionChangedCallback(this::updateCenterAndRedraw);

    exitConnectorWrapper = new ConnectorWrapper<>(segment, segment.getExit());
    exitConnectorWrapper.setOnConnectorPositionChangedCallback(this::updateCenterAndRedraw);

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
    this.entryConnectorView.setPosition(getCenter().add(entryConnectorWrapper.getMovement()));
    this.exitConnectorView.setPosition(getCenter().add(exitConnectorWrapper.getMovement()));
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
    Position entryPositionRelativeToCenter = getCenter().add(entryConnectorWrapper.getMovement());
    Position exitPositionRelativeToCenter = getCenter().add(exitConnectorWrapper.getMovement());
    curve.setStartX(entryPositionRelativeToCenter.getX());
    curve.setStartY(entryPositionRelativeToCenter.getY());
    curve.setControlX(getCenter().getX());
    curve.setControlY(getCenter().getY());
    curve.setEndX(exitPositionRelativeToCenter.getX());
    curve.setEndY(exitPositionRelativeToCenter.getY());
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
}