package edu.kit.rose.controller.application;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LanguageSelector;
import edu.kit.rose.model.ApplicationDataSystem;

/**
 * Provides functionality for application settings
 * and other GUI related methods.
 *
 */
public class RoseApplicationController extends Controller implements ApplicationController {


  /**
   * Creates a new {@link RoseApplicationController}.
   *
   * @param changeCommandBuffer   the buffer for change commands
   * @param storageLock           the coordinator for controller actions
   * @param languageSelector      class that configures the applications language
   * @param applicationDataSystem the model facade for application data
   */
  public RoseApplicationController(ChangeCommandBuffer changeCommandBuffer, StorageLock storageLock,
                                   LanguageSelector languageSelector,
                                   ApplicationDataSystem applicationDataSystem) {
    super(storageLock);
  }

  @Override
  public void setLanguage(Language language) {

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
