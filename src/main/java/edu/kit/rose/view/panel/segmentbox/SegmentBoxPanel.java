package edu.kit.rose.view.panel.segmentbox;

import java.util.List;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.view.commons.FXMLContainer;

/**
 * The segment box panel provides an overview over the available street segment types which can be created.
 * This realizes specification PF11.1.7.
 */
public class SegmentBoxPanel extends
    FXMLContainer { // also uses javafx.scene.control.ScrollPane, javafx.scene.layout.GridPane
  /**
   * The controller to use for segment creation.
   */
  private RoadSystemController controller;
  /**
   * A scrollable list of segment blueprints is contained within the panel.
   */
  private List<SegmentBlueprint> blueprints;

  /**
   * Creates a new segment box panel.
   * Requires {@link #setTranslator(LocalizedTextProvider)} + {@link #setController(RoadSystemController)}
   */
  public SegmentBoxPanel() {
    super("segment_box_panel.fxml");
  }

  /**
   * Sets the controller of this panel.
   *
   * @param controller
   */
  public void setController(RoadSystemController controller) {
    this.controller = controller;
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected List<FXMLContainer> getSubFXMLContainer() {
    return null;
  }
}
