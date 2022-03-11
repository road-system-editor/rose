package edu.kit.rose.view.panel.hierarchy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.SearchBar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;


/**
 * The hierarchy panel shows the hierarchical order of the elements contained in the road system.
 */
public class HierarchyPanel extends FxmlContainer
                            implements SetObserver<Element, Element> {

  @Inject
  private HierarchyController hierarchyController;
  @Inject
  private RoadSystemController roadSystemController;
  @Inject
  private Project project;

  @FXML
  private SearchBar searchBar;
  @FXML
  private Button createGroupButton;
  @FXML
  private TreeView<Element> elementsTreeView;
  @FXML
  private BorderPane hierarchyLayout;

  private final ElementTreeItem rootItem;

  private final Map<Element, ElementTreeCell> elementToElementTreeCellMapping;

  /**
   * Creates an empty hierarchy view.
   */
  public HierarchyPanel() {
    super("HierarchyPanel.fxml");

    DisabledSelectionModel dsm = new DisabledSelectionModel();
    this.elementsTreeView.setSelectionModel(dsm);
    rootItem = new ElementTreeItem(null);
    this.elementToElementTreeCellMapping = new HashMap<>();

    setUp();
  }

  private void setUp() {
    searchBar.searchStringProperty().addListener(t -> {
      rootItem.updateFilter(searchBar.getSearchString());
    });

    elementsTreeView
        .setCellFactory(elementsTree -> new ElementTreeCell(
            roadSystemController, hierarchyController, getTranslator(),
            elementToElementTreeCellMapping));
    elementsTreeView.setShowRoot(false);
    elementsTreeView.setRoot(rootItem);

    elementsTreeView.setOnDragOver(this::onDragOver);
    elementsTreeView.setOnDragDropped(this::onDragDropped);
    createGroupButton.setOnMouseClicked(this::onCreateGroupButtonClicked);
  }

  private void onCreateGroupButtonClicked(MouseEvent mouseEvent) {
    this.hierarchyController.createGroup();
  }

  private void onDragOver(DragEvent dragEvent) {
    if (!dragEvent.getDragboard().hasContent(ElementTreeCell.DATA_FORMAT)) {
      return;
    }

    if (rootItem == ElementTreeCell.dragItem) {
      return;
    }

    dragEvent.acceptTransferModes(TransferMode.MOVE);
  }

  private void onDragDropped(DragEvent dragEvent) {
    if (!dragEvent.getDragboard().hasContent(ElementTreeCell.DATA_FORMAT)) {
      return;
    }

    if (ElementTreeCell.dragItem != null && ElementTreeCell.dragItem.getValue() != null) {
      this.hierarchyController.addElementToGroup(
          ElementTreeCell.dragItem.getValue(), this.project.getRoadSystem().getRootGroup());
    }

    dragEvent.setDropCompleted(true);
    dragEvent.consume();
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
    createGroupButton.setText(
        getTranslator().getLocalizedText(
            "view.panel.hierarchy.hierarchypanel.createGroupFromSelectionButton"));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return List.of(searchBar);
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);
    this.project.getRoadSystem().getRootGroup().addSubscriber(this);
  }


  @Override
  public void notifyAddition(Element unit) {
    Platform.runLater(() -> {
      TreeViewUtility.addElementToElementTreeItem(unit, rootItem);
    });
  }

  @Override
  public void notifyRemoval(Element unit) {
    Platform.runLater(() -> {
      rootItem.getInternalChildren().removeIf(child -> child.getValue() == unit);
    });
  }

  @Override
  public void notifyChange(Element unit) {

  }
}