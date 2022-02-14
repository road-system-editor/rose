package edu.kit.rose.view.panel.hierarchy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.SearchBar;
import java.util.Collection;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;


/**
 * The hierarchy panel shows the hierarchical order of the elements contained in the road system.
 */
public class HierarchyPanel extends FxmlContainer
                            implements DualSetObserver<Element, Connection, RoadSystem> {

  @Inject
  private HierarchyController controller;
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

  /**
   * Creates an empty hierarchy view.
   */
  public HierarchyPanel() {
    super("HierarchyPanel.fxml");

    DisabledSelectionModel dsm = new DisabledSelectionModel();
    this.elementsTreeView.setSelectionModel(dsm);
    rootItem = new ElementTreeItem(null);

    setUp();
  }

  private void setUp() {
    searchBar.searchStringProperty().addListener(t -> {
      rootItem.updateFilter(searchBar.getSearchString());
    });

    elementsTreeView
        .setCellFactory(elementsTree -> new ElementTreeCell(controller, getTranslator()));
    elementsTreeView.setShowRoot(false);
    elementsTreeView.setRoot(rootItem);

    elementsTreeView.setOnDragOver(this::onDragOver);
    elementsTreeView.setOnDragDropped(this::onDragDropped);
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

    TreeItem<Element> draggedItemParent = ElementTreeCell.dragItem.getParent();
    draggedItemParent.getChildren().remove(ElementTreeCell.dragItem);

    rootItem.getChildren().add(ElementTreeCell.dragItem);
    ElementTreeCell.dragItem = null;

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
  }

  @Override
  public void notifyAdditionSecond(Connection unit) {
  }

  @Override
  public void notifyRemovalSecond(Connection unit) {
  }

  @Override
  public void notifyAddition(Element unit) {
    ElementTreeItem treeItem = new ElementTreeItem(unit);
    rootItem.getChildren().add(treeItem);
  }

  @Override
  public void notifyRemoval(Element unit) {
    rootItem.getChildren().removeIf(child -> child.getValue() == unit);
  }

  @Override
  public void notifyChange(RoadSystem unit) {

  }
}