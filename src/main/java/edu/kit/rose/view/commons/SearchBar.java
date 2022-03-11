package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.language.Language;
import java.util.Collection;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * A search bar is a text input component whose input is used to filter the content of another
 * component.
 */
public class SearchBar extends FxmlContainer {

  @FXML
  private TextField searchTextField;

  /**
   * Creates a new search bar.
   */
  public SearchBar() {
    super("SearchBar.fxml");
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
    searchTextField.setPromptText(
        getTranslator().getLocalizedText("view.commons.searchbar.watermark"));
  }

  public StringProperty searchStringProperty() {
    return searchTextField.textProperty();
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
