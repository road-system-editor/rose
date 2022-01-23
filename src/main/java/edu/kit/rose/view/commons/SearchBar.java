package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * A search bar is a text input component whose input is used to filter the content of another
 * component.
 */
public class SearchBar extends FxmlContainer implements UnitObservable<SearchBar> {

  private final List<UnitObserver<SearchBar>> searchBarObserver = new ArrayList<>();

  @FXML
  private TextField searchTextField;

  private final ChangeListener<? super String> textChangedCallback
      = (o, e, n) -> notifySubscribers();

  /**
   * Creates a new search bar.
   */
  public SearchBar() {
    super("SearchBar.fxml");

    searchTextField.textProperty().addListener(textChangedCallback);
  }

  @Override
  public void addSubscriber(UnitObserver<SearchBar> observer) {
    if (!searchBarObserver.contains(observer)) {
      searchBarObserver.add(observer);
    }
  }

  @Override
  public void removeSubscriber(UnitObserver<SearchBar> observer) {
    searchBarObserver.remove(observer);
  }

  @Override
  public void notifySubscribers() {
    searchBarObserver.forEach(observer -> observer.notifyChange(getThis()));
  }

  @Override
  public SearchBar getThis() {
    return this;
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
    searchTextField.setPromptText(
        getTranslator().getLocalizedText("view.commons.searchbar.watermark"));
  }

  /**
   * Returns the current search string entered into the {@link SearchBar}.
   *
   * @return the current search string
   */
  public String getSearchString() {
    return searchTextField.getText();
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
