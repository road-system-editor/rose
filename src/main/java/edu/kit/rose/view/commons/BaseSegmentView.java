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
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.transform.Rotate;

/**
 * A base segment view is the visual representation of a base street segment.
 * It allows dragging its ends independently of each other.
 */
public class BaseSegmentView extends SegmentView<Base> {

  private static final String ARROW_IMAGE_RESOURCE = "arrow.png";
  private static final double ARROW_WIDTH = 13;
  private static final double ARROW_HEIGHT = 22;
  private static final Color SELECTION_EFFECT_COLOR = Color.rgb(0, 150, 130);
  private static final double SELECTION_EFFECT_RADIUS = 5;
  private static final double EFFECT_CURVE_RADIUS = 6;

  private final ConnectorView entryConnectorView;
  private final ConnectorView exitConnectorView;

  private QuadCurve curve;
  private QuadCurve effectCurve;
  private ImageView arrow;
  private Rotate arrowRotation;


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
    setupArrowImage();

    this.getChildren().addAll(this.effectCurve, this.curve, this.arrow,
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

  private void setupArrowImage() {
    var imageUrl = getClass().getResource(ARROW_IMAGE_RESOURCE);
    Image arrowImage;
    if (imageUrl != null) {
      arrowImage = new Image(imageUrl.toString());
    } else {
      throw new IllegalStateException("image not found.");
    }

    arrow = new ImageView(arrowImage);
    arrow.setPreserveRatio(true);
    arrow.setFitWidth(ARROW_WIDTH);
    arrow.setFitHeight(ARROW_HEIGHT);
    setupArrowImageRotation();
  }

  private void updateArrow() {
    moveArrowToCenter();
    updateArrowRotation();
  }

  private void moveArrowToCenter() {
    var center = getInnerCenter();
    var arrowCenterOffsetX = ARROW_WIDTH / 2;
    var arrowCenterOffsetY = ARROW_HEIGHT / 2;
    arrow.relocate(center.getX() - arrowCenterOffsetX, center.getY() - arrowCenterOffsetY);
  }

  private void updateArrowRotation() {
    arrowRotation.setAngle(getAngleBetweenEndpoints());
  }

  private void setupArrowImageRotation() {
    arrowRotation = new Rotate(getAngleBetweenEndpoints());
    arrowRotation.setPivotX(ARROW_WIDTH / 2);
    arrowRotation.setPivotY(ARROW_HEIGHT / 2);
    arrow.getTransforms().add(arrowRotation);
  }

  private double getAngleBetweenEndpoints() {
    var entryPos = new Point2D(entryConnectorView.getCenterX(), entryConnectorView.getCenterY());
    var exitPos = new Point2D(exitConnectorView.getCenterX(), exitConnectorView.getCenterY());
    var thirdPoint = new Point2D(exitPos.getX(), entryPos.getY());
    //calculate angle needed for current orientation of the arrow.
    // the arrow should always point from entryPos towards endPos
    var angle = entryPos.angle(thirdPoint, exitPos);
    if (entryPos.getY() <= exitPos.getY()) {
      if (entryPos.getX() <= exitPos.getX()) {
        angle = -angle;
      } else {
        angle -= 180;
      }
    } else if (entryPos.getX() > exitPos.getX()) {
      angle = 180 - angle;
    }
    return 90 - angle;
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
    updateArrow();
  }

  @Override
  protected void setupDrag() {
    this.curve.setOnDragDetected(this::onDragDetected);
    this.curve.setOnMouseDragged(this::onMouseDragged);
    this.curve.setOnMouseDragReleased(this::onMouseDragReleased);
  }

  @Override
  protected void setupClicked() {
    this.curve.setOnMouseClicked(event -> this.onMouseClick.accept(event, getSegment()));
  }

  @Override
  protected void setupSelection() {
    setupCurve();
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