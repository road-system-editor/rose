package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.view.panel.hierarchy.HierarchyPanel;
import edu.kit.rose.view.panel.problem.ProblemOverviewPanel;
import edu.kit.rose.view.panel.roadsystem.RoadSystemPanel;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxPanel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is ROSE's main window, as specified in PF11.1.1.
 * This class lays out the contained panels and sets up the menu bar, as specified in PF11.1.2.
 */
public class MainWindow extends RoseWindow {
  @Inject
  private AttributeController attributeController;
  @Inject
  private ApplicationController applicationController;
  @Inject
  private HierarchyController hierarchyController;
  @Inject
  private MeasurementController measurementController;
  @Inject
  private PlausibilityController plausibilityController;
  @Inject
  private RoadSystemController roadSystemController;

  @Inject
  private Project project;
  @Inject
  private ApplicationDataSystem applicationData;

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
    this.hierarchyPanel = new HierarchyPanel();
    hierarchyPanel.init(injector);

    var scene = new Scene(hierarchyPanel);
    stage.setScene(scene);
  }
}
