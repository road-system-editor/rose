package edu.kit.rose.view.panel.roadsystem;

import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.view.commons.FXMLContainer;

/**
 * The change button panel contains the buttons for undoing and redoing a changeable action.
 */
public class ChangeButtonPanel extends FXMLContainer {
  private ApplicationController controller;

  /**
   * Creates a new FXMLPanel and immediately mounts the components specified in the given FXML file ({@code fxmlResourceName}.
   * Requires {@link #setTranslator(LocalizedTextProvider)} + {@link #setController(ApplicationController)}
   */
  public ChangeButtonPanel() {
    super("change_button_panel.fxml");
  }

  /**
   * Sets the application controller
   *
   * @param controller
   */
  public void setController(ApplicationController controller) {
    this.controller = controller;
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }
}
