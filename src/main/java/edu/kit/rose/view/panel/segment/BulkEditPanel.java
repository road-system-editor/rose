package edu.kit.rose.view.panel.segment;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * The bulk edit panel allows the user to edit attributes of multiple segments simultaneously.
 */
public class BulkEditPanel extends FxmlContainer {
  private static final String ATTRIBUTE_PANEL_STYLE =
      "/edu/kit/rose/view/panel/segment/AttributePanel.css";

  @FXML
  private Label label;

  @FXML
  private VBox attributeList;

  @Inject
  private AttributeController controller;


  /**
   * Creates a new bulk edit panel for a given collection of elements.
   */
  public BulkEditPanel() {
    super("BulkEditPanel.fxml");
    setupView();

  }

  @Override
  public void init(Injector injector) {
    super.init(injector);
    setAttributes(injector);
    updateTranslatableStrings(getTranslator().getSelectedLanguage());
    //attributePanel.setAttributes(controller.getBulkEditAccessors());
  }

  private void setupView() {
    String attributeStyleSheetUrl =
        Objects.requireNonNull(getClass().getResource(ATTRIBUTE_PANEL_STYLE)).toExternalForm();
    this.getStylesheets().add(attributeStyleSheetUrl);
  }

  private void setAttributes(Injector injector) {
    attributeList.getChildren().clear();
    BulkEditableAttributeFactory factory = new BulkEditableAttributeFactory(controller);
    for (AttributeAccessor<?> attribute : controller.getBulkEditAccessors()) {
      EditableAttribute<?> editable = factory.forAttribute(attribute);
      editable.init(injector);
      attributeList.getChildren().add(editable);
      HBox.setHgrow(editable, Priority.ALWAYS);
    }
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {
    label.setText(getTranslator().getLocalizedText("view.panel.segment.bulkEditor"));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
