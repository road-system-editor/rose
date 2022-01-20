package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;
import edu.kit.rose.view.panel.measurement.TimeSliceSettingPanel;
import edu.kit.rose.view.panel.measurement.MeasurementOverviewPanel;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.List;

/**
 * The measurements window allows the user to look up and edit measurements of all street segments, as specified in PF11.1.4.
 * This class is responsible for laying it's contained panels and mediating between
 */
public class MeasurementsWindow extends RoseWindow { // also uses HBox and VBox
  private MeasurementController measurementController;
  private Project project;
  /**
   * The interval settings panel is contained in the measurements window.
   */
  private TimeSliceSettingPanel timeSliceSetting;
  /**
   * The measurement overview panels are contained in a tabbed layout in the measurements window.
   */
  private List<MeasurementOverviewPanel> measurementOverviews;
  /**
   * The controller to use for updating measurement values and settings.
   */
  private MeasurementController controller;
  /**
   * The tab pane holds one measurement overview panel for each tab.
   */
  @FXML
  private TabPane tabs;

  /**
   * Creates a new measurements window instance.
   *
   * @param translator
   * @param controller
   * @param project
   * @param injector
   */
  @Inject
  public MeasurementsWindow(LocalizedTextProvider translator, MeasurementController controller,
                            Project project, Injector injector) {
    super(translator, injector);
    this.measurementController = controller;
    this.project = project;
  }

  @Override
  protected void configureStage(Stage stage) {
    // fxml loading
    timeSliceSetting.setTranslator(getTranslator());
    timeSliceSetting.setController(controller);
    timeSliceSetting.setTimeSliceSetting(project.getRoadSystem().getTimeSliceSetting());

    MeasurementOverviewPanel demandPanel = new MeasurementOverviewPanel(getTranslator(), controller,
        null /* TODO project.getRoadSystem()*/, MeasurementType.DEMAND);
    measurementOverviews.add(demandPanel);
  }
}
