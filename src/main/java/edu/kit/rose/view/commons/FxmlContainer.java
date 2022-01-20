package edu.kit.rose.view.commons;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import java.util.function.Consumer;
import javafx.scene.layout.Pane;

/**
 * FXML containers mount components specified in an FXML file provided by the subclass into
 * themselves.
 */
public abstract class FxmlContainer extends Pane {
  private final Consumer<Language> translatorSubscriber = this::updateTranslatableStrings;
  /**
   * Data source for translated strings.
   */
  @Inject
  private LocalizedTextProvider translator;

  /**
   * The Guice injector.
   */
  @Inject
  private Injector injector;


  /**
   * Creates a new FXMLPanel and immediately mounts the components specified in the given FXML
   * resource ({@code fxmlResourceName}).
   *
   * @param fxmlResourceName the name of the fxml resource.
   */
  public FxmlContainer(String fxmlResourceName) {
    FxmlUtility.loadFxml(this, this, this.getClass().getResource(fxmlResourceName));
  }

  /**
   * Returns the data source for string translation.
   *
   * @return the data source for string translation.
   */
  public LocalizedTextProvider getTranslator() {
    return translator;
  }


  /**
   * Template method that updates all visible strings in this container to the new translation.
   *
   * @param newLang the new language.
   */
  protected abstract void updateTranslatableStrings(Language newLang);

  /**
   * Initializes the {@link FXMLContainer} and its sub container.
   */
  public void init() {
    initSubContainer();
    initTranslator();
  }

  private void initSubContainer() {
    Collection<FXMLContainer> fxmlSubContainers = getSubFXMLContainer();

    if (fxmlSubContainers != null && injector != null) {
      for (FXMLContainer subContainer : fxmlSubContainers) {
        injector.injectMembers(subContainer);
        subContainer.init();
      }
    }
  }

  private void initTranslator() {
    if (this.translator != null) {
      this.translator.subscribeToOnLanguageChanged(c);
    }
  }

  /**
   * Hook method that returns a list of all sub {@link FXMLContainer}s of the current {@link FXMLContainer}
   * @return list of {@link FXMLUtility}s
   */
  protected abstract Collection<FXMLContainer> getSubFXMLContainer();
}
