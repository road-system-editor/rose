package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.UnmountUtility;
import java.util.Objects;
import java.util.function.BiConsumer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

/**
 * An editable attribute is a JavaFX component that allows the user to see and edit the value of
 * an attribute.
 *
 * @param <T> the java type of the attribute value.
 */
abstract class EditableAttribute<T> extends FxmlContainer
    implements UnitObserver<AttributeAccessor<T>> {
  private static final String ATTRIBUTE_PANEL_STYLE =
      "/edu/kit/rose/view/panel/segment/AttributePanel.css";
  protected static final String INHOMOGENEOUS_VALUE_PLACEHOLDER = "—";
  private final AttributeAccessor<T> attribute;
  private final AttributeController controller;
  private final Tooltip tooltip;

  @FXML
  private HBox layout;
  @FXML
  private CheckBox visibilitySwitch;
  @FXML
  private Label label;

  protected final BiConsumer<AttributeAccessor<T>, T> consumer;

  /**
   * Creates an editable attribute component for a given attribute accessor.
   */
  protected EditableAttribute(AttributeAccessor<T> attribute, AttributeController controller,
                              BiConsumer<AttributeAccessor<T>, T> consumer) {
    super("EditableAttribute.fxml");
    this.attribute = Objects.requireNonNull(attribute);
    this.controller = Objects.requireNonNull(controller);
    this.consumer = consumer;
    setupView();
    Node inputField = createInputField();
    layout.getChildren().add(inputField);
    UnmountUtility.subscribeUntilUnmount(inputField, this, attribute);

    tooltip = new Tooltip();
    label.setTooltip(tooltip);
  }

  private void setupView() {
    String attributeStyleSheetUrl =
        Objects.requireNonNull(getClass().getResource(ATTRIBUTE_PANEL_STYLE)).toExternalForm();
    this.getStylesheets().add(attributeStyleSheetUrl);
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
    tooltip.setText(EnumLocalizationUtility
        .localizeAttributeTypeTitle(getTranslator(), attribute.getAttributeType()));
  }

  /**
   * Factory method that delegates the creation of the actual input field to the implementing class.
   */
  protected abstract Node createInputField();
}
