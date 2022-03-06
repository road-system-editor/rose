package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.ConnectorView;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.roadsystem.Grid;
import edu.kit.rose.view.panel.segmentbox.SegmentBlueprint;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxListCell;
import edu.kit.rose.view.panel.violation.ViolationHandle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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
  private static final String CRITERION_NAME = "testCriterion";
  private static final String FILE_NAME = "tesCriteriaConfiguration";
  private static final String VIOLATION_MESSAGE = "B1ASE and B0ASE are incompatible";
  private Grid grid;
  private List<Node> segmentViewList;

  @BeforeEach
  void setUp() {

  }

  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testImportExportCriterion() {
    configureLessThan();
    clickOn("#newButton").clickOn("#newButton")
            .clickOn("#newButton").clickOn("#newButton");
    List<Node> criteriaListCell = getCriteriaListCell();
    enterNameOfCriterion(criteriaListCell, 0);
    enterNameOfCriterion(criteriaListCell, 1);
    enterNameOfCriterion(criteriaListCell, 2);
    enterNameOfCriterion(criteriaListCell, 3);
    clickOn("#exportButton");
    selectTestConfiguration();
    push(KeyCode.LEFT);
    push(KeyCode.ENTER);
    clickOn("#deleteAllButton");
    clickOn("#importButton");
    selectTestConfiguration();
    criteriaListCell = getCriteriaListCell();
    Assertions.assertEquals(5, criteriaListCell.size());
    clickOn(criteriaListCell.get(0));
    Assertions.assertEquals(CRITERION_NAME + 4, lookup("#nameField").<TextField>query().getText());
    clickOn(criteriaListCell.get(1));
    Assertions.assertEquals(CRITERION_NAME + 3, lookup("#nameField").<TextField>query().getText());
    clickOn(criteriaListCell.get(2));
    Assertions.assertEquals(CRITERION_NAME + 2, lookup("#nameField").<TextField>query().getText());
    clickOn(criteriaListCell.get(3));
    Assertions.assertEquals(CRITERION_NAME + 1, lookup("#nameField").<TextField>query().getText());
    clickOn(criteriaListCell.get(4));
    Assertions.assertEquals(CRITERION_NAME + 0, lookup("#nameField").<TextField>query().getText());
  }

  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testValidateCompatibilityCriterion() {
    putSegmentsOnGrid();
    List<Node> connectorViewList = lookup((Node node) ->
            node instanceof ConnectorView).queryAll().stream().toList();
    configureCriterion();
    configureAttributes();
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

  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testEditCriteria() {
    putSegmentsOnGrid();
    configureAttributes();
    List<Node> connectorViewList = lookup((Node node) ->
            node instanceof ConnectorView).queryAll().stream().toList();
    drag(connectorViewList.get(0)).interact(()
            -> moveTo(connectorViewList.get(2)).moveBy(16, 13).drop());
    configureCriterion();
    List<ViolationHandle> violationHandleList =
            from(lookup("#violationList").queryListView()).lookup((Node node) ->
                            node instanceof ViolationHandle).<ViolationHandle>queryAll()
                    .stream().filter(e -> !((ListCell) e.getParent()).isEmpty()).toList();

    Assertions.assertEquals(1, violationHandleList.size());
    Assertions.assertEquals(VIOLATION_MESSAGE, from(violationHandleList.get(0)).lookup((Node node)
            -> node instanceof Label).<Label>queryAll().stream().toList().get(1).getText());

    clickOn("#validation");
    clickOn("#criteria");
    clickOn("#deleteAllButton");

    violationHandleList =
            from(lookup("#violationList").queryListView()).lookup((Node node) ->
                            node instanceof ViolationHandle).<ViolationHandle>queryAll()
                    .stream().filter(e -> !((ListCell) e.getParent()).isEmpty()).toList();
    Assertions.assertEquals(0, violationHandleList.size());
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
    configureLessThan();
    closeCurrentWindow();
  }

  private void configureLessThan() {
    clickOn("#validation");
    clickOn("#criteria");
    clickOn("#deleteAllButton");
    clickOn("#newButton");
    List<Node> criteriaListCell =
            from(lookup("#criteriaList").queryListView()).lookup((Node node)
                            -> node.getParent() instanceof CriterionListCell)
                    .queryAll().stream()
                    .filter(e -> !((ListCell) e.getParent()).isEmpty()).toList();
    clickOn(criteriaListCell.get(0));
    lookup("#nameField").<TextField>query().setText(CRITERION_NAME + 4);
    List<Node> typeCell = from(lookup("#typeSelector").queryListView()).lookup((Node node)
                    -> node.getParent() instanceof ListCell)
            .queryAll().stream().toList();
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
  }


  private void configureAttributes() {
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
  }

  private void selectTestConfiguration() {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    StringSelection stringSelection = new StringSelection(FILE_NAME);
    clipboard.setContents(stringSelection, stringSelection);
    press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
    push(KeyCode.ENTER);
  }

  private List<Node> getCriteriaListCell() {
    return from(lookup("#criteriaList").queryListView()).lookup((Node node)
                            -> node.getParent() instanceof CriterionListCell)
                    .queryAll().stream()
                    .filter(e -> !((ListCell) e.getParent()).isEmpty()).toList();
  }

  private void enterNameOfCriterion(List<Node> criteriaListCell, int id) {
    clickOn(criteriaListCell.get(id));
    lookup("#nameField").<TextField>query().setText(CRITERION_NAME + id);
  }
}
