package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
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
 * A group view represents a {@link Group} in the hierarchy view.
 */
class GroupView extends ElementView<Group> {

  @FXML
  private GridPane groupViewSurface;
  @FXML
  private Label groupNameLabel;
  @FXML
  private Button deleteGroupButton;
  @FXML
  private ImageView deleteGroupButtonImageView;

  /**
   * Creates a new group view for a given {@code group}.
   *
   * @param translator the {@link LocalizedTextProvider} to use.
   * @param group the {@link Group} to use.
   * @param controller the {@link HierarchyController} to use.
   */
  GroupView(LocalizedTextProvider translator, Group group, HierarchyController controller) {
    super(translator, "GroupView.fxml", group, controller);

    setupView();
    setupListener();
  }

  private void setupView() {
    groupNameLabel.setText(getElement().getName());
    String styleSheetUrl = Objects.requireNonNull(
            getClass().getResource(ELEMENT_VIEW_STYLE_CSS_FILE)).toExternalForm();
    groupViewSurface.getStylesheets().add(styleSheetUrl);
    groupViewSurface.getStyleClass().add(UNSELECTED_STYLE_CLASS);
    String deleteButtonImageUrl = Objects.requireNonNull(
            getClass().getResource(DELETE_BUTTON_IMAGE_URL)).toExternalForm();
    deleteGroupButtonImageView.setImage(new Image(deleteButtonImageUrl));
  }

  private void setupListener() {
    deleteGroupButton.setOnMouseClicked(this::onDeleteGroupButtonClicked);
    groupViewSurface.setOnMouseClicked(this::onGroupViewSurfaceClicked);
  }

  private void onGroupViewSurfaceClicked(MouseEvent mouseEvent) {
    getController().clearSegmentSelection();
    selectElement(getElement());
  }

  private void selectElement(Element element) {
    if (element.isContainer()) {
      for (Element e : ((Group) element).getElements()) {
        selectElement(e);
      }
    } else {
      getController().addSegmentSelection((Segment) element);
    }
  }

  private void onDeleteGroupButtonClicked(MouseEvent mouseEvent) {
    getController().deleteGroup(getElement());
  }

  @Override
  public void notifyChange(Element unit) {
    groupNameLabel.setText(getElement().getName());
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