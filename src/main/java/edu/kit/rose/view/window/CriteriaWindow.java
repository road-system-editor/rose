package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.view.commons.FxmlUtility;
import edu.kit.rose.view.panel.criterion.CriteriaOverviewPanel;
import edu.kit.rose.view.panel.criterion.CriterionPanel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/**
 * The criterion window allows the user to manage plausibility criteria, as specified in PF11.1.3.
 * This class is responsible for laying it's contained panels and acts as a mediator between the
 * selection of a criterion in the overview panel and the editor panel.
 */
public class CriteriaWindow extends RoseWindow {
  private static final int DEFAULT_WIDTH = 640;
  private static final int DEFAULT_HEIGHT = 360;

  @Inject
  private PlausibilityController plausibilityController;
  @Inject
  private ApplicationDataSystem applicationData;
  @Inject
  private Injector injector;

  /**
   * The criterion overview panel is contained in the criteria window.
   */
  @FXML
  private CriteriaOverviewPanel overview;
  @FXML
  private ScrollPane criterionContainer;

  /**
   * Creates a new criterion window instance.
   *
   * @param injector the dependency injector.
   */
  @Inject
  public CriteriaWindow(Injector injector) {
    super(injector);
  }

  @Override
  protected void configureStage(Stage stage, Injector injector) {
    Parent tree = FxmlUtility.loadFxml(null, this, getClass().getResource("CriteriaWindow.fxml"));
    var scene = new Scene(tree);

    stage.setScene(scene);
    stage.setWidth(DEFAULT_WIDTH);
    stage.setHeight(DEFAULT_HEIGHT);

    getTranslator().subscribeToOnLanguageChanged(lang -> updateTranslatableStrings(stage));
    updateTranslatableStrings(stage);

    overview.init(injector);
    overview.setSelectionListener(this::onSelect);
  }

  private void updateTranslatableStrings(Stage stage) {
    stage.setTitle(getTranslator().getLocalizedText("view.window.criteria.title"));
  }

  private void onSelect(PlausibilityCriterion plausibilityCriterion) {
    System.out.println(plausibilityCriterion);
    if (plausibilityCriterion == null) {
      this.criterionContainer.setContent(null);
    } else {
      this.criterionContainer.setContent(
          CriterionPanel.forCriterion(this.injector, plausibilityCriterion));
    }
  }
}
