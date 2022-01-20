package edu.kit.rose.infrastructure.language;


import java.util.function.Consumer;

/**
 * Provides the functionality to retrieve localized Strings,
 * get their language and subscribe to get notified when
 * the language changes.
 */
public interface LocalizedTextProvider {

  /**
   * Returns the localized string/translated text that belongs to a given key.
   *
   * @param key name of the translated text
   * @return translated string that belongs to the key param.
   */
  String getLocalizedText(String key);

  /**
   * Returns the currently selected language.
   *
   * @return currently selected language
   */
  Language getSelectedLanguage();

  /**
   * Registers a function that will be called when the selected language changes.
   *
   * @param subscription function to be called
   */
  void subscribeToOnLanguageChanged(Consumer<Language> subscription);

  /**
   * Removes the registration of a given function that was registered with the
   * {@link #subscribeToOnLanguageChanged(Consumer)} method. If the function is not registered,
   * this method does nothing.
   *
   * @param subscription function to unregister
   */
  void unsubscribeFromOnLanguageChanged(Consumer<Language> subscription);
}
