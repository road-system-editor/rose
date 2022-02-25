package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;


/**
 * This is the {@link EditableAttribute} implementation for
 * {@link edu.kit.rose.model.roadsystem.DataType}s whose value needs to be selected out of a small
 * set of options (like enums and booleans).
 *
 * @param <T> the java type of the attribute value.
 */
abstract class SelectableAttribute<T> extends EditableAttribute<T> {
  private static final String ATTRIBUTE_PANEL_STYLE =
      "/edu/kit/rose/view/panel/segment/AttributePanel.css";

  private ComboBox<T> inputField;

  /**
   * Creates a new selectable attribute editor for the given {@code attribute} with the given
   * {@code options}.
   */
  SelectableAttribute(AttributeAccessor<T> attribute, AttributeController controller,
                      Collection<T> options, BiConsumer<AttributeAccessor<T>, T> consumer) {
    super(attribute, controller, consumer);
    setupView();
    this.inputField.getItems().addAll(Objects.requireNonNull(options));
    inputField.getSelectionModel().select(attribute.getValue());
  }

  private void setupView() {
    String attributeStyleSheetUrl =
        Objects.requireNonNull(getClass().getResource(ATTRIBUTE_PANEL_STYLE)).toExternalForm();
    this.getStylesheets().add(attributeStyleSheetUrl);
  }

  @Override
  protected Node createInputField() {
    this.inputField = new ComboBox<>();

    this.inputField.setPromptText(INHOMOGENEOUS_VALUE_PLACEHOLDER);
    inputField.getStyleClass().add("comboBox");
    inputField.setMaxHeight(10);
    inputField.setPrefWidth(150);

    inputField.setButtonCell(this.createListCell(null));
    inputField.setCellFactory(this::createListCell);

    inputField.getSelectionModel().selectedItemProperty().addListener(
        (options, old, newVal) -> consumer.accept(getAttribute(), newVal));

    return inputField;
  }

  private ListCell<T> createListCell(ListView<T> listView) {
    return new ListCell<>() {
      @Override
      protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null) {
          setText(INHOMOGENEOUS_VALUE_PLACEHOLDER);
        } else {
          setText(localizeOption(item));
        }
      }
    };
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
