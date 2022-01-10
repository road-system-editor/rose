package edu.kit.rose.infrastructure.language;

import java.util.Locale;

/**
 * Contains support languages and their {@link Locale}s.
 */
public enum Language {
    ENGLISH(new Locale("en")),
    GERMAN(new Locale("de"));

    private Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
