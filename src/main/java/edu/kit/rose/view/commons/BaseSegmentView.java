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
import javafx.geometry.Point2D;
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

    getSegment().addSubscriber(this);

    this.roadSystemController = controller;

    this.entryConnectorView =
        new ConnectorView(SEGMENT_WIDTH, getSegment().getEntry().getPosition());
    this.exitConnectorView =
        new ConnectorView(SEGMENT_WIDTH, getSegment().getExit().getPosition());

    entryConnectorObserver = new ConnectorObserver<>(segment, segment.getEntry());
    entryConnectorObserver.setOnConnectorPositionChangedCallback(this::draw);

    exitConnectorObserver = new ConnectorObserver<>(segment, segment.getExit());
    exitConnectorObserver.setOnConnectorPositionChangedCallback(this::draw);

    curve = new QuadCurve();
    curve.setFill(Color.TRANSPARENT);
    curve.setStroke(Color.BLACK);
    curve.setStrokeWidth(SEGMENT_WIDTH);

    this.getChildren().addAll(
        //this.curve,
        this.entryConnectorView,
        this.exitConnectorView);

    this.setStyle("-fx-background-color: black;");

    setupConnectorViewDragging(entryConnectorView, this.getSegment().getEntry());
    setupConnectorViewDragging(exitConnectorView, this.getSegment().getExit());

    this.setOnMousePressed(mouseEvent -> {

      startPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
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
      mouseEvent.consume();
    });

    this.setOnMouseDragReleased(mouseEvent -> {
    });
  }

  private double posOnSourceX;
  private double posOnSourceY;
  private Point2D startPos;
  //private boolean takeCenterSourceFromView;

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

      startPos = currentPosition;

      MovableConnector movableConnector = (MovableConnector) targetConnector;
      movableConnector.move(movement);



      //TODO: Can move?
      mouseEvent.consume();
    });

    targetView.setOnMouseDragReleased(mouseEvent -> {
      mouseEvent.consume();
    });
  }

  @Override
  protected void redraw() {
    setCenterInParent(getSegment().getCenter());
    entryConnectorView.setPosition(getInnerCenter().add(
        new Movement(
            getSegment().getEntry().getPosition().getX(),
            getSegment().getEntry().getPosition().getY())));

    exitConnectorView.setPosition(getInnerCenter().add(
        new Movement(
            getSegment().getExit().getPosition().getX(),
            getSegment().getExit().getPosition().getY())));
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