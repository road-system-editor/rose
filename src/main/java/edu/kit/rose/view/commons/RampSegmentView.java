package edu.kit.rose.view.commons;

import com.google.inject.Inject;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

/**
 * An exit segment view is the visual representation of an exit street segment.
 */
public abstract class RampSegmentView<T extends Segment> extends SegmentView<T> {

  private Rotate rotation;
  private  ImageView imageView;
  private ConnectorView entryConnectorView;
  private ConnectorView exitConnectorView;
  private ConnectorView rampConnectorView;

  @Inject
  private RoadSystemController roadSystemController;


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
    this.getSegment().getConnectors().forEach(c -> {
      switch (c.getType()) {
        case ENTRY -> entryConnectorView = new ConnectorView(15, c.getPosition());
        case EXIT -> exitConnectorView = new ConnectorView(15, c.getPosition());
        case RAMP_EXIT, RAMP_ENTRY -> rampConnectorView = new ConnectorView(6, c.getPosition());
        default -> throw new IllegalStateException("segment is not a exit segment.");
      }
    });
  }

  private void relocateConnectorView(ConnectorView connectorView) {
    connectorView.setCenterX(connectorView.getCenterX() - getImagePosOffsetX());
    connectorView.setCenterY(connectorView.getCenterY() - getImagePosOffsetY());
  }

  private void setupImage() {
    var url = getClass().getResource(getImageResource());
    var image = new Image(url.toString());
    imageView = new ImageView(image);
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
  public void redraw() {
    updatePosition();
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

  protected abstract double getImagePosOffsetX();

  protected abstract double getImagePosOffsetY();

  protected abstract String getImageResource();

  protected abstract int getImageWidth();

  protected abstract int getImageHeight();

}
