package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.Collection;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

/**
 * This is the {@link EditableAttribute} implementation for
 * {@link edu.kit.rose.model.roadsystem.DataType}s whose value needs to be selected out of a small
 * set of options (like enums and booleans).
 *
 * @param <T> the java type of the attribute value.
 */
class SelectableAttribute<T> extends EditableAttribute<T> {
  private final Collection<T> options;

  /**
   * Creates a new selectable attribute editor for the given {@code attribute} with the given
   * {@code options}.
   */
  SelectableAttribute(AttributeAccessor<T> attribute, AttributeController controller,
                      Collection<T> options) {
    super(attribute, controller);
    this.options = options;
  }

  @Override
  protected Node createInputField() {
    ComboBox<T> cb = new ComboBox<>();
    ObservableList<T> o = new SimpleListProperty<>();
    o.addAll(options);
    cb.setItems(o);

    cb.getSelectionModel().selectedItemProperty().addListener((options, old, newVal) -> {
      getController().setAttribute(getAttribute(), newVal);
    });

    return cb;
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  @Override
  protected List<FXMLContainer> getSubFXMLContainer() {
    return null;
  }

  @Override
  public void notifyChange(AttributeAccessor<T> unit) {

  }
}
