package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.ConnectorView;
import edu.kit.rose.view.commons.SearchBar;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.roadsystem.Grid;
import edu.kit.rose.view.panel.segment.EditableAttribute;
import edu.kit.rose.view.panel.segmentbox.SegmentBlueprint;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxListCell;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Tests the GUI functionality of hierarchy.
 */
public class TestHierarchy extends GuiTest {
  private static final String STREET_1 = "Lieblingsstrasse";
  private static final String STREET_2 = "Strasse 2";
  private List<SegmentBlueprint> segmentBoxListCell;
  private Grid grid;

  @BeforeEach
  void setUp() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    segmentBoxListCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    grid = lookup((Node node) -> node instanceof Grid).query();
  }

  /**
   * Represents T31.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testGroupStreetSegments() {
    drag(segmentBoxListCell.get(1)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));

    List<SegmentView> segmentViewList = grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).map(SegmentView.class::cast).toList();

    drag(segmentViewList.get(2)).interact(() -> dropBy(0, 100));
    drag(segmentViewList.get(1)).interact(() -> dropBy(0, -100));


    List<Node> connectorViewList = lookup((Node node) ->
            node instanceof ConnectorView).queryAll().stream().toList();
    drag(connectorViewList.get(3)).interact(()
            -> moveTo(connectorViewList.get(0)).moveBy(3, -25).drop());
    drag(connectorViewList.get(6)).interact(()
            -> moveTo(connectorViewList.get(1)).moveBy(-3, 25).drop());

    clickOn(segmentViewList.get(0));
    clickOn("#createGroupButton");
    List<ElementTreeCell> groupCells = getGroupListCells();
    Assertions.assertEquals(3, groupCells.size());
    clickOn(segmentViewList.get(1));
    press(KeyCode.CONTROL).clickOn(segmentViewList.get(2)).release(KeyCode.CONTROL);
    clickOn("#createGroupButton");
    List<ElementTreeCell> groupCellsAfter = getGroupListCells();
    Assertions.assertEquals(2, groupCellsAfter.size());
    drag(groupCellsAfter.get(0)).interact(() -> dropTo(groupCellsAfter.get(1)));
    clickOn();
    Assertions.assertTrue(segmentViewList.get(0).getDrawAsSelected());
    Assertions.assertTrue(segmentViewList.get(1).getDrawAsSelected());
    Assertions.assertTrue(segmentViewList.get(2).getDrawAsSelected());
  }

  /**
   * Represents T33.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testMoveElementsToGroup() {
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    List<SegmentView> segmentViewList = grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).map(SegmentView.class::cast).toList();
    drag(segmentViewList.get(0)).interact(() -> dropBy(0, 100));

    clickOn(segmentViewList.get(0));
    clickOn("#createGroupButton");

    clickOn(segmentViewList.get(1));
    clickOn("#createGroupButton");

    TreeView treeView = lookup("#elementsTreeView").query();
    treeView.getTreeItem(0).setExpanded(true);
    treeView.getTreeItem(2).setExpanded(true);
    List<ElementTreeCell> groupListCells = getGroupListCells();
    moveTo(groupListCells.get(1)).moveBy(0, 90);
    press(MouseButton.PRIMARY)
            .interact(() -> moveTo(groupListCells.get(0)).release(MouseButton.PRIMARY));
    Assertions.assertEquals(2, countChildren(treeView.getTreeItem(0)));
    Assertions.assertEquals("Base", treeView.getTreeItem(1).getValue());
    Assertions.assertEquals("Base", treeView.getTreeItem(2).getValue());
  }

  /**
   * Represents T27.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testElementSearch() {
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    List<SegmentView> segmentViewList = grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).map(SegmentView.class::cast).toList();
    drag(segmentViewList.get(0)).interact(() -> dropBy(0, 100));

    doubleClickOn(segmentViewList.get(0));
    VBox attributeList = lookup("#attributeList").query();
    EditableAttribute nameAttribute1 = (EditableAttribute) attributeList.getChildren().get(0);
    interact(() -> ((TextField)
            ((HBox) nameAttribute1.getChildren().get(0)).getChildren().get(1)).setText(STREET_1));

    clickOn(segmentBoxListCell.get(0)).doubleClickOn(segmentViewList.get(1));
    attributeList = lookup("#attributeList").query();
    EditableAttribute nameAttribute2 = (EditableAttribute) attributeList.getChildren().get(0);
    interact(() -> ((TextField)
            ((HBox) nameAttribute2.getChildren().get(0)).getChildren().get(1)).setText(STREET_2));
    SearchBar searchBar = (SearchBar) lookup("#hierarchyLayout")
            .<BorderPane>query().getChildren().get(0);
    clickOn(searchBar);
    enterStreetName();
    TreeView treeView = lookup("#elementsTreeView").query();
    Assertions.assertEquals(1, treeView.getExpandedItemCount());
    Assertions.assertEquals(STREET_1, ((Base) treeView.getTreeItem(0).getValue()).getName());
  }

  private List<ElementTreeCell> getGroupListCells() {
    return from(lookup("#elementsTreeView").<TreeView>query())
            .lookup((Node node) -> node instanceof TreeCell)
            .<ElementTreeCell>queryAll().stream()
            .filter(e -> !e.isEmpty()).toList();
  }

  private int countChildren(TreeItem treeItem) {
    int count = 0;

    if (treeItem != null) {
      ObservableList<TreeItem> children = treeItem.getChildren();
      if (children != null) {
        count += children.size();
        for (TreeItem child : children) {
          count += countChildren(child);
        }
      }
    }
    return count;
  }

  private void enterStreetName() {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    StringSelection stringSelection = new StringSelection(STREET_1);
    clipboard.setContents(stringSelection, stringSelection);
    press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
  }
}
