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
import javafx.stage.Stage;

/**
 * This is ROSE's main window, as specified in PF11.1.1.
 * This class lays out the contained panels and sets up the menu bar, as specified in PF11.1.2.
 */
public class MainWindow extends RoseWindow {
  private final AttributeController attributeController;
  private final ApplicationController applicationController;
  private final HierarchyController hierarchyController;
  private final MeasurementController measurementController;
  private final PlausibilityController plausibilityController;
  private final RoadSystemController roadSystemController;

  private final Project project;
  private final ApplicationDataSystem applicationData;

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
   * @param translator the data source for translated strings.
   * @param project the project to display.
   * @param applicationData the application metadata to use for displaying the project.
   * @param stage the primary stage of the JavaFX application.
   */
  @Inject
  public MainWindow(LocalizedTextProvider translator,
                    ApplicationController applicationController,
                    AttributeController attributeController,
                    HierarchyController hierarchyController,
                    MeasurementController measurementController,
                    PlausibilityController plausibilityController,
                    RoadSystemController roadSystemController,
                    Project project,
                    ApplicationDataSystem applicationData,
                    Stage stage,
                    Injector injector) {
    super(translator, stage, injector);

    this.applicationController = applicationController;
    this.attributeController = attributeController;
    this.hierarchyController = hierarchyController;
    this.measurementController = measurementController;
    this.plausibilityController = plausibilityController;
    this.roadSystemController = roadSystemController;

    this.project = project;
    this.applicationData = applicationData;
  }

  @Override
  protected void configureStage(Stage stage) {
    hierarchyPanel.setController(hierarchyController);
    hierarchyPanel.setRoadSystem(project.getRoadSystem());

    roadSystemPanel.setProject(project);
    roadSystemPanel.setApplicationController(applicationController);
    roadSystemPanel.setRoadSystemController(roadSystemController);
    roadSystemPanel.setAttributeController(attributeController);
    roadSystemPanel.setMeasurementController(measurementController);

    problemOverviewPanel.setController(plausibilityController);
    problemOverviewPanel.setManager(project.getPlausibilitySystem().getViolationManager());

    segmentBoxPanel.setController(roadSystemController);
  }
}
