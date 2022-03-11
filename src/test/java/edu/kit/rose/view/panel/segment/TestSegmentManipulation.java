package edu.kit.rose.view.panel.segment;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.ConnectorView;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.roadsystem.Grid;
import edu.kit.rose.view.panel.roadsystem.RoadSystemPanel;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Tests the duplicate, delete, drag and drop,
 * and rotation of street segments.
 */
class TestSegmentManipulation extends GuiTest {
  private static final String NAME = "test";
  private static final String LENGTH = "600.0";
  private Grid grid;
  private List<? extends SegmentView<?>> segmentViewList;
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
  @Disabled("fails because of the notify system")
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testDuplicateSegment() {
    doubleClickOn(segmentViewList.get(0));
    VBox attributeList = lookup("#attributeList").query();
    EditableAttribute<?> nameAttribute1 =
            (EditableAttribute<?>) attributeList.getChildren().get(0);
    EditableAttribute<?> lengthAttribute1 =
            (EditableAttribute<?>) attributeList.getChildren().get(3);
    interact(() -> ((TextField)
            ((HBox) nameAttribute1.getChildren().get(0)).getChildren().get(1)).setText(NAME));
    interact(() -> ((TextField)
            ((HBox) lengthAttribute1.getChildren().get(0)).getChildren().get(1)).setText(LENGTH));
    moveBy(-50, 0);
    press(MouseButton.PRIMARY).release(MouseButton.PRIMARY);
    List<Node> connectorViewList = lookup((Node node) ->
            node instanceof ConnectorView).queryAll().stream().toList();
    drag(connectorViewList.get(0)).interact(() ->
            moveTo(connectorViewList.get(4)).moveBy(-30, 0).drop());
    press(KeyCode.CONTROL).clickOn(segmentViewList.get(0)).clickOn(segmentViewList.get(1));
    press(KeyCode.CONTROL).press(KeyCode.D).release(KeyCode.D).release(KeyCode.CONTROL);
    segmentViewList = getSegmentViewList();

    Assertions.assertEquals(4, segmentViewList.size());

    doubleClickOn(segmentViewList.get(2));
    attributeList = lookup("#attributeList").query();
    EditableAttribute<?> nameAttribute2 =
            (EditableAttribute<?>) attributeList.getChildren().get(0);
    EditableAttribute<?> lengthAttribute2 =
            (EditableAttribute<?>) attributeList.getChildren().get(3);

    Assertions.assertEquals(NAME, ((TextField)
            ((HBox) nameAttribute2.getChildren().get(0)).getChildren().get(1)).getText());
    Assertions.assertEquals(LENGTH, ((TextField)
            ((HBox) lengthAttribute2.getChildren().get(0)).getChildren().get(1)).getText());

    moveBy(-50, 0);
    press(MouseButton.PRIMARY).release(MouseButton.PRIMARY);

    Assertions.assertTrue(lookup("#roadSystemPanel")
            .<RoadSystemPanel>query().getRoadSystem().getConnections(
                    segmentViewList.get(2).getSegment()).iterator().next()
            .getConnectors().contains(
                    segmentViewList.get(3).getSegment().getConnectors().stream().toList().get(1)));
  }

  /**
   * Represents T18.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testDeleteSegment() {
    clickOn(segmentViewList.get(0));
    push(KeyCode.DELETE);
    segmentViewList = getSegmentViewList();
    Assertions.assertEquals(1, segmentViewList.size());
    clickOn(segmentViewList.get(0));
    push(KeyCode.DELETE);
    segmentViewList = getSegmentViewList();
    Assertions.assertEquals(0, segmentViewList.size());
  }

  /**
   * Represents T9.
   */
  @Disabled("fails because of the notify system")
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testMoveSelectedSegments() {
    doubleClickOn(segmentBoxListCell.get(1));
    segmentViewList = getSegmentViewList();
    moveTo(grid).moveBy(-50, -130);
    press(KeyCode.CONTROL).press(MouseButton.PRIMARY).moveBy(90, 170)
            .release(MouseButton.PRIMARY).release(KeyCode.CONTROL);
    Assertions.assertFalse(segmentViewList.get(0).getDrawAsSelected());
    Assertions.assertTrue(segmentViewList.get(1).getDrawAsSelected());
    Assertions.assertTrue(segmentViewList.get(2).getDrawAsSelected());
    final Position initialPosition1 =
            new Position(segmentViewList.get(0).getSegment().getCenter().getX(),
            segmentViewList.get(0).getSegment().getCenter().getY());
    final Position initialPosition2 =
            new Position(segmentViewList.get(1).getSegment().getCenter().getX(),
            segmentViewList.get(1).getSegment().getCenter().getY());
    final Position initialPosition3 =
            new Position(segmentViewList.get(2).getSegment().getCenter().getX(),
            segmentViewList.get(2).getSegment().getCenter().getY());

    drag(segmentViewList.get(2)).interact(() -> dropBy(10, 30));
    segmentViewList = getSegmentViewList();

    Assertions.assertEquals(initialPosition1.getX(),
            segmentViewList.get(0).getSegment().getCenter().getX());
    Assertions.assertEquals(initialPosition1.getY(),
            segmentViewList.get(0).getSegment().getCenter().getY());
    Assertions.assertEquals(initialPosition2.getX() + 10,
            segmentViewList.get(1).getSegment().getCenter().getX());
    Assertions.assertEquals(initialPosition2.getY() + 30,
            segmentViewList.get(1).getSegment().getCenter().getY());
    Assertions.assertEquals(initialPosition3.getX() + 10,
            segmentViewList.get(2).getSegment().getCenter().getX());
    Assertions.assertEquals(initialPosition3.getY() + 30,
            segmentViewList.get(2).getSegment().getCenter().getY());
  }

  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testRotateSegment() {
    clickOn(segmentViewList.get(0));
    push(KeyCode.R).push(KeyCode.R).push(KeyCode.R)
            .push(KeyCode.R).push(KeyCode.R).push(KeyCode.R);
    List<Node> connectorViewList = lookup((Node node) ->
            node instanceof ConnectorView).queryAll().stream().toList();
    Assertions.assertEquals(22,
            connectorViewList.get(0).localToParent(
                    connectorViewList.get(0).getBoundsInLocal()).getCenterX());
    Assertions.assertEquals(5,
            connectorViewList.get(0).localToParent(
                    connectorViewList.get(0).getBoundsInLocal()).getCenterY());
    Assertions.assertEquals(22,
            connectorViewList.get(1).localToParent(
                    connectorViewList.get(1).getBoundsInLocal()).getCenterX());
    Assertions.assertEquals(65,
            connectorViewList.get(1).localToParent(
                    connectorViewList.get(1).getBoundsInLocal()).getCenterY());
  }

  private List<? extends SegmentView<?>> getSegmentViewList() {
    return grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView)
            .map(node -> (SegmentView<?>) node).toList();
  }
}
