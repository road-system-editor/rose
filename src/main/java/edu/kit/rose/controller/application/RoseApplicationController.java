package edu.kit.rose.controller.application;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.navigation.WindowType;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LanguageSelector;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;

/**
 * Provides functionality for application settings
 * and other GUI related methods.
 */
public class RoseApplicationController extends Controller implements ApplicationController {
  private final ChangeCommandBuffer changeCommandBuffer;
  private final LanguageSelector languageSelector;
  private final ApplicationDataSystem applicationDataSystem;

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
    this.changeCommandBuffer = changeCommandBuffer;
    this.languageSelector = languageSelector;
    this.applicationDataSystem = applicationDataSystem;

    setupLanguageBinding();
  }

  @Override
  public void setLanguage(Language language) {
    applicationDataSystem.setLanguage(language);
  }

  @Override
  public void showHelp() {
    getNavigator().showWindow(WindowType.HELP);
  }

  @Override
  public void undo() {
    changeCommandBuffer.undo();
  }

  @Override
  public void redo() {
    changeCommandBuffer.redo();
  }

  /**
   * Makes sure that the language of the text provider and the language of the application data
   * system stay in sync.
   */
  private void setupLanguageBinding() {
    this.languageSelector.setSelectedLanguage(this.applicationDataSystem.getLanguage());
    this.applicationDataSystem.addSubscriber(new SetObserver<>() {
      @Override
      public void notifyAddition(AttributeType unit) {
      }

      @Override
      public void notifyRemoval(AttributeType unit) {
      }

      @Override
      public void notifyChange(ApplicationDataSystem unit) {
        if (unit.getLanguage() != languageSelector.getSelectedLanguage()) {
          languageSelector.setSelectedLanguage(unit.getLanguage());
        }
      }
    });
  }
}
