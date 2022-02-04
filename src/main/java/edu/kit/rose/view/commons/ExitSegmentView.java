package edu.kit.rose.view.commons;

import com.google.inject.Inject;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import java.net.URL;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

/**
 * An exit segment view is the visual representation of an exit street segment.
 */
public class ExitSegmentView extends CanvasSegmentView<Exit> {

  private static final String IMAGE_RESOURCE = "exit_segment_raw.png";
  private static final int IMAGE_WIDTH = 60;
  private static final int IMAGE_HEIGHT = 70;
  private static final double IMAGE_POS_OFFSET_X = -20.5;
  private static final double IMAGE_POS_OFFSET_Y = -38;

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
    setupConnectors();
    setupImage();
    getChildren().addAll(imageView, entryConnectorView, exitConnectorView, rampConnectorView);
  }

  private void setupConnectors() {
    /*this.getSegment().getConnectors().forEach(c -> {
      switch (c.getType()) {
        case ENTRY -> entryConnectorView = new ConnectorView(c, 15);
        case EXIT -> exitConnectorView = new ConnectorView(c, 15);
        case RAMP_EXIT, RAMP_ENTRY -> rampConnectorView = new ConnectorView(c, 6);
        default -> throw new IllegalStateException("segment is not a exit segment.");
      }
    });*/
  }

  private void setupImage() {
    var url = getClass().getResource(IMAGE_RESOURCE);
    var image = new Image(url.toString());
    imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setFitWidth(IMAGE_WIDTH);
    imageView.setFitHeight(IMAGE_HEIGHT);
    imageView.relocate(getSegment().getCenter().getX() + IMAGE_POS_OFFSET_X,
        getSegment().getCenter().getY() + IMAGE_POS_OFFSET_Y);
  }

  @Override
  public void redraw(GraphicsContext context) {

  }

  @Override
  public void notifyChange(Element unit) {

  }
}
