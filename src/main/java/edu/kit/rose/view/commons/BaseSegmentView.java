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
import java.util.function.Supplier;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
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

  private Point2D startPos;

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
        new ConnectorView(SEGMENT_WIDTH, getSegment().getEntry(),
            this::setDraggedConnectorView);
    this.exitConnectorView =
        new ConnectorView(SEGMENT_WIDTH, getSegment().getExit(),
            this::setDraggedConnectorView);

    entryConnectorObserver = new ConnectorObserver<>(segment, segment.getEntry());
    entryConnectorObserver.setOnConnectorPositionChangedCallback(this::draw);

    exitConnectorObserver = new ConnectorObserver<>(segment, segment.getExit());
    exitConnectorObserver.setOnConnectorPositionChangedCallback(this::draw);

    curve = new QuadCurve();
    curve.setFill(Color.TRANSPARENT);
    curve.setStroke(Color.BLACK);
    curve.setStrokeWidth(SEGMENT_WIDTH);

    this.getChildren().addAll(
        this.curve,
        this.entryConnectorView,
        this.exitConnectorView);

    setupSegmentViewDragging();
    setupConnectorViewDragging(entryConnectorView, this.getSegment().getEntry());
    setupConnectorViewDragging(exitConnectorView, this.getSegment().getExit());
  }

  private boolean canBeMoved(Node target, Movement movement) {
    final Bounds bounds = target.getLayoutBounds();
    final Point2D topLeft = localToParent(bounds.getMinX(), bounds.getMinY());
    final Point2D topRight = localToParent(bounds.getMaxX(), bounds.getMinY());
    final Point2D bottomLeft = localToParent(bounds.getMinX(), bounds.getMaxY());
    final Point2D bottomRight = localToParent(bounds.getMinX(), bounds.getMinY());
    final Point2D movementVector = new Point2D(movement.getX(), movement.getY());
    final Bounds gridBounds = getParent().getLayoutBounds();
    List<Supplier<Boolean>> checks = List.of(
        () -> gridBounds.contains(topLeft.add(movementVector)),
        () -> gridBounds.contains(topRight.add(movementVector)),
        () -> gridBounds.contains(bottomLeft.add(movementVector)),
        () -> gridBounds.contains(bottomRight.add(movementVector))
    );
    return checks.stream().allMatch(Supplier::get);
  }

  private void setupSegmentViewDragging() {
    this.curve.setOnMousePressed(mouseEvent -> {
      startPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
    });

    this.curve.setOnDragDetected(mouseEvent -> startFullDrag());

    this.curve.setOnMouseDragged(mouseEvent -> {
      Point2D currentPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
      Movement movement = new Movement(currentPos.getX() - startPos.getX(),
          currentPos.getY() - startPos.getY());
      if (canBeMoved(this, movement)) {
        getSegment().move(movement);
        startPos = currentPos;
      }
      mouseEvent.consume();
    });

    this.curve.setOnMouseDragReleased(mouseEvent -> {
      Point2D absolutPoint = localToParent(mouseEvent.getX(), mouseEvent.getY());
    });
  }

  private void setupConnectorViewDragging(ConnectorView targetView, Connector targetConnector) {
    targetView.setOnMousePressed(mouseEvent -> {
      startPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
      mouseEvent.consume();
    });

    targetView.setOnDragDetected(mouseEvent -> {
      startFullDrag();
      mouseEvent.consume();
    });

    targetView.setOnMouseDragged(mouseEvent -> {
      Point2D currentPosition = localToParent(mouseEvent.getX(), mouseEvent.getY());
      Movement movement = new Movement(currentPosition.getX() - startPos.getX(),
          currentPosition.getY() - startPos.getY());

      if (canBeMoved(targetView, movement)) {
        startPos = currentPosition;
        MovableConnector movableConnector = (MovableConnector) targetConnector;
        movableConnector.move(movement);
      }

      mouseEvent.consume();
    });

    targetView.setOnMouseDragReleased(mouseEvent -> mouseEvent.consume());
  }


  @Override
  protected void redraw() {
    updateBaseSegmentViewBounds();
    updateConnectorViewPositions();
    redrawCurve();
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
      this.setLayoutX(segmentCenter.getX() + leftConnector.getPosition().getX() - SEGMENT_WIDTH);
      this.setLayoutY(segmentCenter.getY() + leftConnector.getPosition().getY() - SEGMENT_WIDTH);
    } else {
      this.setLayoutX(segmentCenter.getX() + leftConnector.getPosition().getX() - SEGMENT_WIDTH);
      this.setLayoutY(segmentCenter.getY() + rightConnector.getPosition().getY() - SEGMENT_WIDTH);
    }

    this.setPrefWidth(
        Math.abs(rightConnector.getPosition().getX() - leftConnector.getPosition().getX())
            + 2 * SEGMENT_WIDTH);
    this.setPrefHeight(
        Math.abs(rightConnector.getPosition().getY() - leftConnector.getPosition().getY())
            + 2 * SEGMENT_WIDTH);
  }

  private void redrawCurve() {
    Position relativeEntryPosition = getSegment().getEntry().getPosition();
    Position relativeExitPosition = getSegment().getExit().getPosition();
    Position innerCenter = getInnerCenter();

    curve.setStartX(innerCenter.getX() + relativeEntryPosition.getX());
    curve.setStartY(innerCenter.getY() + relativeEntryPosition.getY());
    curve.setControlX(innerCenter.getX());
    curve.setControlY(innerCenter.getY());
    curve.setEndX(innerCenter.getX() + relativeExitPosition.getX());
    curve.setEndY(innerCenter.getY() + relativeExitPosition.getY());
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

  private void setCenterInParent(Position position) {
    this.setLayoutX(position.getX() - this.getWidth() / 2);
    this.setLayoutY(position.getY() - this.getHeight() / 2);
  }

  @Override
  public void notifyAddition(Element unit) {

  }

  @Override
  public void notifyRemoval(Element unit) {

  }
}