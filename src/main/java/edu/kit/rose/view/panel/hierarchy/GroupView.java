package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * A group view represents a {@link Group} in the hierarchy view.
 */
class GroupView extends ElementView<Group> {

  @FXML
  private TitledPane titledPane;

  @FXML
  private VBox titledPaneContent;

  /**
   * Creates a new group view for a given {@code group}.
   *
   * @param translator the {@link LocalizedTextProvider} to use.
   * @param group the {@link Group} to use.
   * @param controller the {@link HierarchyController} to use.
   */
  GroupView(LocalizedTextProvider translator, Group group, HierarchyController controller) {
    super(translator, "GroupView.fxml", group, controller);
    titledPane.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    this.getStyleClass().add("-fx-background-radius: 5.0;");

    for (Element e : group.getElements()) {
      if (e.isContainer()) {
        titledPaneContent.getChildren().add(new GroupView(translator, (Group) e, controller));
      } else {
        titledPaneContent.getChildren().add(new SegmentView(translator, (Segment) e, controller));
      }

    }
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

  private Pane getHeader() {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("SegmentView.fxml"));
    loader.setController(this);
    Pane pane = new Pane();
    loader.setRoot(pane);
    try {
      loader.load();
    } catch (Exception ex) {
      return null;
    }
    return pane;
  }
}
