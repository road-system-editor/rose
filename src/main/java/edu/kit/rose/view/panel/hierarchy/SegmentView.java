package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * A segment view represents a {@link Segment} in the hierarchy panel.
 */
class SegmentView extends ElementView<Segment> {

  @FXML
  private Label label;

  /**
   * Creates a new segment view for a given {@code segment}.
   *
   * @param translator the {@link LocalizedTextProvider} to use.
   * @param segment    the {@link Segment} to show.
   * @param controller the {@link HierarchyController} to use.
   */
  SegmentView(LocalizedTextProvider translator, Segment segment, HierarchyController controller) {
    super(translator, "segment_view.fxml", segment, controller);
  }

  @Override
  public void notifyChange(Element unit) {

  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }

  @Override
  public void notifyAddition(Element unit) {

  }

  @Override
  public void notifyRemoval(Element unit) {

  }
}
