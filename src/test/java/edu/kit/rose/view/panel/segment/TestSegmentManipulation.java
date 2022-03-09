package edu.kit.rose.view.panel.segment;

import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.roadsystem.Grid;
import edu.kit.rose.view.panel.segmentbox.SegmentBlueprint;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxListCell;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Tests the duplicate, delete, drag and drop,
 * and rotation of street segments.
 */
public class TestSegmentManipulation extends GuiTest {
  private static final String NAME = "test";
  private static final String LENGTH = "600.0";
  private Grid grid;
  private List<SegmentView> segmentViewList;
  private List<SegmentBlueprint> segmentBoxListCell;

  @BeforeEach
  void setUp() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    segmentBoxListCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    grid = lookup((Node node) -> node instanceof Grid).query();
    doubleClickOn(segmentBoxListCell.get(1));
    segmentViewList = getSegmentViewList();
    drag(segmentViewList.get(0)).interact(() -> dropBy(0, 100));
    doubleClickOn(segmentBoxListCell.get(1));
    segmentViewList = getSegmentViewList();
    drag(segmentViewList.get(1)).interact(() -> dropBy(0, -100));
  }

  /**
   * Represents T11.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testDuplicateSegment() {
    doubleClickOn(segmentViewList.get(0));
    VBox attributeList = lookup("#attributeList").query();
    EditableAttribute nameAttribute1 = (EditableAttribute) attributeList.getChildren().get(0);
    EditableAttribute lengthAttribute1 = (EditableAttribute) attributeList.getChildren().get(3);
    interact(() -> ((TextField)
            ((HBox) nameAttribute1.getChildren().get(0)).getChildren().get(1)).setText(NAME));
    interact(() -> ((TextField)
            ((HBox) lengthAttribute1.getChildren().get(0)).getChildren().get(1)).setText(LENGTH));
    moveBy(-50, 0);
    press(MouseButton.PRIMARY).release(MouseButton.PRIMARY);
    clickOn(segmentViewList.get(0));
    press(KeyCode.CONTROL).press(KeyCode.D).release(KeyCode.D).release(KeyCode.CONTROL);
    segmentViewList = getSegmentViewList();
    Assertions.assertEquals(3, segmentViewList.size());
    doubleClickOn(segmentViewList.get(2));
    attributeList = lookup("#attributeList").query();
    EditableAttribute nameAttribute2 = (EditableAttribute) attributeList.getChildren().get(0);
    EditableAttribute lengthAttribute2 = (EditableAttribute) attributeList.getChildren().get(3);
    Assertions.assertEquals(NAME, ((TextField)
            ((HBox) nameAttribute2.getChildren().get(0)).getChildren().get(1)).getText());
    Assertions.assertEquals(LENGTH, ((TextField)
            ((HBox) lengthAttribute2.getChildren().get(0)).getChildren().get(1)).getText());
  }

  /**
   * Represents T18.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testDeleteSegment() {
    //clickOn()
  }

  private List<SegmentView> getSegmentViewList() {
    return grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).map(SegmentView.class::cast).toList();
  }
}
