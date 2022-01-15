package edu.kit.rose.infrastructure.language;

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
    private Language language;

    @Override
    public void setSelectedLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String getLocalizedText(String key) {
        return null;
    }

    @Override
    public Language getSelectedLanguage() {
        return this.language;
    }

    @Override
    public void subscribeToOnLanguageChanged(Consumer<Language> subscription) {

    }

    @Override
    public void unsubscribeFromOnLanguageChanged(Consumer<Language> subscription) {

    }


}
