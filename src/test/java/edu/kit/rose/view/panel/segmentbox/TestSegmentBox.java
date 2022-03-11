package edu.kit.rose.view.panel.segmentbox;

import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.roadsystem.Grid;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Tests the SegmentBox Component of the GUI.
 */
class TestSegmentBox extends GuiTest {
  private List<SegmentBlueprint> listCell;
  private Grid grid;

  @BeforeEach
  void setUp() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    listCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof ListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    grid = lookup((Node node) -> node instanceof Grid).query();
  }

  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testSetStreetSegmentDoubleClick() {
    doubleClickOn(listCell.get(1));
    List<Node> segmentViewList = grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).toList();
    Assertions.assertEquals(1, segmentViewList.size());
    SegmentView<?> segmentView = (SegmentView<?>) segmentViewList.get(0);
    Assertions.assertEquals(SegmentType.ENTRANCE, segmentView.getSegment().getSegmentType());
  }

  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testSetStreetSegmentDragAndDrop() {
    drag(listCell.get(2)).interact(() -> dropTo(grid));
    List<Node> segmentViewList = grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).toList();
    Assertions.assertEquals(1, segmentViewList.size());
    SegmentView<?> segmentView = (SegmentView<?>) segmentViewList.get(0);
    Assertions.assertEquals(SegmentType.EXIT, segmentView.getSegment().getSegmentType());
  }
}
