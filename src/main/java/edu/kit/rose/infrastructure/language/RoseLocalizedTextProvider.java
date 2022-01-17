package edu.kit.rose.infrastructure.language;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Provides the functionality to get translated versions of the same text, which is identified
 * by a unique name.
 * The language of this text can be changed and clients can observe {@link RoseLocalizedTextProvider},
 * to get notified when these changes occur.
 *
 * @implNote This class uses resource bundles for translations.
 */
public class RoseLocalizedTextProvider implements LocalizedTextProvider, LanguageSelector {
  private final List<Consumer<Language>> subscribers = new LinkedList<>();
  private Language language;
  private ResourceBundle resourceBundle;

  @Override
  public String getLocalizedText(String key) {
    return null;
  }

  @Override
  public Language getSelectedLanguage() {
    return this.language;
  }

  @Override
  public void setSelectedLanguage(Language language) {
    if (this.language != language) {
      loadLanguage(language);

      subscribers.forEach(subscriber -> subscriber.accept(language));
    }
  }

  private void loadLanguage(Language language) {
    this.resourceBundle = ResourceBundle.getBundle("edu.kit.rose.infrastructure.language.roseLocalization", Locale.forLanguageTag(language.getLocale().getLanguage()));
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
