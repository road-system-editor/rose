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
public class ExitSegmentView extends RampSegmentView<Exit> {

  private static final String IMAGE_RESOURCE = "exit_segment_raw.png";
  private static final int IMAGE_WIDTH = 60;
  private static final int IMAGE_HEIGHT = 70;
  private static final double IMAGE_POS_OFFSET_X = -20.5;
  private static final double IMAGE_POS_OFFSET_Y = -38;

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
  }

  @Override
  protected double getImagePosOffsetX() {
    return IMAGE_POS_OFFSET_X;
  }

  @Override
  protected double getImagePosOffsetY() {
    return IMAGE_POS_OFFSET_Y;
  }

  @Override
  protected String getImageResource() {
    return IMAGE_RESOURCE;
  }

  @Override
  protected int getImageWidth() {
    return IMAGE_WIDTH;
  }

  @Override
  protected int getImageHeight() {
    return IMAGE_HEIGHT;
  }
}
