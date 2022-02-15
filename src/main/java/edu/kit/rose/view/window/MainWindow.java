package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.project.ProjectController;
import edu.kit.rose.model.ProjectFormat;
import edu.kit.rose.view.commons.FxmlUtility;
import edu.kit.rose.view.panel.hierarchy.HierarchyPanel;
import edu.kit.rose.view.panel.roadsystem.RoadSystemPanel;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxPanel;
import edu.kit.rose.view.panel.violation.ViolationOverviewPanel;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * This is ROSE's main window, as specified in PF11.1.1.
 * This class lays out the contained panels and sets up the menu bar, as specified in PF11.1.2.
 */
public class MainWindow extends RoseWindow {
  @Inject
  private ProjectController projectController;

  @FXML
  private RoseMenuBar roseMenuBar;
  @FXML
  private HierarchyPanel hierarchyPanel;
  @FXML
  private RoadSystemPanel roadSystemPanel;
  @FXML
  private ViolationOverviewPanel violationOverviewPanel;
  @FXML
  private SegmentBoxPanel segmentBoxPanel;

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
  public void close() {
    super.close();
    this.projectController.shutDown();
    Platform.exit();
  }

  @Override
  protected void configureStage(Stage stage, Injector injector) {
    Parent tree = FxmlUtility.loadFxml(null, this, getClass().getResource("MainWindow.fxml"));
    var scene = new Scene(tree);

    registerShortcuts(scene);

    stage.setScene(scene);
    stage.setWidth(1280); // TODO magic number
    stage.setHeight(720);
    stage.setTitle("ROSE");

    this.roseMenuBar.init(injector);
    this.hierarchyPanel.init(injector);
    this.roadSystemPanel.init(injector);
    this.violationOverviewPanel.init(injector);
    this.segmentBoxPanel.init(injector);
  }

  private void registerShortcuts(Scene scene) {
    Map<KeyCombination, Runnable> shortCutBindings = new HashMap<>();
    shortCutBindings.put(
        new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
        () -> this.projectController.save());
    shortCutBindings.put(
        new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
        () -> this.projectController.saveAs());
    shortCutBindings.put(
        new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN),
        () -> this.projectController.loadProject());
    shortCutBindings.put(
        new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN),
        () -> this.projectController.export(ProjectFormat.YAML));
    shortCutBindings.put(
        new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN),
        () -> this.projectController.createNewProject());

    scene.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
      for (var binding : shortCutBindings.entrySet()) {
        if (binding.getKey().match(evt)) {
          binding.getValue().run();
          evt.consume();
        }
      }
    });
  }
}
