package edu.kit.rose.view.panel.segment;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.List;
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

  @Inject
  private AttributeController controller;

  /**
   * Creates a new bulk edit panel for a given collection of elements.
   */
  public BulkEditPanel(LocalizedTextProvider translator, RoadSystem roadSystem,
                       AttributeController controller, Collection<Element> elements) {
    super("bulk_edit_panel.fxml");


  }

  @Override
  public void init(Injector injector) {
    super.init(injector);

    attributePanel.setAttributes(controller.getBulkEditAccessors());
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return List.of(attributePanel);
  }
}
