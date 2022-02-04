package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import javafx.scene.canvas.GraphicsContext;

/**
 * An entrance segment view is the visual representation of an entrance street segment.
 */
class EntranceSegmentView extends CanvasSegmentView<Entrance> {

  /**
   * Creates a new entrance segment view for a given exit {@code segment}.
   *
   * @param segment the segment to display.
   * @param controller the controller to use for handling position updates.
   * @param translator the translator to use for localizing label.
   */
  public EntranceSegmentView(Entrance segment, RoadSystemController controller,
                         LocalizedTextProvider translator) {
    super(segment, controller, translator);
  }

  @Override
  protected void redraw(GraphicsContext context) {

  }

  @Override
  public void notifyChange(Element unit) {

  }
}
