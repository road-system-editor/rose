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
 * Tests the attribute/bulk editor of the GUI.
 */
public class TestAttributeEditor extends GuiTest {
  private List<SegmentView> segmentViewList;

  @BeforeEach
  void setUp() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    List<SegmentBlueprint> segmentBoxListCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    Grid grid = lookup((Node node) -> node instanceof Grid).query();
    doubleClickOn(segmentBoxListCell.get(1));
    doubleClickOn(segmentBoxListCell.get(2));
    segmentViewList = grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).map(SegmentView.class::cast).toList();
    drag(segmentViewList.get(1)).interact(() -> dropBy(0, 100));
  }


  /**
   * Represents T4.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testEnterAttribute() {
    doubleClickOn(segmentViewList.get(1));
    VBox attributeList = lookup("#attributeList").query();
    EditableAttribute nameAttribute1 = (EditableAttribute) attributeList.getChildren().get(0);
    moveTo(nameAttribute1).moveBy(100, 0).press(MouseButton.PRIMARY).release(MouseButton.PRIMARY);
    for (int i = 0; i < 8; i++) {
      push(KeyCode.BACK_SPACE);
    }
    push(KeyCode.A);
    EditableAttribute lengthAttribute1 = (EditableAttribute) attributeList.getChildren().get(2);
    moveTo(lengthAttribute1).moveBy(100, 0).press(MouseButton.PRIMARY).release(MouseButton.PRIMARY);
    push(KeyCode.BACK_SPACE).push(KeyCode.BACK_SPACE);
    push(KeyCode.DIGIT1);
    moveBy(-200, 0);
    press(MouseButton.PRIMARY).release(MouseButton.PRIMARY);
    doubleClickOn(segmentViewList.get(1));
    attributeList = lookup("#attributeList").query();
    EditableAttribute nameAttribute2 = (EditableAttribute) attributeList.getChildren().get(0);
    EditableAttribute lengthAttribute2 = (EditableAttribute) attributeList.getChildren().get(2);
    Assertions.assertEquals("a", ((TextField)
            ((HBox) nameAttribute2.getChildren().get(0)).getChildren().get(1)).getText());
    Assertions.assertEquals("1", ((TextField)
            ((HBox) lengthAttribute2.getChildren().get(0)).getChildren().get(1)).getText());
  }

  /**
   * Represents T19.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testBulkEdit() {
    clickOn(segmentViewList.get(0));
    press(KeyCode.CONTROL).clickOn(segmentViewList.get(1))
            .doubleClickOn(segmentViewList.get(0)).release(KeyCode.CONTROL);
    VBox attributeList = lookup("#attributeList").query();
    EditableAttribute lanesAttribute = (EditableAttribute) attributeList.getChildren().get(2);
    ((TextField) ((HBox) lanesAttribute.getChildren().get(0)).getChildren().get(1)).setText("3");

    doubleClickOn(segmentViewList.get(1));
    attributeList = lookup("#attributeList").query();
    lanesAttribute = (EditableAttribute) attributeList.getChildren().get(4);
    Assertions.assertEquals("3", ((TextField)
            ((HBox) lanesAttribute.getChildren().get(0)).getChildren().get(1)).getText());

    moveBy(-100, 0).press(MouseButton.PRIMARY).release(MouseButton.PRIMARY);
    doubleClickOn(segmentViewList.get(0));
    attributeList = lookup("#attributeList").query();
    lanesAttribute = (EditableAttribute) attributeList.getChildren().get(4);
    Assertions.assertEquals("3", ((TextField)
            ((HBox) lanesAttribute.getChildren().get(0)).getChildren().get(1)).getText());
  }
}
