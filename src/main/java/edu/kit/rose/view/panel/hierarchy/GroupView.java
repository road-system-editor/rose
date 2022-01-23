package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.UnmountUtility;
import java.nio.Buffer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * A group view represents a {@link Group} in the hierarchy view.
 */
class GroupView extends ElementView<Group> {

  @FXML
  private Label segmentNameLabel;
  @FXML
  private Button deleteGroupButton;

  /**
   * Creates a new group view for a given {@code group}.
   *
   * @param translator the {@link LocalizedTextProvider} to use.
   * @param group the {@link Group} to use.
   * @param controller the {@link HierarchyController} to use.
   */
  GroupView(LocalizedTextProvider translator, Group group, HierarchyController controller) {
    super(translator, "GroupView.fxml", group, controller);

    segmentNameLabel.setText(group.getName());
    deleteGroupButton.setOnMouseClicked(this::onDeleteGroupButtonClicked);
  }

  private void onDeleteGroupButtonClicked(MouseEvent mouseEvent) {
    getController().deleteGroup(getElement());
  }

  @Override
  public void notifyChange(Element unit) {
    segmentNameLabel.setText(getElement().getName());
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}