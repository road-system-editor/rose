package edu.kit.rose.view.panel.hierarchy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.SearchBar;
import java.util.Collection;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;


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
  private ListView<Element> elementsListView;

  /**
   * Creates an empty hierarchy view.
   */
  public HierarchyPanel() {
    super("HierarchyPanel.fxml");
    elementsListView
        .setCellFactory(elementsTree -> new ElementListCell(controller, getTranslator()));

    Group g = new Group();
    g.addElement(new Entrance());
    elementsListView.getItems().add(g);
  }

  @Override
  public void notifyAdditionSecond(Connection unit) {

  }

  @Override
  public void notifyRemovalSecond(Connection unit) {

  }

  @Override
  public void notifyAddition(Element unit) {

  }

  @Override
  public void notifyRemoval(Element unit) {

  }

  @Override
  public void notifyChange(RoadSystem unit) {

  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return List.of();
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);
  }
}
