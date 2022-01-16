package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * A segment view represents a {@link Segment} in the hierarchy panel.
 */
class SegmentView extends ElementView<Segment> {
  @FXML
  private Label label;

  /**
   * Creates a new segment view for a given \lstinline{segment}.
   *
   * @param translator
   * @param segment
   * @param controller
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
}
