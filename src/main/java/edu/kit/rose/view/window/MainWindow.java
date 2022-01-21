package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.navigation.WindowType;
import edu.kit.rose.controller.project.ProjectController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.ExportFormat;
import edu.kit.rose.view.commons.FxmlUtility;
import edu.kit.rose.view.panel.hierarchy.HierarchyPanel;
import edu.kit.rose.view.panel.problem.ProblemOverviewPanel;
import edu.kit.rose.view.panel.roadsystem.RoadSystemPanel;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxPanel;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private RoseMenuBar roseMenuBar;
  @FXML
  private HierarchyPanel hierarchyPanel;
  @FXML
  private RoadSystemPanel roadSystemPanel;
  @FXML
  private ProblemOverviewPanel problemOverviewPanel;
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
  protected void configureStage(Stage stage, Injector injector) {
    Parent tree = FxmlUtility.loadFxml(null, this, getClass().getResource("MainWindow.fxml"));
    var scene = new Scene(tree);

    stage.setScene(scene);
    stage.setWidth(1280);
    stage.setHeight(720);
    stage.setTitle("ROSE");

    this.roseMenuBar.init(injector);
    this.hierarchyPanel.init(injector);
    this.roadSystemPanel.init(injector);
    this.problemOverviewPanel.init(injector);
    this.segmentBoxPanel.init(injector);
  }
}
