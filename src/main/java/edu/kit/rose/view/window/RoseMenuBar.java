package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.navigation.WindowType;
import edu.kit.rose.controller.project.ProjectController;
import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.ProjectFormat;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.view.commons.FxmlContainer;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * Represents the {@link javafx.scene.control.MenuBar} that
 * is displayed in {@link MainWindow}.
 */
public class RoseMenuBar extends FxmlContainer implements DualSetObserver<AttributeType, Path,
    ApplicationDataSystem> {
  @FXML
  private Menu project;
  @FXML
  private MenuItem newProject;
  @FXML
  private MenuItem loadProject;
  @FXML
  private Menu exportProject;
  @FXML
  private MenuItem saveProject;
  @FXML
  private MenuItem saveProjectAs;
  @FXML
  private Menu loadProjectBackup;
  @FXML
  private Menu validation;
  @FXML
  private MenuItem criteria;
  @FXML
  private Menu measurements;
  @FXML
  private MenuItem measurementsOverview;
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

  @Inject
  private ApplicationController applicationController;
  @Inject
  private ProjectController projectController;
  @Inject
  private Navigator navigator;
  @Inject
  private ApplicationDataSystem applicationData;

  private Map<Path, MenuItem> recentProjectMenuItems;



  /**
   * Creates a new {@link RoseMenuBar}.
   *
   */
  public RoseMenuBar() {
    super("RoseMenuBar.fxml");
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

    project.setText(getTranslator().getLocalizedText("view.window.menu.project"));
    newProject.setText(getTranslator().getLocalizedText("view.window.menu.newProject"));
    loadProject.setText(getTranslator().getLocalizedText("view.window.menu.loadProject"));
    exportProject.setText(getTranslator().getLocalizedText("view.window.menu.exportProject"));
    saveProject.setText(getTranslator().getLocalizedText("view.window.menu.saveProject"));
    saveProjectAs.setText(getTranslator().getLocalizedText("view.window.menu.saveProjectAs"));
    loadProjectBackup.setText(getTranslator().getLocalizedText(
        "view.window.menu.loadProjectBackup"));

    // Validation
    validation.setText(getTranslator().getLocalizedText("view.window.menu.validation"));
    criteria.setText(getTranslator().getLocalizedText("view.window.menu.criteria"));

    // Measurements
    measurements.setText(getTranslator().getLocalizedText("view.window.menu.measurements"));
    measurementsOverview.setText(getTranslator().getLocalizedText(
        "view.window.menu.measurementsOverview"));

    // Language
    language.setText(getTranslator().getLocalizedText("view.window.menu.language"));
    germanLanguage.setText(getTranslator().getLocalizedText("view.window.menu.germanLanguage"));
    englishLanguage.setText(getTranslator().getLocalizedText("view.window.menu.englishLanguage"));

    // Help
    help.setText(getTranslator().getLocalizedText("view.window.menu.help"));
    shortcuts.setText(getTranslator().getLocalizedText("view.window.menu.shortcuts"));
    tutorial.setText(getTranslator().getLocalizedText("view.window.menu.tutorial"));
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);

    populateExportMenu();
    populateBackupMenu();
    populateRecentProjectsMenu();
    registerMenuListeners();

    this.applicationData.addSubscriber(this);
  }

  private void populateExportMenu() {
    for (var format : ProjectFormat.values()) {
      var item = new MenuItem(format.toString());
      item.setOnAction(evt -> this.projectController.export(format));
      this.exportProject.getItems().add(item);
    }
  }

  private void populateBackupMenu() {
    var backups = this.projectController.getBackupPaths();

    loadProjectBackup.getItems().clear();
    for (var backup : backups) {
      var item = new MenuItem(backup.getFileName().toString());
      item.setOnAction(evt -> this.projectController.loadBackup(backup));
      loadProjectBackup.getItems().add(item);
    }
  }

  private void populateRecentProjectsMenu() {
    if (recentProjectMenuItems == null) {
      recentProjectMenuItems = new HashMap<>();
    }

    recentProjectMenuItems.clear();

    // insert new items
    var recentProjects = this.applicationData.getRecentProjectPaths();
    for (var recentProject : recentProjects) {
      addMenuItemForRecentProject(recentProject);
    }
  }

  private void addMenuItemForRecentProject(Path recentProject) {
    var item = new MenuItem(recentProject.getFileName().toString());
    recentProjectMenuItems.put(recentProject, item);
    item.setOnAction(evt -> this.projectController.loadRecentProject(recentProject));
    project.getItems().add(item);
  }

  private void removeMenuItemForRecentProject(Path recentProject) {
    var item = recentProjectMenuItems.get(recentProject);
    project.getItems().remove(item);
    recentProjectMenuItems.remove(recentProject);
  }

  /**
   * Sets up listeners for all menu items from the fxml file.
   *
   * @implNote To detect clicks on menu (not menuitem) objects, a child nee
   */
  private void registerMenuListeners() {
    // Project
    newProject.setOnAction(evt -> this.projectController.createNewProject());
    loadProject.setOnAction(evt -> this.projectController.loadProject());
    saveProject.setOnAction(evt -> projectController.save());
    saveProjectAs.setOnAction(evt -> projectController.saveAs());
    // Validation
    // TODO remove sub-menu and detect clicks on top-level menu
    criteria.setOnAction(evt -> navigator.showWindow(WindowType.CRITERION));

    // Measurements
    // TODO remove sub-menu and detect clicks on top-level menu
    measurementsOverview.setOnAction(evt -> navigator.showWindow(WindowType.MEASUREMENT_OVERVIEW));

    // Language
    germanLanguage.setOnAction(evt -> applicationController.setLanguage(Language.GERMAN));
    englishLanguage.setOnAction(evt -> applicationController.setLanguage(Language.ENGLISH));

    // Help
    shortcuts.setOnAction(evt -> navigator.showWindow(WindowType.HELP));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }

  @Override
  public void notifyAdditionSecond(Path unit) {
    addMenuItemForRecentProject(unit);
  }

  @Override
  public void notifyRemovalSecond(Path unit) {
    removeMenuItemForRecentProject(unit);
  }

  @Override
  public void notifyAddition(AttributeType unit) {
    // irrelevant for the menu bar
  }

  @Override
  public void notifyRemoval(AttributeType unit) {
    // irrelevant for the menu bar
  }

  @Override
  public void notifyChange(ApplicationDataSystem unit) {
    // irrelevant for the menu bar
  }
}
