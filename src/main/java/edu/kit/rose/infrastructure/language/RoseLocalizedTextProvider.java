package edu.kit.rose.infrastructure.language;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Provides the functionality to get translated versions of the same text, which is identified
 * by a unique name.
 * The language of this text can be changed and clients can observe 
 {@link RoseLocalizedTextProvider},
 * to get notified when these changes occur.
 *
 * @implNote This class uses resource bundles for translations.
 */
public class RoseLocalizedTextProvider implements LocalizedTextProvider, LanguageSelector {
  private final List<Consumer<Language>> subscribers = new LinkedList<>();
  private Language language;
  private ResourceBundle resourceBundle;

  /**
   * Creates an instance of the {@link RoseLocalizedTextProvider} class
   * with {@link Language}.ENGLISH as selected language.
   */
  public RoseLocalizedTextProvider() {
    loadLanguage(Language.DEFAULT);
  }

  /**
   * Creates an instance of the {@link RoseLocalizedTextProvider} class
   * with an initial language.
   *
   * @param language the language to initialize the {@link RoseLocalizedTextProvider} instance with
   */
  public RoseLocalizedTextProvider(Language language) {
    if (language != null) {
      loadLanguage(language);
    } else {
      loadLanguage(Language.DEFAULT);
    }
  }

  @Override
  public String getLocalizedText(String key) {
    return resourceBundle.getString(key);
  }

  @Override
  public Language getSelectedLanguage() {
    return this.language;
  }

  @Override
  public void setSelectedLanguage(Language language) {
    if (language != null && this.language != language) {
      loadLanguage(language);

      subscribers.forEach(subscriber -> subscriber.accept(language));
    }
  }

  private void loadLanguage(Language language) {

    this.resourceBundle = ResourceBundle.getBundle(
            "edu.kit.rose.infrastructure.language.RoseTextResources",
            language.getLocale());
    this.language = language;
  }

  @Override
  public void subscribeToOnLanguageChanged(Consumer<Language> subscription) {
    this.subscribers.add(subscription);
  }

  @Override
  public void unsubscribeFromOnLanguageChanged(Consumer<Language> subscription) {
    this.subscribers.remove(subscription);
  }
}
