package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import java.util.function.Consumer;
import javafx.scene.layout.Pane;

/**
 * FXML containers mount components specified in an FXML file provided by the subclass into
 * themselves.
 */
public abstract class FxmlContainer extends Pane {
  private final Consumer<Language> translatorSubscriber = this::updateTranslatableStrings;
  /**
   * Data source for translated strings.
   */
  private LocalizedTextProvider translator;

  /**
   * Creates a new FXMLPanel and immediately mounts the components specified in the given FXML
   * resource ({@code fxmlResourceName}).
   *
   * @param fxmlResourceName the name of the fxml resource.
   */
  public FxmlContainer(String fxmlResourceName) {
    FxmlUtility.loadFxml(this, this, this.getClass().getResource(fxmlResourceName));
  }

  /**
   * Returns the data source for string translation.
   *
   * @return the data source for string translation.
   */
  public LocalizedTextProvider getTranslator() {
    return translator;
  }

  /**
   * Sets the data source for string translation.
   *
   * @param translator the new data source for string translation, may not be null.
   */
  public void setTranslator(LocalizedTextProvider translator) {
    if (this.translator != null) {
      this.translator.unsubscribeFromOnLanguageChanged(translatorSubscriber);
    }
    this.translator = translator;
    this.translator.subscribeToOnLanguageChanged(translatorSubscriber);
  }

  /**
   * Template method that updates all visible strings in this container to the new translation.
   *
   * @param newLang the new language.
   */
  protected abstract void updateTranslatableStrings(Language newLang);
}
