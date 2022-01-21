package edu.kit.rose.view.panel.segmentbox;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.List;

/**
 * The segment box panel provides an overview over the available street segment types which can
 * be created, as specified in PF11.1.7.
 */
public class SegmentBoxPanel extends FxmlContainer {
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
   * Requires {@link #setTranslator(LocalizedTextProvider)} +
   * {@link #setController(RoadSystemController)}
   */
  public SegmentBoxPanel() {
    super("SegmentBoxPanel.fxml");
  }

  /**
   * Sets the controller of this panel.
   *
   * @param controller the controller to use to create segments.
   */
  public void setController(RoadSystemController controller) {
    this.controller = controller;
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
