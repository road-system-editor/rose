package edu.kit.rose.view.panel.measurement;

import com.google.inject.Inject;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
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
  @Inject
  private Project project;
  @Inject
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
   * Creates a new time slice settings panel.
   */
  public TimeSliceSettingPanel() {
    super("TimeSliceSettingsPanel.fxml");
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
