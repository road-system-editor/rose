package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.UnmountUtility;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * This is the {@link EditableAttribute} implementation for the {@link edu.kit.rose.model.roadsystem.DataType} {@code STRING}.
 */
class StringAttribute extends EditableAttribute<String> {
  /**
   * Creates a new string attribute editor for the given {@code attribute}.
   *
   * @param attribute
   * @param controller
   */
  StringAttribute(AttributeAccessor<String> attribute, AttributeController controller) {
    super(attribute, controller);
    UnmountUtility.subscribeUntilUnmount(this, this, attribute);
  }

  @Override
  protected Node createInputField() {
    TextField tf = new TextField();
    tf.textProperty().setValue(getAttribute().getValue());
    tf.textProperty().addListener(
        (observable, oldVal, newVal) -> getController().setAttribute(getAttribute(), newVal));
    return new TextField();
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  @Override
  public void notifyChange(AttributeAccessor<String> unit) {

  }
}
