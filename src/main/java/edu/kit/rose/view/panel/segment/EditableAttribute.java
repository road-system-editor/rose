package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.FXMLContainer;
import edu.kit.rose.view.commons.UnmountUtility;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

/**
 * An editable attribute is a JavaFX component that allows the user to see and edit the value of
 * an attribute.
 *
 * @param <T> the java type of the attribute value.
 */
abstract class EditableAttribute<T> extends FXMLContainer
    implements UnitObserver<AttributeAccessor<T>> {
  private final AttributeAccessor<T> attribute;
  private final AttributeController controller;

  @FXML
  private CheckBox visibilitySwitch;
  @FXML
  private Label label;

  /**
   * Creates an editable attribute component for a given attribute accessor.
   */
  protected EditableAttribute(AttributeAccessor<T> attribute, AttributeController controller) {
    super("editable_attribute.fxml");
    this.attribute = attribute;
    this.controller = controller;
    UnmountUtility.subscribeUntilUnmount(this, this, attribute);
    getChildren().add(createInputField());
  }

  /**
   * Returns the attribute accessor that this component is bound to.
   *
   * @return the attribute accessor that this component is bound to.
   */
  protected AttributeAccessor<T> getAttribute() {
    return this.attribute;
  }

  /**
   * Returns the controller that is used for handling attribute value updates.
   *
   * @return the controller that is used for handling attribute value updates.
   */
  protected AttributeController getController() {
    return this.controller;
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  /**
   * Factory method that delegates the creation of the actual input field to the implementing class.
   */
  protected abstract Node createInputField();
}
