package edu.kit.rose.infrastructure.language;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseLocalizedTextProvider}.
 */
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
  public void testSubscriptionOnlyFiresOnLanguageChange() {
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
  public void testTitleTranslation() {
    String ROSE_APPLICATION_NAME = "ROSE";
    var translator = new RoseLocalizedTextProvider();

    translator.setSelectedLanguage(Language.ENGLISH);
    Assertions.assertEquals(ROSE_APPLICATION_NAME, translator.getLocalizedText("ROSE_Application_Name"));

    translator.setSelectedLanguage(Language.GERMAN);
    Assertions.assertEquals(ROSE_APPLICATION_NAME, translator.getLocalizedText("ROSE_Application_Name"));
  }
}
