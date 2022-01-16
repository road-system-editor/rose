package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import javafx.scene.canvas.GraphicsContext;

/**
 * An entrance segment view is the visual representation of an entrance street segment.
 */
class EntranceSegmentView extends SegmentView<Entrance> {
  /**
   * Creates a new entrance segment view for a given entrance {@code segment}.
   *
   * @param segment the segment to display.
   * @param controller the controller to use for handling position updates.
   * @param translator the translator to use for localizing label.
   */
  EntranceSegmentView(Entrance segment, RoadSystemController controller,
                      LocalizedTextProvider translator) {
    super(segment, controller, translator);
  }

  @Override
  protected void redraw(GraphicsContext graphicsContext) {

  }

  @Override
  public void notifyChange(Element unit) {

  }
}
