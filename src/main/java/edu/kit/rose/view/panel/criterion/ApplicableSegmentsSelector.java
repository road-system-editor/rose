package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.FXMLContainer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

/**
 * The applicable segments selector allows the user to select which segment types a plausibility criterion applies to.
 */
class ApplicableSegmentsSelector extends FXMLContainer {
  @FXML
  private ListView<SelectableSegmentType> typeSelector;
  @FXML
  private CheckBox selectAllCheckBox;

  /**
   * Requires {@link #setTranslator(LocalizedTextProvider)}
   */
  public ApplicableSegmentsSelector() {
    super("applicable_segments_selector.fxml");
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  private static class SelectableSegmentType {
    private SegmentType type;
    private BooleanProperty selected = new SimpleBooleanProperty(true);

    /**
     * @param type
     */
    public SelectableSegmentType(SegmentType type) {
      this.type = type;
    }

    public BooleanProperty getSelectedProperty() {
      return selected;
    }
  }
}
