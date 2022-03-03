package edu.kit.rose.view.roadsystem;

import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.ConnectionView;
import edu.kit.rose.view.commons.ConnectorView;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.roadsystem.Grid;
import edu.kit.rose.view.panel.segmentbox.SegmentBlueprint;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests if the Segments connect properly.
 */
public class SegmentConnectTest extends GuiTest {
  private List<Node> connectorViewList;
  private Grid grid;

  @BeforeEach
  void setUp() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    List<SegmentBlueprint> listCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof ListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    grid = lookup((Node node) -> node instanceof Grid).query();
    doubleClickOn(listCell.get(1));
    doubleClickOn(listCell.get(0));
    List<Node> segmentViewList = grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).toList();
    drag(segmentViewList.get(1)).interact(() -> dropBy(-60, 80));
    connectorViewList = lookup((Node node) ->
            node instanceof  ConnectorView).queryAll().stream().toList();
  }

  @Disabled("disabled till the pipeline will be adapted")
  @Test
  void testConnectBaseSegment() {
    ConnectorView connectorView1 = (ConnectorView) connectorViewList.get(4);
    ConnectorView connectorView2 = (ConnectorView) connectorViewList.get(1);
    drag(connectorView1).interact(() ->
            moveTo(connectorView2).moveBy(-1, 17).drop());
    List<Node> connectionViewList = grid.getChildren()
            .stream().filter(e -> e instanceof ConnectionView).toList();
    ConnectionView connectionView = (ConnectionView) connectionViewList.get(0);
    Assertions.assertEquals(1, connectionViewList.size());
    Rectangle connection = (Rectangle) connectionView.getChildren()
            .stream().filter(e -> e instanceof Rectangle).findFirst().orElseThrow();
    Point2D pos1 = connectorView1.getParent()
            .localToParent(connectorView1.getCenterX(), connectorView1.getCenterY());
    Point2D pos2 = connectorView2.getParent()
            .localToParent(connectorView2.getCenterX(), connectorView2.getCenterY());
    Assertions.assertEquals(connectorView1.getLayoutX(), connection.getLayoutX());
    Assertions.assertEquals(connectorView1.getLayoutY(), connection.getLayoutY());
    double distance = pos1.distance(pos2);
    Assertions.assertEquals(distance, connection.getWidth());
  }

  @Disabled("disabled till the pipeline will be adapted")
  @Test
  void testConnectEntrySegment() {
    ConnectorView connectorView1 = (ConnectorView) connectorViewList.get(4);
    ConnectorView connectorView2 = (ConnectorView) connectorViewList.get(1);
    drag(connectorView2).interact(() ->
            moveTo(connectorView1).moveBy(1, -17).drop());
    List<Node> connectionViewList = grid.getChildren()
            .stream().filter(e -> e instanceof ConnectionView).toList();
    ConnectionView connectionView = (ConnectionView) connectionViewList.get(0);
    Assertions.assertEquals(1, connectionViewList.size());
    Rectangle connection = (Rectangle) connectionView.getChildren()
            .stream().filter(e -> e instanceof Rectangle).findFirst().orElseThrow();
    Point2D pos1 = connectorView1.getParent()
            .localToParent(connectorView1.getCenterX(), connectorView1.getCenterY());
    Point2D pos2 = connectorView2.getParent()
            .localToParent(connectorView2.getCenterX(), connectorView2.getCenterY());
    Assertions.assertEquals(connectorView2.getLayoutX(), connection.getLayoutX());
    Assertions.assertEquals(connectorView2.getLayoutY(), connection.getLayoutY());
    double distance = pos1.distance(pos2);
    Assertions.assertEquals(distance, connection.getWidth());
  }
}
