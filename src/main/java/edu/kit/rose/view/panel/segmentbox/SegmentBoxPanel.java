package edu.kit.rose.view.panel.segmentbox;

import com.google.inject.Inject;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;


/**
 * The segment box panel provides an overview over the available street segment types which can
 * be created, as specified in PF11.1.7.
 */
public class SegmentBoxPanel extends FxmlContainer {

  @FXML
  private ListView<SegmentType> blueprintListView;

  /**
   * The controller to use for segment creation.
   */
  @Inject
  private RoadSystemController controller;

  /**
   * A scrollable list of segment blueprints is contained within the panel.
   */
  private List<SegmentBlueprint> blueprints;

  /**
   * Creates a new segment box panel.
   */
  public SegmentBoxPanel() {
    super("SegmentBoxPanel.fxml");

    blueprintListView.setCellFactory(
            listView -> new SegmentBoxListCell(this.controller, getTranslator()));
    blueprintListView.getItems().addAll(SegmentType.values());
    blueprintListView.getStylesheets()
            .add("edu/kit/rose/view/panel/segmentbox/SegmentBoxListView.css");
    blueprintListView.getStyleClass().add("list-cell");
    blueprintListView.getSelectionModel().clearSelection();
    blueprintListView.setSelectionModel(new DisabledSelectionModel<>());
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
    blueprintListView.refresh();
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
