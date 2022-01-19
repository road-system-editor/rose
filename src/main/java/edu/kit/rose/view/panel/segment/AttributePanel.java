package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.FXMLContainer;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * An attribute panel allows the user to see and configure attributes.
 */
class AttributePanel extends FXMLContainer {
  private AttributeController controller;
  private SortedBox<AttributeAccessor<?>> attributes;

  @FXML
  private VBox layout;

  /**
   * Creates an empty attribute panel.
   * Requires {@link #setTranslator(LocalizedTextProvider)} +
   * {@link #setController(AttributeController)} + {@link #setAttributes(SortedBox)}
   */
  public AttributePanel() {
    super("attribute_editor.fxml");
  }

  /**
   * Sets the controller that handles attribute value updates.
   *
   * @param controller the controller that should handle attribute value updates.
   */
  public void setController(AttributeController controller) {
    this.controller = controller;
  }

  /**
   * Sets which attributes are shown in this panel.
   *
   * @param attributes the attributes to display.
   */
  public void setAttributes(SortedBox<AttributeAccessor<?>> attributes) {
    this.attributes = attributes;

    layout.getChildren().clear();
    EditableAttributeFactory factory = new EditableAttributeFactory(controller);
    for (AttributeAccessor<?> attribute : attributes) {
      layout.getChildren().add(factory.forAttribute(attribute));
    }
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }
}
