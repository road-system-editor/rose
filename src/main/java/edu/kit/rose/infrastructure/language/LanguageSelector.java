package edu.kit.rose.infrastructure.language;

/**
 * Provides the functionality to get and set the selected {@link Language}
 * of the class that implements this interface.
 */
public interface LanguageSelector {

    /**
     * Returns the currently selected language.
     * @return currently selected language
     */
    Language getSelectedLanguage();

    /**
     * Sets the selected language.
     * @param language language to be selected
     */
    void setSelectedLanguage(Language language);
}
