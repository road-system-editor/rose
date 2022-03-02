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
    SegmentView segmentViewBase = (SegmentView) segmentViewList.get(0);
    SegmentView segmentViewEntrance = (SegmentView) segmentViewList.get(1);
    drag(segmentViewEntrance).interact(() -> dropBy(-60, 80));
    connectorViewList = lookup((Node node) ->
            node instanceof  ConnectorView).queryAll().stream().toList();
  }

  @Test
  void testConnectBaseSegment() {
    drag(connectorViewList.get(4)).interact(() ->
            moveTo(connectorViewList.get(1)).moveBy(-1, 17).drop());
    List<Node> connectionViewList = grid.getChildren()
            .stream().filter(e -> e instanceof ConnectionView).toList();
    ConnectionView connectionView = (ConnectionView) connectionViewList.get(0);
    Assertions.assertEquals(1, connectionViewList.size());
    Rectangle connection = (Rectangle) connectionView.getChildren()
            .stream().filter(e -> e instanceof Rectangle).findFirst().orElseThrow();
    ConnectorView connectorView1 = (ConnectorView) connectorViewList.get(4);
    ConnectorView connectorView2 = (ConnectorView) connectorViewList.get(1);
    Point2D pos1 = connectorView1.getParent()
            .localToParent(connectorView1.getCenterX(), connectorView1.getCenterY());
    Point2D pos2 = connectorView2.getParent()
            .localToParent(connectorView2.getCenterX(), connectorView2.getCenterY());
    Assertions.assertEquals(connectorViewList.get(4).getLayoutX(), connection.getLayoutX());
    Assertions.assertEquals(connectorViewList.get(4).getLayoutY(), connection.getLayoutY());
    double distance = pos1.distance(pos2);
    Assertions.assertEquals(distance, connection.getWidth());
  }
}
