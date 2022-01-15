package edu.kit.rose.infrastructure.language;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class RoseLocalizedTextProviderTest {
    @Test
    public void testSetLanguage() {
        var translator = new RoseLocalizedTextProvider();

        translator.setSelectedLanguage(Language.GERMAN);
        Assertions.assertEquals(translator.getSelectedLanguage(), Language.GERMAN);

        translator.setSelectedLanguage(Language.ENGLISH);
        Assertions.assertEquals(translator.getSelectedLanguage(), Language.ENGLISH);
    }

    @Test
    public void testSubscription() {
        var translator = new RoseLocalizedTextProvider();
        AtomicInteger subscriberCalls = new AtomicInteger();
        Consumer<Language> subscriber = lang -> {
            subscriberCalls.getAndIncrement();
            Assertions.assertEquals(Language.ENGLISH, lang);
        };

        translator.setSelectedLanguage(Language.ENGLISH);
        Assertions.assertEquals(0, subscriberCalls.get());

        translator.subscribeToOnLanguageChanged(subscriber);
        Assertions.assertEquals(0, subscriberCalls.get());

        translator.setSelectedLanguage(Language.ENGLISH);
        Assertions.assertEquals(1, subscriberCalls.get());

        translator.unsubscribeFromOnLanguageChanged(subscriber);
        Assertions.assertEquals(1, subscriberCalls.get());

        translator.setSelectedLanguage(Language.ENGLISH);
        Assertions.assertEquals(1, subscriberCalls.get());
    }
}
