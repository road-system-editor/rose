package edu.kit.rose.controller.application;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LanguageSelector;
import edu.kit.rose.model.ApplicationDataSystem;

/**
 * Provides functionality for application settings
 * and other GUI related methods.
 *
 */
public class RoseApplicationController extends Controller implements ApplicationController {
  private final LanguageSelector languageSelector;

  /**
   * Creates a new {@link RoseApplicationController}.
   *
   * @param changeCommandBuffer   the buffer for change commands
   * @param storageLock           the coordinator for controller actions
   * @param navigator             the navigator for the controller
   * @param languageSelector      class that configures the applications language
   * @param applicationDataSystem the model facade for application data
   */
  public RoseApplicationController(ChangeCommandBuffer changeCommandBuffer, StorageLock storageLock,
                                   Navigator navigator, LanguageSelector languageSelector,
                                   ApplicationDataSystem applicationDataSystem) {
    super(storageLock, navigator);
    this.languageSelector = languageSelector;
  }

  @Override
  public void setLanguage(Language language) {
    //TODO check if this is everything that needs to be done here (implemented for testing the UI)
    languageSelector.setSelectedLanguage(language);
  }

  @Override
  public void showHelp() {

  }

  @Override
  public void undo() {

  }

  @Override
  public void redo() {

  }
}
