package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.view.commons.FxmlUtility;
import edu.kit.rose.view.panel.hierarchy.HierarchyPanel;
import edu.kit.rose.view.panel.problem.ProblemOverviewPanel;
import edu.kit.rose.view.panel.roadsystem.RoadSystemPanel;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxPanel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This is ROSE's main window, as specified in PF11.1.1.
 * This class lays out the contained panels and sets up the menu bar, as specified in PF11.1.2.
 */
public class MainWindow extends RoseWindow {
  /**
   * The hierarchy overview panel is contained in the main window.
   */
  @FXML
  private HierarchyPanel hierarchyPanel;
  /**
   * The road system editor panel is contained in the main window.
   */
  @FXML
  private RoadSystemPanel roadSystemPanel;
  /**
   * The problem overview panel is contained in the main window.
   */
  @FXML
  private ProblemOverviewPanel problemOverviewPanel;
  /**
   * The segment box panel is contained in the main window.
   */
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

    this.hierarchyPanel.init(injector);
    this.roadSystemPanel.init(injector);
    this.problemOverviewPanel.init(injector);
    this.segmentBoxPanel.init(injector);
  }
}
