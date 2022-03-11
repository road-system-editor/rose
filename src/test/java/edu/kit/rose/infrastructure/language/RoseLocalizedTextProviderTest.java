package edu.kit.rose.infrastructure.language;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseLocalizedTextProvider}.
 */
class RoseLocalizedTextProviderTest {
  @Test
  void testSetLanguage() {
    var translator = new RoseLocalizedTextProvider();

    translator.setSelectedLanguage(Language.GERMAN);
    Assertions.assertEquals(translator.getSelectedLanguage(), Language.GERMAN);

    translator.setSelectedLanguage(Language.ENGLISH);
    Assertions.assertEquals(translator.getSelectedLanguage(), Language.ENGLISH);
  }

  @Test
  void testSubscription() {
    var translator = new RoseLocalizedTextProvider();

    AtomicInteger subscriberCalls = new AtomicInteger();
    Consumer<Language> subscriber = lang -> {
      subscriberCalls.getAndIncrement();
    };

    translator.setSelectedLanguage(Language.ENGLISH);
    Assertions.assertEquals(0, subscriberCalls.get());

    translator.subscribeToOnLanguageChanged(subscriber);
    Assertions.assertEquals(0, subscriberCalls.get());

    translator.setSelectedLanguage(Language.GERMAN);
    Assertions.assertEquals(1, subscriberCalls.get());

    translator.unsubscribeFromOnLanguageChanged(subscriber);
    Assertions.assertEquals(1, subscriberCalls.get());

    translator.setSelectedLanguage(Language.ENGLISH);
    Assertions.assertEquals(1, subscriberCalls.get());
  }

  @Test
  void testSubscriptionOnlyFiresOnLanguageChange() {
    var translator = new RoseLocalizedTextProvider();
    translator.setSelectedLanguage(Language.ENGLISH);

    AtomicInteger subscriberCalls = new AtomicInteger();

    Consumer<Language> subscriber = lang -> {
      subscriberCalls.getAndIncrement();
    };

    translator.subscribeToOnLanguageChanged(subscriber);
    Assertions.assertEquals(0, subscriberCalls.get());

    translator.setSelectedLanguage(Language.GERMAN);
    Assertions.assertEquals(translator.getSelectedLanguage(), Language.GERMAN);
    Assertions.assertEquals(1, subscriberCalls.get());

    translator.setSelectedLanguage(Language.GERMAN);
    Assertions.assertEquals(translator.getSelectedLanguage(), Language.GERMAN);
    Assertions.assertEquals(1, subscriberCalls.get());

    translator.unsubscribeFromOnLanguageChanged(subscriber);
  }

  @Test
  void testTitleTranslation() {
    var translator = new RoseLocalizedTextProvider();

    translator.setSelectedLanguage(Language.ENGLISH);
    Assertions.assertEquals("Help", translator.getLocalizedText("view.window.menu.help"));

    translator.setSelectedLanguage(Language.GERMAN);
    Assertions.assertEquals("Hilfe", translator.getLocalizedText("view.window.menu.help"));
  }
}
