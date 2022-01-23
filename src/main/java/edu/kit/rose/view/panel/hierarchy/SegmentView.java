package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * A segment view represents a {@link Segment} in the hierarchy panel.
 */
class SegmentView extends ElementView<Segment> {

  @FXML
  private Label segmentNameLabel;
  @FXML
  private Button deleteSegmentButton;
  @FXML
  private GridPane segmentViewSurface;

  /**
   * Creates a new segment view for a given {@code segment}.
   *
   * @param translator the {@link LocalizedTextProvider} to use.
   * @param segment    the {@link Segment} to show.
   * @param controller the {@link HierarchyController} to use.
   */
  SegmentView(LocalizedTextProvider translator, Segment segment, HierarchyController controller) {
    super(translator, "SegmentView.fxml", segment, controller);

    segmentViewSurface.setOnMouseClicked(this::onSegmentViewSurfaceMouseClicked);
  }

  private void onSegmentViewSurfaceMouseClicked(MouseEvent mouseEvent) {
    getController().toggleSegmentSelection(getElement());
    mouseEvent.consume();
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }


  @Override
  public void notifyChange(Element unit) {
    segmentNameLabel.setText(getElement().getName());
  }

  private void toggleSelection() {
    getController().toggleSegmentSelection(getElement());
  }
}

