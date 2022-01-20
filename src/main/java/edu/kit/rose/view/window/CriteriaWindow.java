package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.view.panel.criterion.CriteriaOverviewPanel;
import edu.kit.rose.view.panel.criterion.CriterionPanel;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * The criterion window allows the user to manage plausibility criteria, as specified in PF11.1.3.
 * This class is responsible for laying it's contained panels and acts as a mediator between the
 * selection of a criterion in the overview panel and the editor panel.
 */
public class CriteriaWindow extends RoseWindow {
  private final PlausibilityController plausibilityController;
  private final ApplicationDataSystem applicationData;

  /**
   * The criterion overview panel is contained in the criteria window.
   */
  @FXML
  private CriteriaOverviewPanel overview;
  /**
   * The criterion editor panel is contained in the criteria window.
   * The criterion shown in this panel must match the criterion selected in the {@link #overview}
   * panel.
   */
  @FXML
  private CriterionPanel<? extends PlausibilityCriterion> criterion;

  /**
   * Creates a new criterion window instance.
   *
   * @param translator the data source for translated strings.
   * @param controller the controller to handle criterion changes.
   * @param applicationData the application data system that stores the criteria.
   */
  @Inject
  public CriteriaWindow(LocalizedTextProvider translator, PlausibilityController controller,
                        ApplicationDataSystem applicationData, Injector injector) {
    super(translator, injector);
    this.plausibilityController = controller;
    this.applicationData = applicationData;
  }

  @Override
  protected void configureStage(Stage stage) {
    // fxml loading
    overview.setController(plausibilityController);
    overview.setManager(applicationData.getCriteriaManager());
    overview.setSelectionListener(this::onSelect);
  }

  private void onSelect(PlausibilityCriterion plausibilityCriterion) {
    // implementation detail
    criterion = CriterionPanel.forCriterion(getTranslator(), this.plausibilityController,
        plausibilityCriterion);
  }
}
