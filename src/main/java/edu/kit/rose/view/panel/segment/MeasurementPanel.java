package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import edu.kit.rose.view.commons.FXMLContainer;
import edu.kit.rose.view.commons.MeasurementTableFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.List;

/**
 * A measurement panel allows the user to see and configure time-dependant measurements for a given segment.
 */
class MeasurementPanel extends FXMLContainer {
  private TabPane tabPane;
  private MeasurementController controller;
  private TimeSliceSetting timeSliceSetting;
  private Segment segment;

  /**
   * Creates an empty measurement panel.
   */
  public MeasurementPanel() {
    super("measurement_panel");
  }

  /**
   * Sets the time slice settings that the measurement should be displayed with.
   *
   * @param timeSliceSetting
   */
  public void setTimeSliceSetting(TimeSliceSetting timeSliceSetting) {
    this.timeSliceSetting = timeSliceSetting;
  }

  /**
   * Sets the segment whose values should be displayed in the measurement panel.
   *
   * @param segment
   */
  public void setSegment(Segment segment) {
    this.segment = segment;
  }

  private void setContent(Segment segment, TimeSliceSetting timeSliceSetting) {
    tabPane.getTabs().clear();
    MeasurementTableFactory factory =
        new MeasurementTableFactory(List.of(segment), timeSliceSetting);
    for (Measurement<?> m : segment.getMeasurements()) {
      tabPane.getTabs().add(new Tab(m.getMeasurementType().getName(),
          factory.forMeasurementType(m.getMeasurementType())));
    }
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  @Override
  protected List<FXMLContainer> getSubFXMLContainer() {
    return null;
  }

  /**
   * Sets the controller that handles measurement value updates.
   *
   * @param controller
   */
  public void setController(MeasurementController controller) {
    this.controller = controller;
  }
}
