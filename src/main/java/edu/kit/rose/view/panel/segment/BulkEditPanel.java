package edu.kit.rose.view.panel.segment;

import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The bulk edit panel allows the user to edit attributes of multiple segments simultaneously.
 */
public class BulkEditPanel extends FxmlContainer {
  @FXML
  private Label label;
  @FXML
  private AttributePanel attributePanel;

  /**
   * Creates a new bulk edit panel for a given collection of elements.
   */
  public BulkEditPanel(LocalizedTextProvider translator, RoadSystem roadSystem,
                       AttributeController controller, Collection<Element> elements) {
    super("bulk_edit_panel.fxml");

    attributePanel.setController(controller);
    attributePanel.setAttributes(roadSystem.getSharedAttributeAccessors(elements));
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
