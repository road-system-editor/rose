package edu.kit.rose.view.panel.segment;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * An attribute panel allows the user to see and configure attributes.
 */
public class AttributePanel extends FxmlContainer {
  private static final String ATTRIBUTE_PANEL_STYLE =
      "/edu/kit/rose/view/panel/segment/AttributePanel.css";

  @FXML
  private VBox attributeList;

  @Inject
  private AttributeController controller;
  @Inject
  private Injector injector;

  /**
   * Creates an empty attribute panel. Make sure to call {@link #init(Injector)} afterwards!
   */
  public AttributePanel() {
    super("AttributePanel.fxml");
    setupView();
  }

  private void setupView() {
    String attributeStyleSheetUrl =
        Objects.requireNonNull(getClass().getResource(ATTRIBUTE_PANEL_STYLE)).toExternalForm();
    this.getStylesheets().add(attributeStyleSheetUrl);
  }

  /**
   * Sets which attributes are shown in this panel.
   *
   * @param attributes the attributes to display.
   */
  public void setAttributes(SortedBox<AttributeAccessor<?>> attributes) {
    attributeList.getChildren().clear();
    EditableAttributeFactory factory = new EditableAttributeFactory(controller);
    for (AttributeAccessor<?> attribute : attributes) {
      EditableAttribute<?> editable = factory.forAttribute(attribute);
      editable.init(injector);
      attributeList.getChildren().add(editable);
      HBox.setHgrow(editable, Priority.ALWAYS);
    }
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
