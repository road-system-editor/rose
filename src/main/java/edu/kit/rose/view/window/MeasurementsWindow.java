package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import edu.kit.rose.view.commons.FxmlUtility;
import edu.kit.rose.view.panel.measurement.MeasurementOverviewPanel;
import edu.kit.rose.view.panel.measurement.TimeSliceSettingPanel;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * The measurements window allows the user to look up and edit measurements of all street
 * segments, as specified in PF11.1.4.
 * This class is responsible for laying it's contained panels and mediating between
 */
public class MeasurementsWindow extends RoseWindow {
  @Inject
  private Project project;

  /**
   * The interval settings panel is contained in the measurements window.
   */
  @FXML
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
   * @param injector the injector for dependency injection
   */
  @Inject
  public MeasurementsWindow(Injector injector) {
    super(injector);
  }

  @Override
  protected void configureStage(Stage stage, Injector injector) {
    Parent tree = FxmlUtility.loadFxml(null, this,
        getClass().getResource("MeasurementsWindow.fxml"));
    var scene = new Scene(tree);

    stage.setScene(scene);
    stage.setWidth(640); // TODO magic numbers
    stage.setHeight(360);

    getTranslator().subscribeToOnLanguageChanged(lang -> updateTranslatableStrings(stage));
    updateTranslatableStrings(stage);

    timeSliceSetting.init(injector);

    // TODO add tabs with measurement tables
    for (var type : MeasurementType.values()) {
      String title = EnumLocalizationUtility.localizeMeasurementTypeTitle(getTranslator(), type);
      Node content = new Button(String.format("table for %s goes here!", title));
      tabs.getTabs().add(new Tab(title, content));
    }
  }

  private void updateTranslatableStrings(Stage stage) {
    stage.setTitle(getTranslator().getLocalizedText("view.window.measurements.title"));
    // TODO translate tab titles too
  }
}
