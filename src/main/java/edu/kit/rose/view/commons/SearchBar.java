package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * A search bar is a text input component whose input is used to filter the content of another component.
 */
public class SearchBar extends FXMLContainer
    implements UnitObservable<SearchBar> { // also uses StackPane and css?
  @FXML
  private TextField searchTextField;

  /**
   * Creates a new search bar.
   * Requires {@link #setTranslator(LocalizedTextProvider)}
   */
  public SearchBar() {
    super("search_bar.fxml");
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
}
