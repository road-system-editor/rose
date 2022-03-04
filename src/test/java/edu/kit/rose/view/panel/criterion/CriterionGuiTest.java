package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.ConnectorView;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.roadsystem.Grid;
import edu.kit.rose.view.panel.segmentbox.SegmentBlueprint;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxListCell;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;

/**
 * Include test scenarios for criteria.
 */
public class CriterionGuiTest extends GuiTest {
  private Grid grid;
  private List<Node> connectorViewList;

  @Test
  void testValidateCompatibilityCriterion() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    List<SegmentBlueprint> segmentBoxListCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    grid = lookup((Node node) -> node instanceof Grid).query();
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    List<Node> segmentViewList = grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).toList();
    drag(segmentViewList.get(3)).interact(() -> dropBy(-70, 80));
    drag(segmentViewList.get(2)).interact(() -> dropBy(70, 80));
    drag(segmentViewList.get(1)).interact(() -> dropBy(-70, -80));
    drag(segmentViewList.get(0)).interact(() -> dropBy(70, -80));
    clickOn("#validation");
    clickOn("#criteria");
    clickOn("#deleteAllButton");
    clickOn("#newButton");
    ListView<PlausibilityCriterion> criteriaListView = lookup("#criteriaList").queryListView();
    List<SegmentBlueprint> criteriaListCell = from(criteriaListView).lookup((Node node)
                    -> node.getParent() instanceof CriterionListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    clickOn(criteriaListCell.get(9));
    System.out.println(criteriaListCell.size());
    connectorViewList = lookup((Node node) ->
            node instanceof ConnectorView).queryAll().stream().toList();
    sleep(10000);
  }
}
