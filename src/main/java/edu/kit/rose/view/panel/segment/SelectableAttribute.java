package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.LocalizedComboBox;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
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

  private LocalizedComboBox<T> inputField;

  private ChangeListener<T> listener;

  /**
   * Creates a new selectable attribute editor for the given {@code attribute} with the given
   * {@code options}.
   */
  SelectableAttribute(AttributeAccessor<T> attribute, AttributeController controller,
                      Collection<T> options, BiConsumer<AttributeAccessor<T>, T> consumer) {
    super(attribute, controller, consumer);
    setupStyle();
    this.inputField.getItems().addAll(Objects.requireNonNull(options));

    selectWithoutListener(attribute.getValue());
  }

  private void setupStyle() {
    String attributeStyleSheetUrl =
        Objects.requireNonNull(getClass().getResource(ATTRIBUTE_PANEL_STYLE)).toExternalForm();
    this.getStylesheets().add(attributeStyleSheetUrl);
  }

  private void setupListener() {
    this.listener = (observable, oldValue, newValue) -> consumer.accept(getAttribute(), newValue);
  }

  @Override
  protected Node createInputField() {
    setupListener();
    this.inputField = new LocalizedComboBox<>();

    this.inputField.setPromptText(INHOMOGENEOUS_VALUE_PLACEHOLDER);
    inputField.getStyleClass().add("comboBox");
    inputField.setMaxHeight(10);
    inputField.setPrefWidth(150);

    inputField.setButtonCell(this.createListCell(null));
    inputField.setCellFactory(this::createListCell);

    inputField.getSelectionModel().selectedItemProperty().addListener(getListener());

    this.inputField.init(INHOMOGENEOUS_VALUE_PLACEHOLDER, this::localizeOption);
    return inputField;
  }

  private ChangeListener<T> getListener() {
    return this.listener;
  }

  private ListCell<T> createListCell(ListView<T> listView) {
    return new ListCell<>() {
      @Override
      protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
          setText(INHOMOGENEOUS_VALUE_PLACEHOLDER);
        } else {
          if (listView != null) {
            inputField.putCell(item, this);
          }
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
  protected void updateTranslatableStrings(Language newLang) {
    super.updateTranslatableStrings(newLang);
    this.inputField.updateLocalization();
  }

  @Override
  public void notifyChange(AttributeAccessor<T> unit) {
    if (inputField.getSelectionModel().getSelectedItem() != unit.getValue()) {
      selectWithoutListener(unit.getValue());
    }
  }

  private void selectWithoutListener(T value) {
    inputField.getSelectionModel().selectedItemProperty().removeListener(getListener());
    Platform.runLater(() -> {
      inputField.getSelectionModel().select(value);
      inputField.getSelectionModel().selectedItemProperty().addListener(getListener());
    });
  }

  /**
   * Create a localized description of the given {@code option}.
   *
   * @param option the selectable option to create a localized description for, may not be null.
   * @return the localized description of the option.
   */
  protected abstract String localizeOption(T option);
}
