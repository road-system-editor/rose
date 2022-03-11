package edu.kit.rose.view.panel.roadsystem;

import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.segmentbox.SegmentBlueprint;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxListCell;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Tests the zoom and undo, redo functionality of the GUI.
 */
class TestRoadSystemTools extends GuiTest {
  private Grid grid;

  /**
   * Represents T12.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testUndoRedo() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    List<SegmentBlueprint> segmentBoxListCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    grid = lookup((Node node) -> node instanceof Grid).query();
    doubleClickOn(segmentBoxListCell.get(0));
    List<Node> segmentViewList = getSegmentViewList();
    Assertions.assertEquals(1, segmentViewList.size());
    clickOn("#undoButtonIcon");
    segmentViewList = getSegmentViewList();
    Assertions.assertEquals(0, segmentViewList.size());
    clickOn("#redoButtonIcon");
    segmentViewList = getSegmentViewList();
    Assertions.assertEquals(1, segmentViewList.size());
  }


  /**
   * Represents T13.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void test20TimesUndoRedo() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    List<SegmentBlueprint> segmentBoxListCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    grid = lookup((Node node) -> node instanceof Grid).query();
    for (int i = 0; i < 10; i++) {
      doubleClickOn(segmentBoxListCell.get(0));
      doubleClickOn(segmentBoxListCell.get(1));
    }

    List<Node> segmentViewList = getSegmentViewList();
    Assertions.assertEquals(20, segmentViewList.size());

    for (int i = 0; i < 20; i++) {
      clickOn("#undoButtonIcon");
      sleep(30);
    }

    segmentViewList = getSegmentViewList();
    Assertions.assertEquals(0, segmentViewList.size());

    for (int i = 0; i < 20; i++) {
      clickOn("#redoButtonIcon");
      sleep(30);
    }

    segmentViewList = getSegmentViewList();
    Assertions.assertEquals(20, segmentViewList.size());
  }

  /**
   * Represents T22.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testUndoShortcut() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    List<SegmentBlueprint> segmentBoxListCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    grid = lookup((Node node) -> node instanceof Grid).query();
    doubleClickOn(segmentBoxListCell.get(0));
    List<Node> segmentViewList = getSegmentViewList();
    Assertions.assertEquals(1, segmentViewList.size());
    clickOn(grid);
    press(KeyCode.CONTROL).press(KeyCode.Z).release(KeyCode.Z).release(KeyCode.CONTROL);
    segmentViewList = getSegmentViewList();
    Assertions.assertEquals(0, segmentViewList.size());
  }

  /**
   * Represents T31.
   */
  @Disabled("fails because the plus button does not zoomIn")
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testZoom() {
    ZoomSetting zoomSetting = lookup("#roadSystemPanel").<RoadSystemPanel>query().getZoomSetting();
    double level0 = zoomSetting.getZoomLevel();
    push(KeyCode.PLUS);
    double level1 = zoomSetting.getZoomLevel();
    Assertions.assertTrue(level1 < level0);

    push(KeyCode.PLUS);
    double level2 = zoomSetting.getZoomLevel();
    Assertions.assertTrue(level2 < level1);

    push(KeyCode.MINUS);
    double level3 = zoomSetting.getZoomLevel();
    Assertions.assertEquals(level1, level3);

    push(KeyCode.MINUS);
    double level4 = zoomSetting.getZoomLevel();
    Assertions.assertEquals(level0, level4);
  }

  private List<Node> getSegmentViewList() {
    return grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).toList();
  }
}
