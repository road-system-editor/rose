package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * An editable attribute is a JavaFX component that allows the user to see and edit the value of
 * an attribute.
 *
 * @param <T> the java type of the attribute value.
 */
abstract class EditableAttribute<T> extends FxmlContainer
    implements UnitObserver<AttributeAccessor<T>> {
  protected static final String INHOMOGENEOUS_VALUE_PLACEHOLDER = "—";
  private final AttributeAccessor<T> attribute;
  private final AttributeController controller;

  @FXML
  private HBox layout;
  @FXML
  private CheckBox visibilitySwitch;
  @FXML
  private Label label;

  /**
   * Creates an editable attribute component for a given attribute accessor.
   */
  protected EditableAttribute(AttributeAccessor<T> attribute, AttributeController controller) {
    super("EditableAttribute.fxml");
    this.attribute = Objects.requireNonNull(attribute);
    this.controller = Objects.requireNonNull(controller);
    //UnmountUtility.subscribeUntilUnmount(this, this, attribute); TODO run later bc this is not
    // mounted yet
    layout.getChildren().add(createInputField());
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
    label.setText(EnumLocalizationUtility
        .localizeAttributeTypeTitle(getTranslator(), attribute.getAttributeType()));
  }

  /**
   * Factory method that delegates the creation of the actual input field to the implementing class.
   */
  protected abstract Node createInputField();
}
