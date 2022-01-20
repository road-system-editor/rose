package edu.kit.rose.view.panel.roadsystem;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.view.commons.FxmlContainer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * The navigator view is a set of buttons which can be used to navigate on the RoadSystemPanel.
 */
public class NavigatorView extends FxmlContainer {

  private RoadSystemController roadSystemController;

  @FXML
  private Button zoomInButton;

  /**
   * Creates a new NavigatorView.
   */
  public NavigatorView() {
    super("fxmlResourceName");
  }

  /**
   * Sets the roadSystemController controller.
   *
   * @param roadSystemController roadSystemController instance
   */
  public void setRoadSystemController(RoadSystemController roadSystemController) {
    this.roadSystemController = roadSystemController;
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }
}
