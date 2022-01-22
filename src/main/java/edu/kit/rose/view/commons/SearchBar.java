package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * A search bar is a text input component whose input is used to filter the content of another
 * component.
 */
public class SearchBar extends FxmlContainer implements UnitObservable<SearchBar> {
  @FXML
  private TextField searchTextField;

  /**
   * Creates a new search bar.
   * Requires {@link #setTranslator(LocalizedTextProvider)}
   */
  public SearchBar() {
    super("SearchBar.fxml");
  }

  @Override
  public void addSubscriber(UnitObserver<SearchBar> observer) {

  }

  @Override
  public void removeSubscriber(UnitObserver<SearchBar> observer) {

  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public SearchBar getThis() {
    return this;
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
