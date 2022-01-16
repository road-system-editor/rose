package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import javafx.scene.canvas.GraphicsContext;

class BaseSegmentView extends SegmentView<Base> {
  BaseSegmentView(Base segment, RoadSystemController controller, LocalizedTextProvider translator) {
    super(segment, controller, translator);
  }

  @Override
  protected void redraw(GraphicsContext graphicsContext) {

  }

  @Override
  public void notifyChange(Element unit) {

  }
}
