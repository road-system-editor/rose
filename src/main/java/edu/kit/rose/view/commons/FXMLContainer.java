package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;

/**
 * FXML containers mount components specified in an FXML file provided by the subclass into themselves.
 */
public abstract class FXMLContainer extends Pane {
  /**
   * implementation detail
   */
  private final Consumer<Language> c = this::updateTranslatableStrings;
  /**
   * Data source for translated strings.
   */
  private LocalizedTextProvider translator;

  /**
   * Creates a new FXMLPanel and immediately mounts the components specified in the given FXML resource ({@code fxmlResourceName}).
   *
   * @param fxmlResourceName
   */
  public FXMLContainer(String fxmlResourceName) {
    FXMLUtility.loadFXML(this, this, this.getClass().getResource(fxmlResourceName));
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
      this.translator.unsubscribeFromOnLanguageChanged(c);
    }
    this.translator = translator;
    this.translator.subscribeToOnLanguageChanged(c);
  }

  /**
   * Template method that updates all visible strings in this container to the new translation.
   *
   * @param newLang the new language.
   */
  protected abstract void updateTranslatableStrings(Language newLang);
}
