package edu.kit.rose.view.panel.segment;

import com.google.inject.Injector;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.UnmountUtility;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

/**
 * The segment editor panel allows the user to configure the attributes and measurements
 * of a given segment, as specified in PF11.1.5.
 */
public class SegmentEditorPanel extends FxmlContainer implements SetObserver<Element, Element> {
  private static final String ATTRIBUTE_PANEL_STYLE =
      "/edu/kit/rose/view/panel/segment/AttributePanel.css";
  private Segment segment;

  @FXML
  private Tab attributeTab;
  @FXML
  private AttributePanel attributePanel;
  @FXML
  private Tab measurementTab;
  @FXML
  private MeasurementPanel measurementPanel;

  /**
   * Creates a new segment editor panel. Make sure to call {@link #init(Injector)} afterwards!
   */
  public SegmentEditorPanel() {
    super("SegmentEditorPanel.fxml");
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);
    setupView();
    UnmountUtility.runOnUnmount(this, this::unregisterListeners);
  }

  private void setupView() {
    String attributeStyleSheetUrl =
        Objects.requireNonNull(getClass().getResource(ATTRIBUTE_PANEL_STYLE)).toExternalForm();
    this.getStylesheets().add(attributeStyleSheetUrl);
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {
    attributeTab.setText(
        getTranslator().getLocalizedText("view.panel.segment.attributeEditor"));
    measurementTab.setText(
        getTranslator().getLocalizedText("view.panel.segment.measurementEditor"));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return List.of(attributePanel, measurementPanel);
  }

  @Override
  public void notifyChange(Element unit) {

  }

  /**
   * Sets the segment that should be editable in this editor.
   *
   * @param segment the new segment to edit.
   */
  public void setSegment(Segment segment) {
    if (this.segment != null) {
      segment.removeSubscriber(this);
    }

    this.segment = segment;
    attributePanel.setAttributes(segment.getAttributeAccessors());

    if (this.segment != null) {
      segment.addSubscriber(this);
    }
  }

  private void unregisterListeners() {
    if (this.segment != null) {
      segment.removeSubscriber(this);
    }
  }

  @Override
  public void notifyAddition(Element unit) {

  }

  @Override
  public void notifyRemoval(Element unit) {

  }
}
