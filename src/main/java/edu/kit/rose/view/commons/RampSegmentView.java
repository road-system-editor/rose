package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.RampSegment;
import java.util.LinkedList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

/**
 * An exit segment view is the visual representation of an exit street segment.
 */
public abstract class RampSegmentView<T extends RampSegment> extends SegmentView<T> {

  private static final int RAMP_WIDTH = 6;

  private Rotate rotation;
  private ImageView imageView;
  private ConnectorView entryConnectorView;
  private ConnectorView exitConnectorView;
  private ConnectorView rampConnectorView;
  private List<ConnectorView> connectorViews;
  private Image defaultImage;
  private Image selectedImage;

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
    updatePosition();
    getChildren().addAll(imageView, exitConnectorView, entryConnectorView, rampConnectorView);

    relocateConnectorView(entryConnectorView);
    relocateConnectorView(exitConnectorView);
    relocateConnectorView(rampConnectorView);
  }

  private void setupConnectors() {
    entryConnectorView =
        new ConnectorView(MAIN_STREET_RADIUS,
            this.getSegment().getEntry(), this::setDraggedConnectorView);
    exitConnectorView =
        new ConnectorView(MAIN_STREET_RADIUS,
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
    if (defaultUrl != null && selectedUrl != null) {
      defaultImage = new Image(defaultUrl.toString());
      selectedImage = new Image(selectedUrl.toString());
    } else {
      throw new IllegalStateException("image not found.");
    }

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

  @Override
  public void draw() {
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

  @Override
  protected void setupDrag() {
    setOnDragDetected(this::onDragDetected);
    setOnMouseDragged(this::onMouseDragged);
  }

  protected abstract double getImagePosOffsetX();

  protected abstract double getImagePosOffsetY();

  protected abstract String getDefaultImageResource();

  protected abstract String getSelectedImageResource();

  protected abstract int getImageWidth();

  protected abstract int getImageHeight();

}
