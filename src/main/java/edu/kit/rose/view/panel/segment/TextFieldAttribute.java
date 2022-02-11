package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * This is the {@link EditableAttribute} implementation for
 * {@link edu.kit.rose.model.roadsystem.DataType}s whose values needs to be typed in by the user.
 */
public abstract class TextFieldAttribute<T> extends EditableAttribute<T> {
  private static final String TEXT_FIELD_STYLE = "edu/kit/rose/view/panel/segment/TextField.css";
  /**
   * This attribute is {@code true} when this component is currently trying to insert an
   * "inhomogeneous" value into the input field and the input should not be validated.
   */
  private boolean inhomogeneousInsertion = false;

  private TextField inputField; //TODO make final and init immediately

  /**
   * Creates an editable attribute component for a given attribute accessor.
   */
  protected TextFieldAttribute(AttributeAccessor<T> attribute,
                               AttributeController controller) {
    super(attribute, controller);
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
    inputField.setStyle("-fx-border-radius: 15; -fx-background-radius: 15; -fx-font-family: "
        + "Arial; -fx-font-size: 12");
    updateInputField();

    inputField.textProperty().addListener(this::onInputFieldUpdate);
    return inputField;
  }

  private void onInputFieldUpdate(ObservableValue<? extends String> observable,
                                  String oldVal,
                                  String newVal) {
    if (!inhomogeneousInsertion) {
      if (this.validate(newVal)) {
        getController().setAttribute(getAttribute(), parse(newVal));
      } else {
        inputField.setText(oldVal);
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
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
