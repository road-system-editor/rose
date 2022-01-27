package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.view.commons.FxmlUtility;
import edu.kit.rose.view.panel.hierarchy.HierarchyPanel;
import edu.kit.rose.view.panel.problem.ProblemOverviewPanel;
import edu.kit.rose.view.panel.roadsystem.RoadSystemPanel;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxPanel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
  public void close() {
    super.close();
    Platform.exit();
  }

  @Override
  protected void configureStage(Stage stage, Injector injector) {
    Parent tree = FxmlUtility.loadFxml(null, this, getClass().getResource("MainWindow.fxml"));
    var scene = new Scene(tree);

    stage.setScene(scene);
    stage.setWidth(1280); // TODO magic number
    stage.setHeight(720);
    stage.setTitle("ROSE");

    this.roseMenuBar.init(injector);
    this.hierarchyPanel.init(injector);
    this.roadSystemPanel.init(injector);
    this.problemOverviewPanel.init(injector);
    this.segmentBoxPanel.init(injector);
  }
}
