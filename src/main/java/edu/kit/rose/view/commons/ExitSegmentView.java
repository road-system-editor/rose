package edu.kit.rose.view.commons;

import com.google.inject.Inject;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

/**
 * An exit segment view is the visual representation of an exit street segment.
 */
public class ExitSegmentView extends SegmentView<Exit> {

  private static final String IMAGE_RESOURCE = "exit_segment_raw.png";
  private static final int IMAGE_WIDTH = 60;
  private static final int IMAGE_HEIGHT = 70;
  private static final double IMAGE_POS_OFFSET_X = -20.5;
  private static final double IMAGE_POS_OFFSET_Y = -38;

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
  public ExitSegmentView(Exit segment,
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
    connectorView.setCenterX(connectorView.getCenterX() - IMAGE_POS_OFFSET_X);
    connectorView.setCenterY(connectorView.getCenterY() - IMAGE_POS_OFFSET_Y);
  }

  private void setupImage() {
    var url = getClass().getResource(IMAGE_RESOURCE);
    var image = new Image(url.toString());
    imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setFitWidth(IMAGE_WIDTH);
    imageView.setFitHeight(IMAGE_HEIGHT);
  }

  private void updatePosition() {
    relocate(getSegment().getCenter().getX() + IMAGE_POS_OFFSET_X,
        getSegment().getCenter().getY() + IMAGE_POS_OFFSET_Y);
  }

  private void setupRotation() {
    rotation = new Rotate(getSegment().getRotation());
    rotation.setPivotX(-IMAGE_POS_OFFSET_X);
    rotation.setPivotY(-IMAGE_POS_OFFSET_Y);
    getTransforms().add(rotation);
  }

  @Override
  public void redraw() {
    updatePosition();
    rotation.setPivotX(-IMAGE_POS_OFFSET_X);
    rotation.setPivotY(-IMAGE_POS_OFFSET_Y);
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
}
