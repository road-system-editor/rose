package edu.kit.rose.view.commons;

import com.google.inject.Inject;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.RampSegment;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.LinkedList;
import java.util.List;
import java.util.List;
import java.util.function.Supplier;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * An exit segment view is the visual representation of an exit street segment.
 */
public abstract class RampSegmentView<T extends RampSegment> extends SegmentView<T> {

  private static final int MAIN_STREET_WIDTH = 15;
  private static final int RAMP_WIDTH = 6;

  private Rotate rotation;
  private ImageView imageView;
  private ConnectorView entryConnectorView;
  private ConnectorView exitConnectorView;
  private ConnectorView rampConnectorView;
  private List<ConnectorView> connectorViews;
  private Image defaultImage;
  private Image selectedImage;

  private Position initialPos;
  private Point2D startPoint;


  /**
   * Creates a new segment view that acts as visual representation of a given segment.
   *
   * @param segment    the segment to create a visual representation for
   * @param controller the road system controller.
   * @param translator the translator.
   */
  public RampSegmentView(T segment,
                         RoadSystemController controller,
                         LocalizedTextProvider translator) {
    super(segment, controller, translator);
    segment.addSubscriber(this);
    setup();
  }

  private void setup() {
    setupRotation();
    setupConnectors();
    setupImage();
    setupDragging();
    updatePosition();
    getChildren().addAll(imageView, exitConnectorView, entryConnectorView, rampConnectorView);

    relocateConnectorView(entryConnectorView);
    relocateConnectorView(exitConnectorView);
    relocateConnectorView(rampConnectorView);
  }

  private void setupConnectors() {
    entryConnectorView =
        new ConnectorView(MAIN_STREET_WIDTH,
            this.getSegment().getEntry(), this::setDraggedConnectorView);
    exitConnectorView =
        new ConnectorView(MAIN_STREET_WIDTH,
            this.getSegment().getExit(), this::setDraggedConnectorView);
    rampConnectorView =
        new ConnectorView(RAMP_WIDTH,
            this.getSegment().getRamp(), this::setDraggedConnectorView);
    this.connectorViews = List.of(entryConnectorView, exitConnectorView, rampConnectorView);
  }

  private void relocateConnectorView(ConnectorView connectorView) {
    connectorView.setCenterX(connectorView.getCenterX() - getImagePosOffsetX());
    connectorView.setCenterY(connectorView.getCenterY() - getImagePosOffsetY());
  }

  private void setupImage() {
    var defaultUrl = getClass().getResource(getDefaultImageResource());
    var selectedUrl = getClass().getResource(getSelectedImageResource());
    defaultImage = new Image(defaultUrl.toString());
    selectedImage = new Image(selectedUrl.toString());
    imageView = new ImageView(defaultImage);
    imageView.setPreserveRatio(true);
    imageView.setFitWidth(getImageWidth());
    imageView.setFitHeight(getImageHeight());
  }

  private void updatePosition() {
    relocate(getSegment().getCenter().getX() + getImagePosOffsetX(),
        getSegment().getCenter().getY() + getImagePosOffsetY());
  }

  private void setupRotation() {
    rotation = new Rotate(getSegment().getRotation());
    rotation.setPivotX(-getImagePosOffsetX());
    rotation.setPivotY(-getImagePosOffsetY());
    getTransforms().add(rotation);
  }

  private void setupDragging() {
    this.setOnMousePressed(mouseEvent -> {
      startPoint = localToParent(mouseEvent.getX(), mouseEvent.getY());
      initialPos = new Position(startPoint.getX(), startPoint.getY());
      if (getDrawAsSelected()) {
        return;
      }
      if (mouseEvent.isControlDown()) {
        controller.toggleSegmentSelection(this.getSegment());
      } else {
        controller.putSegmentSelection(this.getSegment());
      }
    });

    this.setOnDragDetected(mouseEvent -> {
      controller.beginDragStreetSegment(this.initialPos);
      startFullDrag();
    });

    this.setOnMouseDragged(mouseEvent -> {
      var currentPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
      var movement = new Movement(currentPos.getX() - startPoint.getX(),
          currentPos.getY() - startPoint.getY());
      if (this.canBeMoved(this, movement)) {
        controller.dragStreetSegments(movement);
        startPoint = currentPos;
      }
      if (this.draggedConnectorView != null) {
        if (this.onConnectorViewDragged != null) {
          this.onConnectorViewDragged.accept(draggedConnectorView);
        }
      }
      mouseEvent.consume();
    });

    this.setOnMouseDragReleased(mouseEvent -> {
      var releasePoint = localToParent(mouseEvent.getX(), mouseEvent.getY());
      var releasePosition = new Position(releasePoint.getX(), releasePoint.getY());
      if (this.draggedConnectorView != null) {
        controller.endDragStreetSegment(releasePosition, draggedConnectorView.getConnector());
        if (this.onConnectorViewDragEnd != null) {
          onConnectorViewDragEnd.accept(draggedConnectorView);
        }
      } else {
        controller.endDragStreetSegment(releasePosition);
      }
      this.draggedConnectorView = null;
    });
  }

  @Override
  public void redraw() {
    updatePosition();
    if (getDrawAsSelected()) {
      imageView.setImage(selectedImage);
    } else {
      imageView.setImage(defaultImage);
    }
    rotation.setPivotX(-getImagePosOffsetX());
    rotation.setPivotY(-getImagePosOffsetY());
    rotation.setAngle(getSegment().getRotation());
  }

  @Override
  public void notifyChange(Element unit) {
    this.draw();
  }

  @Override
  public void notifyAddition(Element unit) {
  }

  @Override
  public void notifyRemoval(Element unit) {
  }

  @Override
  public List<ConnectorView> getConnectorViews() {
    return new LinkedList<>(this.connectorViews);
  }

  protected abstract double getImagePosOffsetX();

  protected abstract double getImagePosOffsetY();

  protected abstract String getDefaultImageResource();

  protected abstract String getSelectedImageResource();

  protected abstract int getImageWidth();

  protected abstract int getImageHeight();

}
