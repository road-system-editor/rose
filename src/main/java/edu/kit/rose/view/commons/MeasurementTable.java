package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * A measurement table displays measurements of a given measurement type, for a given set of
 * {@link Segment}s.
 *
 * @param <T> the data type of the measurement to display.
 */
public class MeasurementTable<T> extends FxmlContainer {
  @FXML
  private TableView<?> table;

  /**
   * Creates a new measurement table for a given measurement type and segment list.
   */
  public MeasurementTable(MeasurementType type, Collection<Segment> segments,
                          TimeSliceSetting timeSliceSetting) {
    super("measurement_table.fxml");
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }
}
