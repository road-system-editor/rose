package edu.kit.rose.view.commons;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import java.util.Collection;
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
   * Initializes the {@link FxmlContainer} and its sub container.
   *
   * @param injector the dependency injector.
   */
  public void init(Injector injector) {
    injector.injectMembers(this);

    initSubContainer(injector);
    initTranslator();
  }

  private void initSubContainer(Injector injector) {
    Collection<FxmlContainer> fxmlSubContainers = getSubFxmlContainer();

    if (fxmlSubContainers != null && injector != null) {
      for (FxmlContainer subContainer : fxmlSubContainers) {
        injector.injectMembers(subContainer);
        subContainer.init(injector);
      }
    }
  }

  private void initTranslator() {
    if (this.translator != null) {
      this.translator.subscribeToOnLanguageChanged(translatorSubscriber);
    }
  }

  /**
   * Hook method that returns a list of all sub
   * {@link FxmlContainer}s of the current {@link FxmlContainer}.
   *
   * @return list of {@link FxmlContainer}s
   */
  protected abstract Collection<FxmlContainer> getSubFxmlContainer();
}
