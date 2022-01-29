package edu.kit.rose.controller.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.navigation.WindowType;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LanguageSelector;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link RoseApplicationController}.
 */
public class RoseApplicationControllerTest {
  ChangeCommandBuffer changeCommandBuffer;
  StorageLock storageLock;
  Navigator navigator;
  LanguageSelector languageSelector;
  ApplicationDataSystem applicationDataSystem;
  RoseApplicationController controller;

  SetObserver<AttributeType, ApplicationDataSystem> applicationDataSystemSetObserver;

  @BeforeEach
  void beforeEach() {
    this.changeCommandBuffer = mock(ChangeCommandBuffer.class);
    this.storageLock = mock(StorageLock.class);
    this.navigator = mock(Navigator.class);
    this.languageSelector = mock(LanguageSelector.class);
    this.applicationDataSystem = mock(ApplicationDataSystem.class);

    doAnswer(invocation -> this.applicationDataSystemSetObserver = invocation.getArgument(0))
        .when(this.applicationDataSystem).addSubscriber(any());

    this.controller = new RoseApplicationController(this.changeCommandBuffer,
        this.storageLock,
        this.navigator,
        this.languageSelector,
        this.applicationDataSystem);
  }

  @Test
  void testShowHelp() {
    this.controller.showHelp();
    verify(navigator, times(1)).showWindow(WindowType.HELP);
  }

  @Test
  void testUndo() {
    this.controller.undo();
    verify(changeCommandBuffer, times(1)).undo();
  }

  @Test
  void testRedo() {
    this.controller.redo();
    verify(changeCommandBuffer, times(1)).redo();
  }

  @Test
  void setLanguage() {
    this.controller.setLanguage(Language.GERMAN);
    verify(applicationDataSystem, times(1)).setLanguage(Language.GERMAN);
  }

  @Test
  void testLanguageBinding() {
    // simulate application data system had its language changed
    when(applicationDataSystem.getLanguage()).thenReturn(Language.GERMAN);
    this.applicationDataSystemSetObserver.notifyChange(this.applicationDataSystem);

    verify(this.languageSelector, atLeastOnce()).setSelectedLanguage(Language.GERMAN);
  }
}
