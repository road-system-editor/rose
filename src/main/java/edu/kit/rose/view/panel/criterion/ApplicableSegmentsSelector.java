package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.FxmlContainer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * The applicable segments' selector allows the user to select which segment types a plausibility
 * criterion applies to.
 */
class ApplicableSegmentsSelector extends FxmlContainer {
  @FXML
  private ListView<SelectableSegmentType> typeSelector;
  @FXML
  private CheckBox selectAllCheckBox;

  /**
   * Creates a new ApplicableSegmentSelector.
   *
   * @param translator the localizedTextProvider.
   */
  public ApplicableSegmentsSelector(LocalizedTextProvider translator) {
    super("applicable_segments_selector.fxml"); //? -philipp 19.01
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected List<FXMLContainer> getSubFXMLContainer() {
    return null;
  }

  private static class SelectableSegmentType {
    private final SegmentType type;
    private final BooleanProperty selected = new SimpleBooleanProperty(true);

    public SelectableSegmentType(SegmentType type) {
      this.type = type;
    }

    public BooleanProperty getSelectedProperty() {
      return selected;
    }
  }
}
