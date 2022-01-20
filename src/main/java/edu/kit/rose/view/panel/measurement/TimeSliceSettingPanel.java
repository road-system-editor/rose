package edu.kit.rose.view.panel.measurement;

import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.view.commons.FXMLContainer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * The time slice setting panel allows the user to configure the measurement time slice settings, as specified in 11.1.4.
 */
public class TimeSliceSettingPanel extends FXMLContainer { // also uses HBox
  private TimeSliceSetting timeSliceSetting;
  private MeasurementController controller;

  @FXML
  private Label intervalStepsLabel;
  @FXML
  private TextField intervalStepsField;
  @FXML
  private Label intervalLengthLabel;
  @FXML
  private TextField intervalLengthField;

  /**
   * Needs {@link #setTranslator(LocalizedTextProvider)} + {@link #setController(MeasurementController)} + {@link #setTimeSliceSetting(TimeSliceSetting)}
   */
  public TimeSliceSettingPanel() {
    super("interval_settings_panel.fxml");
  }

  public void setController(MeasurementController controller) {
    this.controller = controller;
  }

  public void setTimeSliceSetting(TimeSliceSetting timeSliceSetting) {
    this.timeSliceSetting = timeSliceSetting;
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected List<FXMLContainer> getSubFXMLContainer() {
    return null;
  }
}
