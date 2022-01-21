package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import javafx.scene.canvas.GraphicsContext;

/**
 * An exit segment view is the visual representation of an exit street segment.
 */
class ExitSegmentView extends SegmentView<Exit> {
  /**
   * Creates a new entrance segment view for a given exit {@code segment}.
   *
   * @param segment the segment to display.
   * @param controller the controller to use for handling position updates.
   * @param translator the translator to use for localizing label.
   */
  ExitSegmentView(Exit segment, RoadSystemController controller, LocalizedTextProvider translator) {
    super(segment, controller, translator);
  }

  @Override
  protected void redraw(GraphicsContext graphicsContext) {

  }

  @Override
  public void notifyChange(Element unit) {

  }
}
