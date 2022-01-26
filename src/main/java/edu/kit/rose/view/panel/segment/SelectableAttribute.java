package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.function.Function;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;


/**
 * This is the {@link EditableAttribute} implementation for
 * {@link edu.kit.rose.model.roadsystem.DataType}s whose value needs to be selected out of a small
 * set of options (like enums and booleans).
 *
 * @param <T> the java type of the attribute value.
 */
abstract class SelectableAttribute<T> extends EditableAttribute<T> {
  
  private final ComboBox<T> inputField;
  
  private final Collection<T> options;

  /**
   * Creates a new selectable attribute editor for the given {@code attribute} with the given
   * {@code options}.
   */
  SelectableAttribute(AttributeAccessor<T> attribute, AttributeController controller,
                      Collection<T> options) {
    super(attribute, controller);
    this.options = options;
    this.inputField = new ComboBox<>();
  }

  @Override
  protected Node createInputField() {
    ObservableList<T> o = new SimpleListProperty<>();
    o.addAll(options);
    inputField.setItems(o);

    inputField.setCellFactory(listView -> new ListCell<>() {
      @Override
      protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
          setText(INHOMOGENEOUS_VALUE_PLACEHOLDER);
        } else {
          setText(localizeOption(item));
        }
      }
    });

    inputField.getSelectionModel().selectedItemProperty().addListener(
        (options, old, newVal) -> getController().setAttribute(getAttribute(), newVal));

    return inputField;
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }

  @Override
  public void notifyChange(AttributeAccessor<T> unit) {

  }

  /**
   * Create a localized description of the given {@code option}.
   *
   * @param option the selectable option to create a localized description for, may not be null.
   * @return the localized description of the option.
   */
  protected abstract String localizeOption(T option);
}
