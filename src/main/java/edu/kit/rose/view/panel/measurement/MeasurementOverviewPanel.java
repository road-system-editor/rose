package edu.kit.rose.view.panel.measurement;

import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;
import edu.kit.rose.view.commons.FxmlContainer;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * The measurement overview panel provides an editable overview over all measurement
 * values of a given type for all segments of the road system.
 */
public class MeasurementOverviewPanel extends FxmlContainer {
  private MeasurementController controller;
  private RoadSystem roadSystem;
  private MeasurementType type;

  @FXML
  private TableView table;

  /**
   * Creates a new measurement overview panel for the given measurement type.
   *
   * @param translator the data source for localized strings.
   * @param type the type of measurement to show.
   */
  public MeasurementOverviewPanel(LocalizedTextProvider translator,
                                  MeasurementController controller, RoadSystem roadSystem,
                                  MeasurementType type) {
    super("measurement_overview_panel.fxml");
    this.controller = controller;
    this.roadSystem = roadSystem;
    this.type = type;
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected List<FXMLContainer> getSubFXMLContainer() {
    return null;
  }
}
