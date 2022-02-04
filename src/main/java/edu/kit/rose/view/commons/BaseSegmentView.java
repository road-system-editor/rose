package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;

/**
 * A base segment view is the visual representation of a base street segment.
 * It allows dragging its ends independently of each other.
 */
class BaseSegmentView extends SegmentView<Base> {
  /**
   * Creates a new base segment view for a given base {@code segment}.
   *
   * @param segment the segment to display.
   * @param controller the controller to use for handling position updates.
   * @param translator the translator to use for localizing label.
   */
  BaseSegmentView(Base segment, RoadSystemController controller, LocalizedTextProvider translator) {
    super(segment, controller, translator);
  }

  @Override
  protected void redraw() {

  }

  @Override
  public void notifyChange(Element unit) {

  }

  @Override
  public void notifyAddition(Element unit) {

  }

  @Override
  public void notifyRemoval(Element unit) {

  }
}
