package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
  @FXML
  private ImageView deleteGroupButtonImageView;

  /**
   * Creates a new segment view for a given {@code segment}.
   *
   * @param translator the {@link LocalizedTextProvider} to use.
   * @param segment    the {@link Segment} to show.
   * @param controller the {@link HierarchyController} to use.
   */
  SegmentView(LocalizedTextProvider translator, Segment segment, HierarchyController controller) {
    super(translator, "SegmentView.fxml", segment, controller);

    deleteSegmentButton.setOnMouseClicked(this::onDeleteSegmentButtonClicked);
    segmentViewSurface.setOnMouseClicked(this::onSegmentViewSurfaceMouseClicked);
    String styleSheetUrl = Objects.requireNonNull(
            getClass().getResource(ELEMENT_VIEW_STYLE_CSS_FILE)).toExternalForm();
    segmentViewSurface.getStylesheets().add(styleSheetUrl);
    segmentViewSurface.getStyleClass().add(UNSELECTED_STYLE_CLASS);
    String deleteButtonImageUrl = Objects.requireNonNull(
            getClass().getResource(DELETE_BUTTON_IMAGE_URL)).toExternalForm();
    deleteGroupButtonImageView.setImage(new Image(deleteButtonImageUrl));
  }

  private void onSegmentViewSurfaceMouseClicked(MouseEvent mouseEvent) {
    getController().toggleSegmentSelection(getElement());
    mouseEvent.consume();
  }

  private void onDeleteSegmentButtonClicked(MouseEvent mouseEvent) {
    //TODO: delete element
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

  @Override
  public void notifyAddition(Element unit) {
    if (unit == getElement()) {
      segmentViewSurface.getStyleClass().remove(UNSELECTED_STYLE_CLASS);
      segmentViewSurface.getStyleClass().add(SELECTED_STYLE_CLASS);
    }
  }

  @Override
  public void notifyRemoval(Element unit) {
    if (unit == getElement()) {
      segmentViewSurface.getStyleClass().remove(SELECTED_STYLE_CLASS);
      segmentViewSurface.getStyleClass().add(UNSELECTED_STYLE_CLASS);
    }
  }
}

