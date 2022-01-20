package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FXMLContainer;
import edu.kit.rose.view.commons.UnmountUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

import java.util.List;

/**
 * The segment editor panel allows the user to configure the attributes and measurements of a given segment, as specified in PF11.1.5.
 */
public class SegmentEditorPanel extends FXMLContainer implements UnitObserver<Element> {
  private final AttributeController attributeController;
  private final MeasurementController measurementController;

  private final Segment segment;
  @FXML
  private TabPane tabPane;
  @FXML
  private AttributePanel attributePanel;
  @FXML
  private MeasurementPanel measurementPanel;

  /**
   * Creates a new segment editor panel for a given segment.
   */
  public SegmentEditorPanel(Segment segment, TimeSliceSetting timeSliceSetting,
                            AttributeController attributeController,
                            MeasurementController measurementController) {
    super("segment_editor_panel.fxml");
    this.segment = segment;
    this.attributeController = attributeController;
    this.measurementController = measurementController;
    UnmountUtility.subscribeUntilUnmount(this, this, segment);
    attributePanel.setAttributes(segment.getAttributeAccessors());
    attributePanel.setController(attributeController);
    measurementPanel.setSegment(segment);
    measurementPanel.setTimeSliceSetting(timeSliceSetting);
    measurementPanel.setController(measurementController);
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  @Override
  protected List<FXMLContainer> getSubFXMLContainer() {
    return null;
  }

  @Override
  public void notifyChange(Element unit) {

  }
}
