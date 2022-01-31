package edu.kit.rose.view.panel.criterion;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

/**
 * The criteria overview panel allows the user to view the criteria.
 */
public class CriteriaOverviewPanel extends FxmlContainer
    implements SetObserver<PlausibilityCriterion, CriteriaManager> {
  @Inject
  private PlausibilityController controller;
  @Inject
  private ApplicationDataSystem applicationDataSystem;
  private CriterionHandle selected;
  private Consumer<PlausibilityCriterion> selectionListener;

  @FXML
  private Button exportButton;
  @FXML
  private Button importButton;
  @FXML
  private Button deleteAllButton;
  @FXML
  private Button newButton;
  @FXML
  private ListView<PlausibilityCriterion> criteriaList;

  /**
   * Creates a new CriteriaOverviewPanel.
   */
  public CriteriaOverviewPanel() {
    super("CriteriaOverviewPanel.fxml");
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);

    newButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
        event -> controller.addCompatibilityCriterion());
    importButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
        event -> controller.importCompatibilityCriteria());
    exportButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
        event -> controller.exportCompatibilityCriteria());
    deleteAllButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
        event -> controller.deleteAllCompatibilityCriteria());

    initCriteriaList();

  }

  private void initCriteriaList() {
    criteriaList
        .setCellFactory(criterion -> new CriterionListCell(this.controller, selectionListener));

    for (PlausibilityCriterion criterion :
        applicationDataSystem.getCriteriaManager()
            .getCriteriaOfType(PlausibilityCriterionType.COMPATIBILITY)) {
      criteriaList.getItems().add(criterion);
    }
  }

  /**
   * Setter for the selectionListener.
   *
   * @param selectionListener is the listener that is triggered by selection.
   */
  public void setSelectionListener(Consumer<PlausibilityCriterion> selectionListener) {
    this.selectionListener = selectionListener;
  }

  @Override
  public void notifyAddition(PlausibilityCriterion unit) {
    if (unit.getType() == PlausibilityCriterionType.COMPATIBILITY) {
      criteriaList.getItems().add(unit);
    }
  }

  @Override
  public void notifyRemoval(PlausibilityCriterion unit) {
    criteriaList.getItems().removeIf(item -> item == unit);
  }

  @Override
  public void notifyChange(CriteriaManager unit) {
    initCriteriaList();
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
    newButton.setText(getTranslator()
        .getLocalizedText("view.panel.criterion.criteriaOverviewPanel.newCriteria"));
    importButton.setText(
        getTranslator().getLocalizedText("view.panel.criterion.criteriaOverviewPanel.import"));
    exportButton.setText(
        getTranslator().getLocalizedText("view.panel.criterion.criteriaOverviewPanel.export"));
    deleteAllButton.setText(getTranslator()
        .getLocalizedText("view.panel.criterion.criteriaOverviewPanel.deleteAll"));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
