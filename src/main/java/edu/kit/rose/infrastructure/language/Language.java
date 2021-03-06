package edu.kit.rose.infrastructure.language;

import java.util.Locale;

/**
 * Contains support languages and their {@link Locale}s.
 */
public enum Language {

  /**
   * References the english language.
   */
  ENGLISH(new Locale("en")),
  /**
   * References the german language.
   */
  GERMAN(new Locale("de"));

  /**
   * This is the default language.
   */
  public static final Language DEFAULT = Language.ENGLISH;

  private final Locale locale;

  /**
   * Constructor.
   * Requires a Locale that wraps the associated iso-language-code.
   *
   * @param locale the locale that specifies the language
   */
  Language(Locale locale) {
    this.locale = locale;
  }

  /**
   * Returns the {@link Locale} associated with the language.
   *
   * @return the locale assigned to a language value
   */
  public Locale getLocale() {
    return locale;
  }
}
