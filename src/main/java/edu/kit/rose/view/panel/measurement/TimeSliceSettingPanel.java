package edu.kit.rose.view.panel.measurement;

import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * The time slice setting panel allows the user to configure the measurement time slice
 * settings, as specified in PF11.1.4.
 */
public class TimeSliceSettingPanel extends FxmlContainer {
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
   * Needs {@link #setTranslator(LocalizedTextProvider)} +
   * {@link #setController(MeasurementController)} + {@link #setTimeSliceSetting(TimeSliceSetting)}.
   */
  public TimeSliceSettingPanel() {
    super("interval_settings_panel.fxml");
  }

  /**
   * Sets the controller that handles measurement value updates.
   */
  public void setController(MeasurementController controller) {
    this.controller = controller;
  }

  /**
   * Sets the time slice settings instance that is editable through this panel.
   */
  public void setTimeSliceSetting(TimeSliceSetting timeSliceSetting) {
    this.timeSliceSetting = timeSliceSetting;
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
