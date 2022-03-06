package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.ConnectorView;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.roadsystem.Grid;
import edu.kit.rose.view.panel.segmentbox.SegmentBlueprint;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxListCell;
import edu.kit.rose.view.panel.violation.ViolationHandle;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Include test scenarios for criteria.
 */
public class CriterionGuiTest extends GuiTest {
  private static final String VIOLATION_MESSAGE = "B1ASE and B0ASE are incompatible";
  private Grid grid;
  private List<Node> connectorViewList;
  private List<Node> segmentViewList;

  @BeforeEach
  void setUp() {
    putSegmentsOnGrid();
    configureCriterion();
    doubleClickOn(segmentViewList.get(1));
    clickOn(lookup("#attributeList").<VBox>query().getChildren().get(0));
    type(KeyCode.DIGIT1);
    clickOn(lookup("#attributeList").<VBox>query().getChildren().get(4));
    type(KeyCode.DIGIT0);
    clickOn(grid);
    doubleClickOn(segmentViewList.get(0));
    clickOn(lookup("#attributeList").<VBox>query().getChildren().get(0));
    type(KeyCode.DIGIT0);
    clickOn(grid);
    connectorViewList = lookup((Node node) ->
            node instanceof ConnectorView).queryAll().stream().toList();
  }


  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testValidateCompatibilityCriterion() {
    drag(connectorViewList.get(0)).interact(()
            -> moveTo(connectorViewList.get(2)).moveBy(16, 13).drop());
    List<ViolationHandle> violationHandleList =
            from(lookup("#violationList").queryListView()).lookup((Node node) ->
            node instanceof ViolationHandle).<ViolationHandle>queryAll()
                    .stream().filter(e -> !((ListCell) e.getParent()).isEmpty()).toList();

    Assertions.assertEquals(1, violationHandleList.size());
    Assertions.assertEquals(VIOLATION_MESSAGE, from(violationHandleList.get(0)).lookup((Node node)
            -> node instanceof Label).<Label>queryAll().stream().toList().get(1).getText());

    drag(connectorViewList.get(5)).interact(()
            -> moveTo(connectorViewList.get(7)).moveBy(16, -13).drop());
    violationHandleList = from(lookup("#violationList").queryListView()).lookup((Node node) ->
            node instanceof ViolationHandle).<ViolationHandle>queryAll()
            .stream().filter(e -> !((ListCell) e.getParent()).isEmpty()).toList();

    Assertions.assertEquals(1, violationHandleList.size());
  }

  private void putSegmentsOnGrid() {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();
    List<SegmentBlueprint> segmentBoxListCell = from(listView)
            .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
            .<SegmentBlueprint>queryAll().stream().toList();
    grid = lookup((Node node) -> node instanceof Grid).query();

    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));
    drag(segmentBoxListCell.get(0)).interact(() -> dropTo(grid));

    segmentViewList = grid.getChildren()
            .stream().filter(e -> e instanceof SegmentView).toList();
    drag(segmentViewList.get(3)).interact(() -> dropBy(-70, 80));
    drag(segmentViewList.get(2)).interact(() -> dropBy(70, 80));
    drag(segmentViewList.get(1)).interact(() -> dropBy(-70, -80));
    drag(segmentViewList.get(0)).interact(() -> dropBy(70, -80));
  }

  private void configureCriterion() {
    clickOn("#validation");
    clickOn("#criteria");
    clickOn("#deleteAllButton");
    clickOn("#newButton");
    List<SegmentBlueprint> criteriaListCell =
            from(lookup("#criteriaList").queryListView()).lookup((Node node)
                            -> node.getParent() instanceof CriterionListCell)
                    .<SegmentBlueprint>queryAll().stream().toList();
    clickOn(criteriaListCell.get(9));
    List<ListCell> typeCell = from(lookup("#typeSelector").queryListView()).lookup((Node node)
                    -> node.getParent() instanceof ListCell)
            .<ListCell>queryAll().stream().toList();
    clickOn(typeCell.get(0));
    clickOn("#attributeSelector");
    for (int i = 0; i < 3; i++) {
      type(KeyCode.DOWN);
    }
    type(KeyCode.ENTER);
    clickOn("#validationSelector");
    for (int i = 0; i < 3; i++) {
      type(KeyCode.DOWN);
    }
    type(KeyCode.ENTER);
    lookup("#valueField").<TextField>query().setText("1");
    closeCurrentWindow();
  }
}
