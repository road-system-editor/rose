package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.BiConsumer;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * This is the {@link EditableAttribute} implementation for
 * {@link edu.kit.rose.model.roadsystem.DataType}s whose values needs to be typed in by the user.
 */
public abstract class TextFieldAttribute<T> extends EditableAttribute<T> {
  private static final String ATTRIBUTE_PANEL_STYLE =
      "/edu/kit/rose/view/panel/segment/AttributePanel.css";
  private static final String INVALID_VALUE_STYLE_CLASS = "invalid-value";

  /**
   * This attribute is {@code true} when this component is currently trying to insert an
   * "inhomogeneous" value into the input field and the input should not be validated.
   */
  private boolean inhomogeneousInsertion = false;

  private TextField inputField;

  /**
   * Creates an editable attribute component for a given attribute accessor.
   */
  protected TextFieldAttribute(AttributeAccessor<T> attribute,
                               AttributeController controller,
                               BiConsumer<AttributeAccessor<T>, T> consumer) {
    super(attribute, controller, consumer);
    setupTextFieldAttributeStyle();
  }

  private void setupTextFieldAttributeStyle() {
    String attributeStyleSheetUrl =
        Objects.requireNonNull(getClass().getResource(ATTRIBUTE_PANEL_STYLE)).toExternalForm();
    this.getStylesheets().add(attributeStyleSheetUrl);
  }

  /**
   * Checks whether the content of the input text field is valid.
   * An input should never be considered valid if it can not be parsed.
   *
   * @param input the input field content.
   * @return whether the given input is valid.
   */
  protected abstract boolean validate(String input);

  /**
   * Parses a given input valid into a value of the attribute type.
   * This function will only be called if {@link #validate(String)} returned {@code true}.
   *
   * @param input the input field content
   * @return the parsed value.
   */
  protected abstract T parse(String input);

  @Override
  protected Node createInputField() {
    inputField = new TextField();
    inputField.getStyleClass().add("textField");
    updateInputField();

    inputField.textProperty().addListener(this::onInputFieldUpdate);
    return inputField;
  }

  private void onInputFieldUpdate(ObservableValue<? extends String> observable,
                                  String oldVal,
                                  String newVal) {
    if (!inhomogeneousInsertion) {
      boolean valid = this.validate(newVal);
      setValid(valid);

      if (valid) {
        this.consumer.accept(getAttribute(), parse(newVal));
      }
    }
  }

  private void setValid(boolean valid) {
    boolean invalid = !valid;
    boolean hasInvalidClass = this.inputField.getStyleClass().contains(INVALID_VALUE_STYLE_CLASS);

    if (invalid != hasInvalidClass) {
      if (invalid) {
        this.inputField.getStyleClass().add(INVALID_VALUE_STYLE_CLASS);
      } else {
        this.inputField.getStyleClass().remove(INVALID_VALUE_STYLE_CLASS);
      }
    }
  }

  @Override
  public void notifyChange(AttributeAccessor<T> unit) {
    updateInputField();
  }

  private void updateInputField() {
    var newValue = getAttribute().getValue();
    if (newValue == null) {
      inhomogeneousInsertion = true;
      this.inputField.setText(INHOMOGENEOUS_VALUE_PLACEHOLDER);
      inhomogeneousInsertion = false;
    } else {
      this.inputField.setText(newValue.toString());
    }
    setValid(true);
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return Collections.emptyList();
  }
}
