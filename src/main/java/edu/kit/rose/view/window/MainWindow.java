package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.view.commons.FxmlUtility;
import edu.kit.rose.view.panel.hierarchy.HierarchyPanel;
import edu.kit.rose.view.panel.problem.ProblemOverviewPanel;
import edu.kit.rose.view.panel.roadsystem.RoadSystemPanel;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxPanel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * This is ROSE's main window, as specified in PF11.1.1.
 * This class lays out the contained panels and sets up the menu bar, as specified in PF11.1.2.
 */
public class MainWindow extends RoseWindow {
  @FXML
  private Menu project;
  @FXML
  private MenuItem newProject;
  @FXML
  private MenuItem loadProject;
  @FXML
  private MenuItem exportProject;
  @FXML
  private MenuItem saveProject;
  @FXML
  private MenuItem saveProjectAs;
  @FXML
  private MenuItem loadProjectBackup;
  @FXML
  private Menu validation;
  @FXML
  private Menu measurements;
  @FXML
  private Menu language;
  @FXML
  private MenuItem germanLanguage;
  @FXML
  private MenuItem englishLanguage;
  @FXML
  private Menu help;
  @FXML
  private MenuItem shortcuts;
  @FXML
  private MenuItem tutorial;
  @FXML
  private HierarchyPanel hierarchyPanel;
  @FXML
  private RoadSystemPanel roadSystemPanel;
  @FXML
  private ProblemOverviewPanel problemOverviewPanel;
  @FXML
  private SegmentBoxPanel segmentBoxPanel;

  @Inject
  private ApplicationController controller;

  /**
   * Creates a new main window instance.
   *
   * @param stage the primary stage of the JavaFX application.
   * @param injector the dependency injector.
   */
  @Inject
  public MainWindow(Stage stage,
                    Injector injector) {
    super(stage, injector);
  }

  @Override
  protected void configureStage(Stage stage, Injector injector) {
    Parent tree = FxmlUtility.loadFxml(null, this, getClass().getResource("MainWindow.fxml"));
    var scene = new Scene(tree);

    stage.setScene(scene);
    stage.setWidth(1280);
    stage.setHeight(720);
    stage.setTitle("ROSE");

    this.hierarchyPanel.init(injector);
    this.roadSystemPanel.init(injector);
    this.problemOverviewPanel.init(injector);
    this.segmentBoxPanel.init(injector);

    getTranslator().subscribeToOnLanguageChanged(this::updateTranslatableStrings);
    updateTranslatableStrings(getTranslator().getSelectedLanguage());

    germanLanguage.setOnAction(evt -> controller.setLanguage(Language.GERMAN));
    englishLanguage.setOnAction(evt -> controller.setLanguage(Language.ENGLISH));
  }

  private void updateTranslatableStrings(Language newLang) {
    project.setText(getTranslator().getLocalizedText("view.window.menu.project"));
    newProject.setText(getTranslator().getLocalizedText("view.window.menu.newProject"));
    loadProject.setText(getTranslator().getLocalizedText("view.window.menu.loadProject"));
    exportProject.setText(getTranslator().getLocalizedText("view.window.menu.exportProject"));
    saveProject.setText(getTranslator().getLocalizedText("view.window.menu.saveProject"));
    saveProjectAs.setText(getTranslator().getLocalizedText("view.window.menu.saveProjectAs"));
    loadProjectBackup.setText(getTranslator().getLocalizedText(
        "view.window.menu.loadProjectBackup"));

    validation.setText(getTranslator().getLocalizedText("view.window.menu.validation"));
    measurements.setText(getTranslator().getLocalizedText("view.window.menu.measurements"));
    language.setText(getTranslator().getLocalizedText("view.window.menu.language"));
    germanLanguage.setText(getTranslator().getLocalizedText("view.window.menu.germanLanguage"));
    englishLanguage.setText(getTranslator().getLocalizedText("view.window.menu.englishLanguage"));
    help.setText(getTranslator().getLocalizedText("view.window.menu.help"));
    shortcuts.setText(getTranslator().getLocalizedText("view.window.menu.shortcuts"));
    tutorial.setText(getTranslator().getLocalizedText("view.window.menu.tutorial"));
  }
}
